/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.AgreementArticle;
import com.sat.entity.InternshipAgreement;
import com.sat.entity.Organization;
import com.sat.entity.StudyCompetence;
import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author Dean
 * 
 * private int id;
    private Organization organization; (id,name)
    private User student; (username)
    private Date periodBegin;
    private Date periodEnd;
    private StudyProgram studyProgram; (id, name, description)
    private String school_id;
    private int version_num;
    private List<AgreementArticle> articles; (id, content)
    * 
    * signed_student_username+date
    * signed_school_username+date
    * signed_organization_username+date
    * 
    * 
                + "    ia.id as ia_id,  "
                + "    o.id as o_id, o.name as o_name, "
                + "    st.usernmame as st_username, "
                + "    ia.periodBegin as ia_periodbegin, ia.periodEnd as ia_periodend, "
                + "    sp.id as sp_id, sp.name as sp_name, sp.description as sp_description, "
                + "    ia.school_id as ia_school_id, "
                + "    ia.version_num as ia_version_num, "
                + "    iaa.id as iaa_id, iaa.content as iaa_content, "
                + "    ias_st.username as ias_st_username, ias_st.signed_date as ias_st_signed_date, "
                + "    ias_sc.username as ias_sc_username, ias_sc.signed_date as ias_sc_signed_date, "
                + "    ias_or.username as ias_or_username, ias_or.signed_date as ias_or_signed_date "
                * 
                * sc.id as sc_id, sc.name as sc_name, sc.description as sc_description
    * 
 */
public class InternshipAgreementsExtractor implements ResultSetExtractor<List<InternshipAgreement>>{

