/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.EvaluationAnswer;
import com.sat.entity.EvaluationQuestion;
import com.sat.entity.User;
import com.sat.entity.mapper.EvaluationAnswersExtractor;
import com.sat.entity.mapper.EvaluationQuestionsExtractor;
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
 * @author dean
 */
@Repository
public class EvaluationDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public List<EvaluationQuestion> getQuestionsForRole(final User currentUser, final String forRole){
        List<EvaluationQuestion> ret=new ArrayList<EvaluationQuestion>();
        
        String sql = 
                " SELECT "
                + "    eq.id as eq_id,  eq.question as eq_question, eq.options as eq_options, eq.show_if as eq_show_if, eq.show_if_value as eq_show_if_value, "
                + "    eq.question_for as eq_question_for, eq.ordnum as eq_ordnum, eq.school_id as eq_school_id "
                + " FROM EVALUATION_QUESTIONS as eq "
                + " WHERE eq.school_id=? and eq.question_for=? "
                + "   AND NOT EXISTS (select 1 from EVALUATION_ANSWERS as ea where ea.question_id=eq.id)"
                + " ORDER by eq.ordnum asc";
        
        ret=jdbcTemplate.query(
                sql,
                new Object[] {currentUser.getSchool(),forRole}, 
                new EvaluationQuestionsExtractor()
            );
        return ret;
    }
    
    public List<EvaluationQuestion> storeQuestionsForRole(final User currentUser, final List<EvaluationQuestion> questions, final String forRole) {
        
        String sql0 = " DELETE FROM EVALUATION_QUESTIONS WHERE school_id=? and question_for=? and NOT EXISTS (select 1 from EVALUATION_ANSWERS as EA where EA.question_id=EVALUATION_QUESTIONS.id)";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                    public void setValues(PreparedStatement stmt) throws SQLException{
                       stmt.setString(1, currentUser.getSchool()  );
                       stmt.setString(2, forRole  );
                    }
                }
            );
        
        
        String sql1 = " INSERT INTO EVALUATION_QUESTIONS "
                       + " (question, options, show_if, show_if_value,    question_for, ordnum, school_id ) "
                       + " VALUES "
                       + " (   ?,         ?,      ?,          ?,                ?,         ?,       ?     ) "
                       + "  ";
        jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt, int i) throws SQLException {
                    EvaluationQuestion eq=questions.get(i);
                    stmt.setString(1, eq.getQuestion()      );
                    if (eq.getOptions()!=null)       stmt.setString(2, eq.getOptions()       ); else stmt.setNull(2, Types.VARCHAR);
                    if (eq.getShow_if()!=null)       stmt.setInt   (3, eq.getShow_if()       ); else stmt.setNull(3, Types.INTEGER);
                    if (eq.getShow_if_value()!=null) stmt.setString(4, eq.getShow_if_value() ); else stmt.setNull(4, Types.VARCHAR);
                    stmt.setString(5, forRole );
                    if (eq.getOrdnum()!=null)        stmt.setInt   (6, eq.getOrdnum()        );else stmt.setNull(5, Types.INTEGER);
                    stmt.setString(7, currentUser.getSchool()  );
                }

                @Override
                public int getBatchSize() {
                    return questions.size();
                }
            }
        );
        
        List<EvaluationQuestion> new_questions=getQuestionsForRole(currentUser, forRole);
        
        return new_questions;
    }
    
     public List<EvaluationAnswer> getAnswersForInternshipAgreement(final int internship_agreement_id, final User currentUser, final String forRole){
        
        List<EvaluationAnswer> ret=new ArrayList<EvaluationAnswer>();
        
        String sql = 
                " SELECT "
                + "    ea.id as ea_id,  ea.answer as ea_answer, ea.userid as ea_userid, "
                + "    ea.internship_agreement_id as ea_internship_agreement_id, "
                + "    eq.id as eq_id,  eq.question as eq_question, eq.options as eq_options, eq.show_if as eq_show_if, eq.show_if_value as eq_show_if_value, "
                + "    eq.question_for as eq_question_for, eq.ordnum as eq_ordnum, eq.school_id as eq_school_id "
                + "  FROM EVALUATION_QUESTIONS as eq   "
                + "  JOIN EVALUATION_ANSWERS as ea on ea.question_id=eq.id  "
                + "  WHERE ea.internship_agreement_id=? and eq.school_id=? AND eq.question_for=? "
                + " ORDER by eq.ordnum asc";
        
        ret=jdbcTemplate.query(
                sql,
                new Object[] {internship_agreement_id, currentUser.getSchool(), forRole }, 
                new EvaluationAnswersExtractor()
            );
        
        if (ret.size()>0){
            return ret;
        }
        
        
        String sql2 = 
                " SELECT "
                + "    ea.id as ea_id,  ea.answer as ea_answer, ea.userid as ea_userid, "
                + "    ? as ea_internship_agreement_id, "
                + "    eq.id as eq_id,  eq.question as eq_question, eq.options as eq_options, eq.show_if as eq_show_if, eq.show_if_value as eq_show_if_value, "
                + "    eq.question_for as eq_question_for, eq.ordnum as eq_ordnum, eq.school_id as eq_school_id "
                + "  FROM EVALUATION_QUESTIONS as eq   "
                //+ "  LEFT OUTER JOIN EVALUATION_ANSWERS as ea on ea.question_id=eq.id and ea.internship_agreement_id=?  "
                + "  LEFT OUTER JOIN EVALUATION_ANSWERS as ea on ea.question_id=eq.id  "
                + "  WHERE eq.school_id=? AND eq.question_for=? and ea.id is null "
                + " ORDER by eq.ordnum asc";
        
        
        ret=jdbcTemplate.query(
                sql2,
                new Object[] {internship_agreement_id, currentUser.getSchool(), forRole }, 
                new EvaluationAnswersExtractor()
            );
        return ret;
    }
     
    public List<EvaluationAnswer> storeAnswersForInternshipAgreement(final int internship_agreement_id, final User currentUser, final String forRole, List<EvaluationAnswer> answers){
        
        List<EvaluationAnswer> prev=new ArrayList<EvaluationAnswer>();
        
        String sql = 
                " SELECT "
                + "    ea.id as ea_id,  ea.answer as ea_answer, ea.userid as ea_userid, "
                + "    ea.internship_agreement_id as ea_internship_agreement_id, "
                + "    eq.id as eq_id,  eq.question as eq_question, eq.options as eq_options, eq.show_if as eq_show_if, eq.show_if_value as eq_show_if_value, "
                + "    eq.question_for as eq_question_for, eq.ordnum as eq_ordnum, eq.school_id as eq_school_id "
                + "  FROM EVALUATION_QUESTIONS as eq   "
                + "  JOIN EVALUATION_ANSWERS as ea on ea.question_id=eq.id  "
                + "  WHERE ea.internship_agreement_id=? and eq.school_id=? AND eq.question_for=? "
                + " ORDER by eq.ordnum asc";
        
        prev=jdbcTemplate.query(
                sql,
                new Object[] {internship_agreement_id, currentUser.getSchool(), forRole }, 
                new EvaluationAnswersExtractor()
            );
        
        for (int i=0;i<prev.size();i++){
            final EvaluationAnswer ea=prev.get(i);
            if (ea.getId()!=0){
                String sqld = "DELETE EVALUATION_ANSWERS where id=?";
                jdbcTemplate.update(sqld, new PreparedStatementSetter(){
                        public void setValues(PreparedStatement stmt) throws SQLException{
                            stmt.setInt   (1, ea.getId());
                        }
                    }
                );
            }
            if (ea.getQuestion()!=null && ea.getQuestion().getId()!=0){
                String sqld = "DELETE EVALUATION_QUESTIONS where id=?";
                jdbcTemplate.update(sqld, new PreparedStatementSetter(){
                        public void setValues(PreparedStatement stmt) throws SQLException{
                            stmt.setInt   (1, ea.getQuestion().getId());
                        }
                    }
                );
            }
        }
        
        for(int i=0;i<answers.size();i++){
            final EvaluationAnswer ea=answers.get(i);
            
            
            KeyHolder keyHolder=new GeneratedKeyHolder();
            jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        String sql = " INSERT INTO EVALUATION_QUESTIONS (question,options,show_if,show_if_value,question_for,ordnum,school_id) "
                                    + " select eq.question, eq.options, eq.show_if, eq.show_if_value,    eq.question_for, eq.ordnum, eq.school_id "
                                    + " from  EVALUATION_QUESTIONS as eq where eq.id=? and eq.school_id=? "
                                    + "  ";
                        PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"id"} );
                        
                        stmt.setInt(1, ea.getQuestion().getId()   );
                        stmt.setString(2, currentUser.getSchool()  );

                        return stmt;
                    }
                },
                keyHolder);
            final int question_id=keyHolder.getKey().intValue();
            
            
            String sql2 = 
                    " INSERT INTO EVALUATION_ANSWERS "
                   + " (question_id, internship_agreement_id, answer, userid ) "
                   + " VALUES "
                   + " (   ?,                     ?,             ?,      ?) "
                   + "  ";
            jdbcTemplate.update(sql2, new PreparedStatementSetter(){
                    public void setValues(PreparedStatement stmt) throws SQLException{
                       stmt.setInt   (1, question_id      );
                       stmt.setInt   (2, internship_agreement_id );
                       stmt.setString(3, ea.getAnswer() );
                       stmt.setString(4, currentUser.getUsername() );
                    }
                }
            );
        }
            
        return getAnswersForInternshipAgreement(internship_agreement_id, currentUser, forRole);
    }
}

