/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.AgreementArticle;
import com.sat.entity.Grades;
import com.sat.entity.Internship;
import com.sat.entity.InternshipAgreement;
import com.sat.entity.Organization;
import com.sat.entity.StudyCompetence;
import com.sat.entity.User;
import com.sat.entity.mapper.GradesExtractor;
import com.sat.entity.mapper.InternshipAgreementsExtractor;
import com.sat.entity.mapper.StudyProgramsExtractor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dean
 */
@Repository
public class InternshipAgreementDao implements InternshipAgreementDaoI{

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    private OrganizationDao organizationService;
    
    /*
    `id` int(11) NOT NULL AUTO_INCREMENT,
  `organization_id` int(11) NOT NULL,
  `student_username` varchar(64) CHARACTER SET utf8 NOT NULL,
  `period_begin` date NOT NULL,
  `period_end` date NOT NULL,
  `study_program_id` int(11) NOT NULL,
  `school_id` varchar(64) CHARACTER SET utf8 NOT NULL,
  `version_num` int(11) NOT NULL DEFAULT '1',
    */
    @Override
    public InternshipAgreement applyForInternship(final User currentUser, final Integer internship_id){
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = 
                            "   INSERT into INTERNSHIP_AGREEMENT (organization_id,student_username,period_begin,period_end,study_program_id,school_id, version_num, school_organizer, document_number) "
                            + " SELECT isp.organization_id, ?, null, null, isp.study_program_id, ?, 1, null, null "
                            + "   FROM INTERNSHIP_STUDYPROGRAMS_MAP isp where isp.id=? ";
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"id"} );
                    
                    stmt.setString(1, currentUser.getUsername() );
                    stmt.setString(2, currentUser.getSchool());
                    stmt.setInt(3, internship_id);
                    
                    return stmt;
                }
            },
            keyHolder);
        int new_id=keyHolder.getKey().intValue();
        
        return find(new_id, currentUser);
    }
    
    private void addStudyCompetencesFromInternship(final User currentUser, final Integer internship_id, final Integer internshipAgreement_id){
        String sql1 = 
                 " INSERT INTO INTERNSHIPAGREEMENT_STUDYCOMPETENCES_MAP (internship_agreement_id, study_competences_id)"
                + "SELECT  ?, ispc.study_competences_id "
                + "  FROM INTERNSHIP_STUDYPROGRAMS_COMPETENCES_MAP as ispc"
                + "  where ispc.internship_studyprograms_map_id = ?  "
                + "  ";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, internshipAgreement_id );
                    stmt.setInt(2, internship_id );
                }
            }
        );
    }
    
    @Override
    public int addInternshipAgreement(final InternshipAgreement internshipAgreement, final User currentUser) {
        
        
        final Organization org;
        if (internshipAgreement.getOrganization().getId()==0){
            org=organizationService.addOrganization(internshipAgreement.getOrganization(),currentUser);
        } else {
            org=internshipAgreement.getOrganization();
        }
                
        
        
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = 
                            "   INSERT into INTERNSHIP_AGREEMENT "
                            + " (organization_id,student_username,period_begin,period_end,study_program_id,school_id, version_num, mentor, grade_exam, grade_final, grade_date, school_organizer, document_number, ects_points, sum_of_hours) "
                            + " values(?,?,?,?,?,?,1,?, ?,?,?,   ?,?,   ?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"id"} );
                    
                    stmt.setInt(1, org.getId());
                    stmt.setString(2, internshipAgreement.getStudent().getUsername());
                    if (internshipAgreement.getPeriodBegin()!=null) stmt.setDate(3, internshipAgreement.getPeriodBegin()); else stmt.setNull(3, Types.DATE);
                    if (internshipAgreement.getPeriodEnd()!=null) stmt.setDate(4, internshipAgreement.getPeriodEnd()); else stmt.setNull(4,Types.DATE);
                    stmt.setInt(5,internshipAgreement.getStudyProgram().getId());
                    stmt.setString(6,currentUser.getSchool());
                    stmt.setString(7, internshipAgreement.getMentor().getUsername());
                    if (internshipAgreement.getGrade_exam()!=null) stmt.setInt(8,internshipAgreement.getGrade_exam()); else stmt.setNull(8,Types.INTEGER);
                    if (internshipAgreement.getGrade_final()!=null) stmt.setInt(9,internshipAgreement.getGrade_final()); else stmt.setNull(9,Types.INTEGER);
                    if (internshipAgreement.getGrade_date()!=null) stmt.setDate(10,internshipAgreement.getGrade_date()); else stmt.setNull(10,Types.DATE);
                    
                    if (internshipAgreement.getSchool_organizer()!=null) stmt.setString(11,internshipAgreement.getSchool_organizer().getUsername()); else stmt.setNull(11,Types.NVARCHAR);
                    if (internshipAgreement.getDocument_number()!=null) stmt.setString(12,internshipAgreement.getDocument_number()); else stmt.setNull(12,Types.NVARCHAR);

                    if (internshipAgreement.getEcts_points()!=null) stmt.setInt(13,internshipAgreement.getEcts_points()); else stmt.setNull(13,Types.INTEGER);
                    if (internshipAgreement.getGrade_final()!=null) stmt.setInt(14,internshipAgreement.getSum_of_hours()); else stmt.setNull(14,Types.INTEGER);
                    return stmt;
                }
            },
            keyHolder);
        int new_org_id=keyHolder.getKey().intValue();
        
        if (internshipAgreement.getArticles().size()>0){
            addArticlesCustom(new_org_id,currentUser,internshipAgreement.getArticles());
        }
        else{
            addArticlesFromTemplate(new_org_id,currentUser);
        }
        
        if (internshipAgreement.getCompetences().size()>0){
            addStudyCompetencesForStudyprogram(new_org_id, internshipAgreement.getStudyProgram().getId(), currentUser,internshipAgreement.getCompetences());
        } 
        
        return new_org_id;
    }
    
    private void addStudyCompetencesForStudyprogram(final Integer internship_agreement_id, final Integer studyprogram_id, final User currentUser, final List<StudyCompetence> competences){
        String sql0 = " DELETE from INTERNSHIPAGREEMENT_STUDYCOMPETENCES_MAP where internship_agreement_id=? ";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, internship_agreement_id );
                }
            }
        );
        
        String ids="";
        for (int i=0;i<competences.size();i++){
            if (i!=0)ids=ids+",";
            ids=ids+competences.get(i).getId(); 
        }
        
        String sql1 = "INSERT INTO INTERNSHIPAGREEMENT_STUDYCOMPETENCES_MAP (internship_agreement_id, study_competences_id) " +
                      "    SELECT ? as IA_ID, id as SC_ID" +
                      "    FROM STUDY_COMPETENCES as sc where sc.study_program_id IN " +
                      "         (select sdp.ID from STUDY_PROGRAMS as sdp WHERE sdp.id=? or sdp.study_program_id=?) " +
                      "   and sc.id in ("+ids+")";
        
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, internship_agreement_id );
                    stmt.setInt(2, studyprogram_id );
                    stmt.setInt(3, studyprogram_id );
                }
            }
        );
        
    }
    
    private void addArticlesCustom(final Integer internship_agreement_id, final User currentUser, final List<AgreementArticle> articles){
        
        String sql0 = " DELETE from INTERNSHIP_AGREEMENT_ARTICLES where internshipagreement_id=? ";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, internship_agreement_id );
                }
            }
        );
        
        String sql1 = "INSERT INTO INTERNSHIP_AGREEMENT_ARTICLES (school_id, internshipagreement_id, ordnum, content, version_num) "
                + "    VALUES (?,?,?,?,"
                + "             (select version_num from INTERNSHIP_AGREEMENT ia where ia.id=? ) "
                + "            )";
        jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt, int i) throws SQLException {
                    stmt.setString(1, currentUser.getSchool());
                    stmt.setInt(2, internship_agreement_id);
                    stmt.setInt(3, articles.get(i).getOrdnum() );
                    if (articles.get(i).getContent()!=null) stmt.setString(4,articles.get(i).getContent()); else stmt.setNull(4, Types.VARCHAR);
                    stmt.setInt(5, internship_agreement_id);
                }

                @Override
                public int getBatchSize() {
                    return articles.size();
                }
            }
        );
    }
    private void addArticlesFromTemplate(final Integer internship_agreement_id, final User currentUser){
        String sql1 = 
                 " INSERT INTO INTERNSHIP_AGREEMENT_ARTICLES (school_id, internshipagreement_id, ordnum, content, version_num)"
                + "SELECT aat.school_id, ia.id, aat.ordnum, aat.content, ia.version_num"
                + "  FROM AGREEMENT_ARTICLES_TEMPLATE as aat, INTERNSHIP_AGREEMENT as ia"
                + "  where aat.school_id=? and ia.id=? "
                + "  ";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    
                    stmt.setString(1, currentUser.getSchool() );
                    stmt.setInt(2, internship_agreement_id );
                    
                }
            }
        );
    }

    @Override
    public void editInternshipAgreement(final InternshipAgreement internshipAgreement, final User currentUser) {
        String sql1 = 
                 " UPDATE INTERNSHIP_AGREEMENT "
                + "  SET organization_id=?,"
                + "      student_username=?,"
                + "      period_begin=?,"
                + "      period_end=?,"
                + "      study_program_id=?,"
                + "      school_id=?, "
                + "      version_num=version_num+1,"
                + "      mentor=?, "
                + "      grade_exam=?, grade_final=?, grade_date=?, "
                + "      school_organizer=?, document_number=?, "
                + "      ects_points=?, sum_of_hours=? "
                + " WHERE id=? ";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    
                    stmt.setInt(1, internshipAgreement.getOrganization().getId());
                    stmt.setString(2, internshipAgreement.getStudent().getUsername());
                    if (internshipAgreement.getPeriodBegin()!=null) stmt.setDate(3, internshipAgreement.getPeriodBegin()); else stmt.setNull(3, Types.DATE);
                    if (internshipAgreement.getPeriodEnd()!=null) stmt.setDate(4, internshipAgreement.getPeriodEnd()); else stmt.setNull(4,Types.DATE);
                    stmt.setInt(5,internshipAgreement.getStudyProgram().getId());
                    stmt.setString(6,currentUser.getSchool());
                    if (internshipAgreement.getMentor()!=null) stmt.setString(7,internshipAgreement.getMentor().getUsername()); else  stmt.setNull(7,Types.INTEGER);
                    
                    if (internshipAgreement.getGrade_exam()!=null) stmt.setInt(8,internshipAgreement.getGrade_exam()); else stmt.setNull(8,Types.INTEGER);
                    if (internshipAgreement.getGrade_final()!=null) stmt.setInt(9,internshipAgreement.getGrade_final()); else stmt.setNull(9,Types.INTEGER);
                    if (internshipAgreement.getGrade_date()!=null) stmt.setDate(10,internshipAgreement.getGrade_date()); else stmt.setNull(10,Types.DATE);
                    
                    if (internshipAgreement.getSchool_organizer()!=null) stmt.setString(11,internshipAgreement.getSchool_organizer().getUsername()); else stmt.setNull(11,Types.NVARCHAR);
                    if (internshipAgreement.getDocument_number()!=null) stmt.setString(12,internshipAgreement.getDocument_number()); else stmt.setNull(12,Types.NVARCHAR);
                    
                    if (internshipAgreement.getEcts_points()!=null) stmt.setInt(13,internshipAgreement.getEcts_points()); else stmt.setNull(13,Types.INTEGER);
                    if (internshipAgreement.getGrade_final()!=null) stmt.setInt(14,internshipAgreement.getSum_of_hours()); else stmt.setNull(14,Types.INTEGER);
                    
                    stmt.setInt(15, internshipAgreement.getId() );
                }
            }
        );
        
        addArticlesCustom(internshipAgreement.getId(),currentUser,internshipAgreement.getArticles());
        addStudyCompetencesForStudyprogram(internshipAgreement.getId(), internshipAgreement.getStudyProgram().getId(), currentUser,internshipAgreement.getCompetences());
    }

    @Override
    public void deleteInternshipAgreement(final Integer internshipAgreement_id, final User currentUser) {
        String sql1 = 
                 " DELETE from INTERNSHIP_AGREEMENT where id=? and school_id=? ";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, internshipAgreement_id );
                    stmt.setString(2, currentUser.getSchool() );
                }
            }
        );
    }

    @Override
    public InternshipAgreement find(Integer internshipAgreement_id, User currentUser) {
        List<InternshipAgreement> ret=findAll(currentUser, false, null,null, internshipAgreement_id, null);
        if (ret!=null && ret.size()>0) return ret.get(0);
        return null;
    }

    @Override
    public List<InternshipAgreement> findAll(User currentUser, boolean unsigned_only, String student_username, Integer organization_id, Integer internship_agreement_id, String mentor_username) {
        
        List<InternshipAgreement> ret=new ArrayList<InternshipAgreement>();
        String sql = 
                " SELECT "
                + "    ia.id as ia_id,"
                + "    o.id as o_id, o.name as o_name, o.orgtype as o_orgtype, o.cityname as o_cityname, o.cityzipcode as o_cityzipcode, o.streetname as o_streetname, o.contact_username as o_contact_username, o.school_id as o_school_id, "
                + "    st.username as st_username, "
                + "    st.school_id as st_school_id, st.phone as st_phone, st.email as st_email, st.cityname as st_cityname, st.cityzipcode as st_cityzipcode, st.streetname as st_streetname, st.organization_id as st_organization_id, st.studyprogram_id as st_studyprogram_id, st.first_name as st_first_name, st.last_name as st_last_name, st.jwt as st_jwt, "
                + "    ia.period_begin as ia_periodbegin, ia.period_end as ia_periodend, "
                + "    sp.id as sp_id, sp.name as sp_name, sp.description as sp_description, "
                + "    ia.school_id as ia_school_id, "
                + "    ia.version_num as ia_version_num, "
                + "    iaa.id as iaa_id, iaa.content as iaa_content, "
                + "    sc.id as sc_id, sc.name as sc_name, sc.description as sc_description, "
                + "  (select ias_st.username    from INTERNSHIP_AGREEMENT_SIGNATURES as ias_st where ias_st.internship_agreement_id=ia.id and ias_st.signed_version=ia.version_num and ias_st.signed_role='STUDENT' order by ias_st.signed_date desc limit 1) as ias_st_username, "
                + "  (select ias_st.signed_date from INTERNSHIP_AGREEMENT_SIGNATURES as ias_st where ias_st.internship_agreement_id=ia.id and ias_st.signed_version=ia.version_num and ias_st.signed_role='STUDENT' order by ias_st.signed_date desc limit 1) as ias_st_signed_date, "
                + "  (select ias_sc.username    from INTERNSHIP_AGREEMENT_SIGNATURES as ias_sc where ias_sc.internship_agreement_id=ia.id and ias_sc.signed_version=ia.version_num and ias_sc.signed_role='SCHOOL' order by ias_sc.signed_date desc limit 1) as ias_sc_username, "
                + "  (select ias_sc.signed_date from INTERNSHIP_AGREEMENT_SIGNATURES as ias_sc where ias_sc.internship_agreement_id=ia.id and ias_sc.signed_version=ia.version_num and ias_sc.signed_role='SCHOOL' order by ias_sc.signed_date desc limit 1) as ias_sc_signed_date, "
                + "  (select ias_or.username    from INTERNSHIP_AGREEMENT_SIGNATURES as ias_or where ias_or.internship_agreement_id=ia.id and ias_or.signed_version=ia.version_num and ias_or.signed_role='ORGANIZATION' order by ias_or.signed_date desc limit 1) as ias_or_username, "
                + "  (select ias_or.signed_date from INTERNSHIP_AGREEMENT_SIGNATURES as ias_or where ias_or.internship_agreement_id=ia.id and ias_or.signed_version=ia.version_num and ias_or.signed_role='ORGANIZATION' order by ias_or.signed_date desc limit 1) as ias_or_signed_date, "
                //+ "    ias_st.username as ias_st_username, ias_st.signed_date as ias_st_signed_date, "
                //+ "    ias_sc.username as ias_sc_username, ias_sc.signed_date as ias_sc_signed_date, "
                //+ "    ias_or.username as ias_or_username, ias_or.signed_date as ias_or_signed_date "
                + "   ia.mentor as ia_mentor,   "
                + "    um.school_id as um_school_id, um.phone as um_phone, um.email as um_email, um.cityname as um_cityname, um.cityzipcode as um_cityzipcode, um.streetname as um_streetname, um.organization_id as um_organization_id, um.studyprogram_id as um_studyprogram_id, um.first_name as um_first_name, um.last_name as um_last_name, um.jwt as um_jwt, "
                + "   ia.grade_exam as ia_grade_exam, ia.grade_final as ia_grade_final, ia.grade_date as ia_grade_date, "
                + "   ia.school_organizer as ia_school_organizer, ia.document_number as ia_document_number, "
                + "    so.school_id as so_school_id, so.phone as so_phone, so.email as so_email, so.cityname as so_cityname, so.cityzipcode as so_cityzipcode, so.streetname as so_streetname, so.organization_id as so_organization_id, so.studyprogram_id as so_studyprogram_id, so.first_name as so_first_name, so.last_name as so_last_name, so.jwt as so_jwt, "
                + "   ia.ects_points as ia_ects_points, ia.sum_of_hours as ia_sum_of_hours "
                + " FROM INTERNSHIP_AGREEMENT ia "
                + " JOIN ORGANIZATION o on (o.id=ia.organization_id) "
                + " JOIN USERS st on st.username=ia.student_username "
                + " left outer JOIN USERS um on um.username=ia.mentor "
                + " left outer JOIN USERS so on so.username=ia.school_organizer "
                + " JOIN STUDY_PROGRAMS sp on (sp.id=ia.study_program_id) "
                + " LEFT OUTER JOIN INTERNSHIPAGREEMENT_STUDYCOMPETENCES_MAP iasc on (iasc.internship_agreement_id=ia.id) "
                + " LEFT OUTER JOIN STUDY_COMPETENCES sc on (sc.id=iasc.study_competences_id)"
                + " LEFT OUTER JOIN INTERNSHIP_AGREEMENT_ARTICLES iaa on (iaa.internshipagreement_id=ia.id and iaa.school_id=ia.school_id and iaa.version_num=ia.version_num) "
                //+ " LEFT OUTER JOIN INTERNSHIP_AGREEMENT_SIGNATURES ias_st on (ias_st.internship_agreement_id=ia.id and ias_st.signed_version=ia.version_num and ias_st.signed_role='STUDENT')    "
                //+ " LEFT OUTER JOIN INTERNSHIP_AGREEMENT_SIGNATURES ias_sc on (ias_sc.internship_agreement_id=ia.id and ias_sc.signed_version=ia.version_num and ias_sc.signed_role='SCHOOL')     "
                //+ " LEFT OUTER JOIN INTERNSHIP_AGREEMENT_SIGNATURES ias_or on (ias_or.internship_agreement_id=ia.id and ias_or.signed_version=ia.version_num and ias_or.signed_role='ORGANIZATION')     "
                + " WHERE ia.school_id=? "
                + "   AND st.username=coalesce(?,st.username) "
                + "   AND o.id=coalesce(?,o.id) "
                + "   AND ia.id=coalesce(?,ia.id) "
                + "   AND ia.mentor=coalesce(?,ia.mentor) "
                + (unsigned_only?"   HAVING (ias_st_username is null OR ias_sc_username is null OR ias_or_username is null)":"")
                + " ORDER by ia.id asc, iaa.ordnum";
        
        ret=jdbcTemplate.query(
                sql,
                new Object[] {currentUser.getSchool(),student_username, organization_id, internship_agreement_id, mentor_username},
                new InternshipAgreementsExtractor()
            );
        return ret;
    }

    @Override
    public void studentSign(final User currentUser, final Integer internshipAgreement_id) {
        
        String sql1 = 
                 " INSERT into INTERNSHIP_AGREEMENT_SIGNATURES (internship_agreement_id,username,signed_role,signed_date,signed_version)"
               + " SELECT ia.id, ?, 'STUDENT', cast(now() as DATETIME), ia.version_num "
               + "  FROM INTERNSHIP_AGREEMENT as ia where ia.id=? and ia.school_id=? ";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1, currentUser.getUsername() );
                    stmt.setInt(2, internshipAgreement_id );
                    stmt.setString(3, currentUser.getSchool() );
                }
            }
        );
    }

    @Override
    public void organizationSign(final User currentUser, final Integer internshipAgreement_id) {
        String sql1 = 
                 " INSERT into INTERNSHIP_AGREEMENT_SIGNATURES (internship_agreement_id,username,signed_role,signed_date,signed_version)"
               + " SELECT ia.id, ?, 'ORGANIZATION', cast(now() as DATETIME), ia.version_num "
               + "  FROM INTERNSHIP_AGREEMENT as ia where ia.id=? and ia.school_id=? ";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1, currentUser.getUsername() );
                    stmt.setInt(2, internshipAgreement_id );
                    stmt.setString(3, currentUser.getSchool() );
                }
            }
        );
    }

    @Override
    public void schoolSign(final User currentUser, final Integer internshipAgreement_id) {
        String sql1 = 
                 " INSERT into INTERNSHIP_AGREEMENT_SIGNATURES (internship_agreement_id,username,signed_role,signed_date,signed_version)"
               + " SELECT ia.id, ?, 'SCHOOL', cast(now() as DATETIME), ia.version_num "
               + "  FROM INTERNSHIP_AGREEMENT as ia where ia.id=? and ia.school_id=? ";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1, currentUser.getUsername() );
                    stmt.setInt(2, internshipAgreement_id );
                    stmt.setString(3, currentUser.getSchool() );
                }
            }
        );
    }

    @Override
    public List<Grades> getGrades(User currentUser, Integer internshipAgreement_id) {
        List<Grades> ret=new ArrayList<Grades>();
                
        ret = jdbcTemplate.query(
                
                    " SELECT grade, grade_text, mentor_username, "
                  + "        sc.id as sc_id, sc.name as sc_name, sc.description as sc_description, "
                  + "        ia.id as ia_id, ia.organization_id as ia_organization_id, ia.student_username as ia_student_username, "
                  + "        ia.period_begin as ia_period_begin, ia.period_end as ia_period_end, ia.study_program_id as ia_study_program_id"
                  + "   FROM INTERNSHIPAGREEMENT_STUDYCOMPETENCES_MAP iascm "
                  + "   JOIN STUDY_COMPETENCES sc on (sc.id=iascm.study_competences_id) "
                  + "   JOIN INTERNSHIP_AGREEMENT ia on (ia.id=iascm.internship_agreement_id)"
                  + "   LEFT OUTER JOIN INTERNSHIPAGREEMENT_GRADES iag "
                  + "        on (iag.internship_agreement_id=iascm.internship_agreement_id "
                  + "            and iag.study_competences_id=iascm.study_competences_id "
                  + "           ) "
                  + "  WHERE iascm.internship_agreement_id=? ",
                new Object[] { internshipAgreement_id }, 
                new GradesExtractor()
            );
        
        return ret;
    }

    @Override
    public List<Grades> storeGrades(final User currentUser, final List<Grades> grades) {
        if (grades==null) return null;
        if (grades.size()<=0) return null;
        
        final Integer internship_agreement_id=grades.get(0).getInternshipAgreement().getId();
        
        String sql0 = 
                 " DELETE FROM INTERNSHIPAGREEMENT_GRADES where internship_agreement_id=?";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, internship_agreement_id );
                }
            }
        );
        
        String sql1 = "INSERT INTO INTERNSHIPAGREEMENT_GRADES (grade, grade_text, mentor_username, internship_agreement_id, study_competences_id) "
                + "    VALUES (?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt, int i) throws SQLException {
                    Integer grade=grades.get(i).getGrade();
                    if (grade!=null){
                        if (grade<1) grade=null;
                        if (grade>5) grade=null;
                    }
                    stmt.setInt(1, grade);
                    stmt.setString(2, grades.get(i).getGrate_text());
                    stmt.setString(3, currentUser.getUsername());
                    stmt.setInt(4, grades.get(i).getInternshipAgreement().getId());
                    stmt.setInt(5, grades.get(i).getStudyCompetence().getId());
                }

                @Override
                public int getBatchSize() {
                    return grades.size();
                }
            }
        );
     
        return getGrades(currentUser, internship_agreement_id);
    }
    
}
