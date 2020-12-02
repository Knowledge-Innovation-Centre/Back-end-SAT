/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.Assignment;
import com.sat.entity.InternshipAgreement;
import com.sat.entity.StudentWorklog;
import com.sat.entity.StudyCompetence;
import com.sat.entity.User;
import com.sat.entity.Worklog;
import com.sat.entity.mapper.AssignmentExtractor;
import com.sat.entity.mapper.StudentWorklogExtractor;
import com.sat.entity.mapper.StudyCompetenceExtractor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dean
 */
@Repository
public class ReportDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    private InternshipAgreementDao internshipAgreementService;
    
    @Transactional
    public Assignment updateAssignment(User currentUser, final Assignment assignment){
        
        jdbcTemplate.update(
                "UPDATE Assignment SET "+
                " internship_agreement_id=?, "+
                " description=?, "+
                " deadline=? "+
                " where id=?", 
                new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1,assignment.getInternship_agreement().getId());
                    stmt.setString(2, assignment.getDescription());
                    stmt.setDate(3, assignment.getDeadline());
                    stmt.setInt(4, assignment.getId());
                }
            }
        );
        
        jdbcTemplate.update(
                "DELETE from Assignment_StudyCompetences_Map where assignment_id=?", 
                new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1,assignment.getId());
                }
            }
        );
        
        if (assignment.getStudy_competences()!=null){
            jdbcTemplate.batchUpdate(
                    " INSERT into Assignment_StudyCompetences_Map (studycompetence_id, assignment_id) VALUES (?,?)",
                    new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement stmt, int i) throws SQLException {
                        stmt.setInt(1, assignment.getStudy_competences().get(i).getId() );
                        stmt.setInt(2, assignment.getId() );
                    }
                    @Override
                    public int getBatchSize() {
                        return assignment.getStudy_competences().size();
                    }
                }
            );
        }
        
        
        Assignment a=findById(currentUser, assignment.getId());
        return a;
    }
    
    @Transactional
    public void deleteAssignment(User currentUser, final int assignment_id){
        
        jdbcTemplate.update(
                "DELETE from Assignment_StudyCompetences_Map where assignment_id=?", 
                new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1,assignment_id);
                }
            }
        );
        
        jdbcTemplate.update(
                "DELETE from StudentWorklog where assignment_id=?", 
                new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1,assignment_id);
                }
            }
        );
            
        jdbcTemplate.update(
                "DELETE from Assignment where id=?", 
                new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1,assignment_id);
                }
            }
        );
    }
    
    @Transactional
    public Assignment createAssignment(User currentUser, final Assignment assignment){
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = "INSERT into Assignment (internship_agreement_id,description,deadline) values(?,?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"id"} );
                    if (assignment.getInternship_agreement()!=null) stmt.setInt(1, assignment.getInternship_agreement().getId()); else stmt.setNull(1,Types.INTEGER);
                    if (assignment.getDescription()!=null) stmt.setString(2, assignment.getDescription()); else stmt.setNull(2,Types.VARCHAR);
                    if (assignment.getDeadline()!=null) stmt.setDate(3, assignment.getDeadline()); else stmt.setNull(3,Types.DATE);
                    return stmt;
                }
            },
            keyHolder);
        final int new_id=keyHolder.getKey().intValue();
        
        if (assignment.getStudy_competences()!=null){
        
            jdbcTemplate.batchUpdate(
                    " INSERT into Assignment_StudyCompetences_Map (studycompetence_id, assignment_id) VALUES (?,?)",
                    new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement stmt, int i) throws SQLException {
                        stmt.setInt(1, assignment.getStudy_competences().get(i).getId() );
                        stmt.setInt(2, new_id );
                    }
                    @Override
                    public int getBatchSize() {
                        return assignment.getStudy_competences().size();
                    }
                }
            );
        }
        
        Assignment a=findById(currentUser, new_id);
        return a;
    }
    
    public List<Assignment>fetchAssigmentsForInternshipAgreement(User currentUser, int internship_agreement_id){
        
        List<Assignment> ret = jdbcTemplate.query(
            " SELECT "+
            "      a.id as a_id, a.description as a_description, a.deadline as a_deadline, a.internship_agreement_id as a_internship_agreement_id,  "+
            "      sw.id as sw_id,  sw.student_log, sw.date_studentlog, sw.mentor_log, sw.date_mentorlog     "+
            "      "+
            " FROM Assignment as a      "+
            " JOIN INTERNSHIP_AGREEMENT as ia on (ia.id=a.internship_agreement_id)    "+
            " LEFT OUTER JOIN StudentWorklog as sw on (sw.assignment_id=a.id)     "+
            "      "+
            " where a.internship_agreement_id = ? and ia.school_id=? ",
            new Object[] { internship_agreement_id, currentUser.getSchool() }, 
            new AssignmentExtractor()
        );
        
        ret=populateDeepData(ret,currentUser);
        
        return ret;
    }
    public Assignment findByWorklogId(User currentUser, int worklog_id){
        List<Assignment> ret = jdbcTemplate.query(
            " SELECT "+
            "      a.id as a_id, a.description as a_description, a.deadline as a_deadline, a.internship_agreement_id as a_internship_agreement_id,  "+
            "      sw.id as sw_id,  sw.student_log, sw.date_studentlog, sw.mentor_log, sw.date_mentorlog     "+
            "      "+
            " FROM Assignment as a      "+
            " JOIN INTERNSHIP_AGREEMENT as ia on (ia.id=a.internship_agreement_id)    "+
            " LEFT OUTER JOIN StudentWorklog as sw on (sw.assignment_id=a.id)     "+
            "      "+
            " where a.id = (select assignment_id from StudentWorklog where id=?) ",
            new Object[] { worklog_id }, 
            new AssignmentExtractor()
        );
        
        ret=populateDeepData(ret,currentUser);
        if (ret==null) return null;
        if (ret.size()==0) return null;
        
        return ret.get(0);
    }
    public Assignment findById(User currentUser, int assignment_id){
        
        List<Assignment> ret = jdbcTemplate.query(
            " SELECT "+
            "      a.id as a_id, a.description as a_description, a.deadline as a_deadline, a.internship_agreement_id as a_internship_agreement_id,  "+
            "      sw.id as sw_id,  sw.student_log, sw.date_studentlog, sw.mentor_log, sw.date_mentorlog     "+
            "      "+
            " FROM Assignment as a      "+
            " JOIN INTERNSHIP_AGREEMENT as ia on (ia.id=a.internship_agreement_id)    "+
            " LEFT OUTER JOIN StudentWorklog as sw on (sw.assignment_id=a.id)     "+
            "      "+
            " where a.id = ? and ia.school_id=? ",
            new Object[] { assignment_id, currentUser.getSchool() }, 
            new AssignmentExtractor()
        );
        
        ret=populateDeepData(ret,currentUser);
        if (ret==null) return null;
        if (ret.size()==0) return null;
        
        return ret.get(0);
    }
    
    private List<Assignment> populateDeepData(List<Assignment> list, User currentUser){
        Assignment a;
        InternshipAgreement ia;
        for (int i=0;i<list.size();i++){
            a=list.get(i);
            if (a.getId()!=null && a.getInternship_agreement()!=null){
                ia=internshipAgreementService.find(a.getInternship_agreement().getId(), currentUser);
                a.setInternship_agreement(ia);
            }
            List<StudyCompetence> scl= jdbcTemplate.query(
                " SELECT "+
                "      sc.id, sc.name, sc.description  "+
                "      "+
                " FROM STUDY_COMPETENCES as sc      "+
                "  JOIN Assignment_StudyCompetences_Map as map on (map.studycompetence_id=sc.id)"+
                "      "+
                " where map.assignment_id = ? ",
                new Object[] { a.getId() }, 
                new StudyCompetenceExtractor()
            );
            a.setStudy_competences(scl);
        }
        return list;
    }
    
    
    public List<StudentWorklog>fetchWorklog4Assignment(User currentUser, int assignment_id){
        
        List<StudentWorklog> ret = jdbcTemplate.query(
            " SELECT "+
            "      sw.id as sw_id,  sw.student_log, sw.date_studentlog, sw.mentor_log, sw.date_mentorlog     "+
            "      "+
            " FROM StudentWorklog as sw "+
            "      "+
            " where sw.assignment_id = ? ",
            new Object[] { assignment_id }, 
            new StudentWorklogExtractor()
        );
        
        return ret;
    }
    
    public void addWorklog4Assignment(User currentUser, final Worklog wlog, final int assignment_id){
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = "INSERT into StudentWorklog (assignment_id,student_log,date_studentlog) values(?,?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"id"} );
                    stmt.setInt(1, assignment_id);
                    if (wlog.getLog()!=null) stmt.setString(2, wlog.getLog()); else stmt.setNull(2,Types.VARCHAR);
                    if (wlog.getDate_log()!=null) stmt.setDate(3, wlog.getDate_log()); else stmt.setNull(3,Types.DATE);
                    return stmt;
                }
            },
            keyHolder);
        final int new_id=keyHolder.getKey().intValue();
    }
    public void commentWorklog(User currentUser, final Worklog wlog, final int worklog_id){
        jdbcTemplate.update(
                "UPDATE StudentWorklog set mentor_log=?,date_mentorlog=? where id=? ", 
                new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    if (wlog.getLog()!=null) stmt.setString(1, wlog.getLog()); else stmt.setNull(1,Types.VARCHAR);
                    if (wlog.getDate_log()!=null) stmt.setDate(2, wlog.getDate_log()); else stmt.setNull(2,Types.DATE);
                    stmt.setInt(3, worklog_id);
                }
            }
        );
    }
}
