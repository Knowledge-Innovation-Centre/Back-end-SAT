/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.Assignment;
import com.sat.entity.InternshipAgreement;
import com.sat.entity.StudentWorklog;
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
public class StudentWorklogExtractor implements ResultSetExtractor<List<StudentWorklog>>  {

    @Override
    public List<StudentWorklog> extractData(ResultSet rs) throws SQLException, DataAccessException {
        
        List<StudentWorklog> ret=new ArrayList<StudentWorklog>();
                
        StudentWorklog sw;
        while (rs.next()){
            int sw_id=rs.getInt("sw_id");
            if (!rs.wasNull()){
                sw=new StudentWorklog();
                sw.setId(sw_id);
                sw.setStudent_log(rs.getString("student_log"));
                sw.setDate_studentlog(rs.getDate("date_studentlog"));
                sw.setMentor_log(rs.getString("mentor_log"));
                sw.setDate_mentorlog(rs.getDate("date_mentorlog"));
                ret.add(sw);
            }
        }
        
        return ret;
    }
    
}