    @Override
    public List<InternshipAgreement> extractData(ResultSet rs) throws SQLException, DataAccessException {
        
        Hashtable<Integer, InternshipAgreement> hash = new Hashtable<Integer, InternshipAgreement>();
        
        InternshipAgreement ia=null;
        Integer id;
        
        
        while (rs.next()){
            id=rs.getInt("ia_id");
            ia=hash.get(id);
            if (ia==null) {
                ia=new InternshipAgreement();
                ia.setId(id);
                ia.setPeriodBegin(rs.getDate("ia_periodbegin"));
                ia.setPeriodEnd(rs.getDate("ia_periodend"));
                ia.setSchool_id(rs.getString("ia_school_id"));
                ia.setVersion_num(rs.getInt("ia_version_num"));
                ia.setSigned_student_username(rs.getString("ias_st_username"));
                ia.setSigned_student_date(rs.getDate("ias_st_signed_date"));
                ia.setSigned_school_username(rs.getString("ias_sc_username"));
                ia.setSigned_school_date(rs.getDate("ias_sc_signed_date"));
                ia.setSigned_organization_username(rs.getString("ias_or_username"));
                ia.setSigned_organization_date(rs.getDate("ias_or_signed_date"));
                ia.setEcts_points(rs.getInt("ia_ects_points"));
                ia.setSum_of_hours(rs.getInt("ia_sum_of_hours"));
                User mentor=new User();
                mentor.setUsername(rs.getString("ia_mentor"));
                if (!rs.wasNull()) {
                    /* so.school_id as so_school_id, so.phone as so_phone, so.email as so_email, so.cityname as so_cityname, 
                       so.cityzipcode as so_cityzipcode, so.streetname as so_streetname, so.organization_id as so_organization_id, 
                        so.studyprogram_id as so_studyprogram_id, so.first_name as so_first_name, so.last_name as so_last_name, so.jwt as so_jwt
                    */
                    mentor.setSchool(rs.getString("um_school_id"));
                    mentor.setPhone(rs.getString("um_phone"));
                    mentor.setEmail(rs.getString("um_email"));
                    mentor.setCityname(rs.getString("um_cityname"));
                    mentor.setCityzipcode(rs.getString("um_cityzipcode"));
                    mentor.setStreetname(rs.getString("um_streetname"));
                    mentor.setFirst_name(rs.getString("um_first_name"));
                    mentor.setLast_name(rs.getString("um_last_name"));
                    mentor.setJwt(rs.getBoolean("um_jwt"));
                    
                    ia.setMentor(mentor);
                }
                Integer g=rs.getInt("ia_grade_exam");
                if (!rs.wasNull()) ia.setGrade_exam(g);
                g=rs.getInt("ia_grade_final");
                if (!rs.wasNull()) ia.setGrade_final(g);
                Date gd=rs.getDate("ia_grade_date");
                if (!rs.wasNull()) ia.setGrade_date(gd);
                
                //ia.school_organizer as ia_school_organizer, ia.document_number as ia.document_number 
                User school_organizer = new User();
                school_organizer.setUsername(rs.getString("ia_school_organizer"));
                if (!rs.wasNull()) {
                    /* so.school_id as so_school_id, so.phone as so_phone, so.email as so_email, so.cityname as so_cityname, 
                       so.cityzipcode as so_cityzipcode, so.streetname as so_streetname, so.organization_id as so_organization_id, 
                        so.studyprogram_id as so_studyprogram_id, so.first_name as so_first_name, so.last_name as so_last_name, so.jwt as so_jwt
                    */
                    school_organizer.setSchool(rs.getString("so_school_id"));
                    school_organizer.setPhone(rs.getString("so_phone"));
                    school_organizer.setEmail(rs.getString("so_email"));
                    school_organizer.setCityname(rs.getString("so_cityname"));
                    school_organizer.setCityzipcode(rs.getString("so_cityzipcode"));
                    school_organizer.setStreetname(rs.getString("so_streetname"));
                    school_organizer.setFirst_name(rs.getString("so_first_name"));
                    school_organizer.setLast_name(rs.getString("so_last_name"));
                    school_organizer.setJwt(rs.getBoolean("so_jwt"));
                    
                    ia.setSchool_organizer(school_organizer);
                }
                
                String document_number=rs.getString("ia_document_number");
                if (!rs.wasNull()) ia.setDocument_number(document_number);
            }
            Organization org=ia.getOrganization();
            if (org==null){
                org=new Organization();
                org.setId(rs.getInt("o_id"));
                org.setName(rs.getString("o_name"));
                org.setOrgtype(rs.getString("o_orgtype"));
                org.setCityname(rs.getString("o_cityname"));
                org.setCityzipcode(rs.getString("o_cityzipcode"));
                org.setStreetname(rs.getString("o_streetname"));
                org.setContact_username(rs.getString("o_contact_username"));
                org.setSchool_id(rs.getString("o_school_id")); 
                ia.setOrganization(org);
            }
                
            User student=ia.getStudent();
            if (student==null){
                student=new User();
                student.setUsername(rs.getString("st_username"));
                
                /* so.school_id as so_school_id, so.phone as so_phone, so.email as so_email, so.cityname as so_cityname, 
                       so.cityzipcode as so_cityzipcode, so.streetname as so_streetname, so.organization_id as so_organization_id, 
                        so.studyprogram_id as so_studyprogram_id, so.first_name as so_first_name, so.last_name as so_last_name, so.jwt as so_jwt
                    */
                    student.setSchool(rs.getString("st_school_id"));
                    student.setPhone(rs.getString("st_phone"));
                    student.setEmail(rs.getString("st_email"));
                    student.setCityname(rs.getString("st_cityname"));
                    student.setCityzipcode(rs.getString("st_cityzipcode"));
                    student.setStreetname(rs.getString("st_streetname"));
                    student.setFirst_name(rs.getString("st_first_name"));
                    student.setLast_name(rs.getString("st_last_name"));
                    student.setJwt(rs.getBoolean("st_jwt"));
                    
                    
                ia.setStudent(student);
            }
            StudyProgram sp=ia.getStudyProgram();
            if (sp==null){
                sp=new StudyProgram();
                sp.setId(rs.getInt("sp_id"));
                sp.setName(rs.getString("sp_name"));
                sp.setDescription(rs.getString("sp_description"));
                ia.setStudyProgram(sp);
            }
            List<AgreementArticle> articles=ia.getArticles();
            if (articles==null) articles=new ArrayList<AgreementArticle>();
            AgreementArticle aa=null;
            int aa_id=rs.getInt("iaa_id");
            for (int i=0;i<articles.size();i++){
                if (aa_id == articles.get(i).getId()){
                    aa=articles.get(i);
                }
            }
            if (aa==null) {
                aa=new AgreementArticle();
                aa.setId(aa_id);
                aa.setContent(rs.getString("iaa_content"));
                articles.add(aa);
            }
            ia.setArticles(articles);
            
            List<StudyCompetence> competences=ia.getCompetences();
            if (competences==null) competences=new ArrayList<StudyCompetence>();
            StudyCompetence sc=null;
            int sc_id=rs.getInt("sc_id");
            for (int i=0;i<competences.size();i++){
                if (sc_id == competences.get(i).getId()){
                    sc=competences.get(i);
                }
            }
            if (sc==null){
                if (sc_id!=0){
                    sc=new StudyCompetence();
                    sc.setId(sc_id);
                    sc.setName(rs.getString("sc_name"));
                    sc.setDescription(rs.getString("sc_description"));
                    competences.add(sc);
                }
            }
            ia.setCompetences(competences);
            
            hash.put(id, ia);
        }
        
        List<InternshipAgreement> ret=new ArrayList<InternshipAgreement>();
        Enumeration enu=hash.elements();
        while(enu.hasMoreElements()){
            ret.add((InternshipAgreement)enu.nextElement());
        }
        return ret;
    }
    
}
