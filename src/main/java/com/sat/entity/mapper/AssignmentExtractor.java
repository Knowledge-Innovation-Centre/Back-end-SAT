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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author dean
 */
public class AssignmentExtractor implements ResultSetExtractor<List<Assignment>>  {

    @Override
    public List<Assignment> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Hashtable<Integer, Assignment> map = new Hashtable<Integer, Assignment>();
        Assignment a;
        List<StudentWorklog> swl;
        StudentWorklog sw;
        
        while (rs.next()){
            int id=rs.getInt("a_id");
            a=map.get(id);
            if (a==null){
                a=new Assignment();
                a.setId(id);
                a.setDescription(rs.getString("a_description"));
                a.setDeadline(rs.getDate("a_deadline"));
                int ia_id=rs.getInt("a_internship_agreement_id");
                if (!rs.wasNull()){
                    InternshipAgreement ia=new InternshipAgreement();
                    ia.setId(ia_id);
                    a.setInternship_agreement(ia);
                }
                swl=new ArrayList<StudentWorklog>();
                a.setStudent_worklog(swl);
            }
            int sw_id=rs.getInt("sw_id");
            if (!rs.wasNull()){
                swl=a.getStudent_worklog();
                sw=new StudentWorklog();
                sw.setId(sw_id);
                sw.setStudent_log(rs.getString("student_log"));
                sw.setDate_studentlog(rs.getDate("date_studentlog"));
                sw.setMentor_log(rs.getString("mentor_log"));
                sw.setDate_mentorlog(rs.getDate("date_mentorlog"));
                swl.add(sw);
                a.setStudent_worklog(swl);
            }
            map.put(id, a);
        }
        
        List<Assignment> ret=new ArrayList<Assignment>();
        Enumeration enu=map.elements();
        while(enu.hasMoreElements()){
            ret.add((Assignment)enu.nextElement());
        }
        return ret;
    }
    
}
