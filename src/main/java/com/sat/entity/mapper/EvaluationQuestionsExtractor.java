/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.EvaluationQuestion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author dean
 */
public class EvaluationQuestionsExtractor  implements ResultSetExtractor<List<EvaluationQuestion>> {

    @Override
    public List<EvaluationQuestion> extractData(ResultSet rs) throws SQLException, DataAccessException {
        
        List<EvaluationQuestion> ret = new ArrayList<EvaluationQuestion>();
        
        while (rs.next()){
            EvaluationQuestion q=new EvaluationQuestion();
            q.setId(rs.getInt("eq_id"));
            q.setQuestion(rs.getString("eq_question"));
            q.setOptions(rs.getString("eq_options"));
            q.setShow_if(rs.getInt("eq_show_if"));             if (rs.wasNull()) q.setShow_if(null);
            q.setShow_if_value(rs.getString("eq_show_if_value")); if (rs.wasNull()) q.setShow_if_value(null);
            q.setOrdnum(rs.getInt("eq_ordnum"));               if (rs.wasNull()) q.setOrdnum(null);
            q.setSchool_id(rs.getString("eq_school_id"));
            q.setQuestion_for(rs.getString("eq_question_for"));
            ret.add(q);
        }
        
        return ret;
    }
    /*
        eq.id as eq_id,  eq.question as eq_question, eq.options as eq_options, eq.show_if as eq_show_if, eq.show_if_value as eq_show_if_value, "
        eq.question_for as eq_question_for, eq.ordnum as eq_ordnum, eq.school_id as eq_school_id "
    private int id;
    private String question;
    private String options;
    private Integer show_if;
    private String show_if_value;
    private Integer ordnum;
    
    private String school_id;
    */
    
}
