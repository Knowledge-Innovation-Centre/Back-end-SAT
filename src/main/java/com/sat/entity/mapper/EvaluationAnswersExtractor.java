/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.EvaluationAnswer;
import com.sat.entity.EvaluationQuestion;
import com.sat.entity.InternshipAgreement;
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
public class EvaluationAnswersExtractor  implements ResultSetExtractor<List<EvaluationAnswer>> {

    @Override
    public List<EvaluationAnswer> extractData(ResultSet rs) throws SQLException, DataAccessException {
        
        List<EvaluationAnswer> ret = new ArrayList<EvaluationAnswer>();
        
        while (rs.next()){
            // ea.answer as ea_answer, ea.userid as ea_userid,
            EvaluationAnswer a=new EvaluationAnswer();
            a.setId(rs.getInt("ea_id"));                       if (rs.wasNull()) a.setId(null);
            a.setAnswer(rs.getString("ea_answer"));            if (rs.wasNull()) a.setAnswer(null);
            a.setUserid(rs.getString("ea_userid"));            if (rs.wasNull()) a.setUserid(null);
            
            // ea_internship_agreement_id
            InternshipAgreement ia=new InternshipAgreement();
            ia.setId(rs.getInt("ea_internship_agreement_id"));
            if (!rs.wasNull())  a.setInternship_agreement(ia); 

           
            EvaluationQuestion q=new EvaluationQuestion();
            q.setId(rs.getInt("eq_id"));
            q.setQuestion(rs.getString("eq_question"));
            q.setOptions(rs.getString("eq_options"));
            q.setShow_if(rs.getInt("eq_show_if"));             if (rs.wasNull()) q.setShow_if(null);
            q.setShow_if_value(rs.getString("eq_show_if_value")); if (rs.wasNull()) q.setShow_if_value(null);
            q.setOrdnum(rs.getInt("eq_ordnum"));               if (rs.wasNull()) q.setOrdnum(null);
            q.setSchool_id(rs.getString("eq_school_id"));
            q.setQuestion_for(rs.getString("eq_question_for"));
            a.setQuestion(q);
            ret.add(a);
        }
        
        return ret;
    }
}
