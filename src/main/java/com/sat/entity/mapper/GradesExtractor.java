/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.Grades;
import com.sat.entity.InternshipAgreement;
import com.sat.entity.Organization;
import com.sat.entity.StudyCompetence;
import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author Dean
 * 
grade, grade_text, mentor_username, "
sc.id as sc_id, sc.name as sc_name, sc.description as sc_description, "
ia.id as ia_id, ia.organization_id as ia_organization_id, ia.student_username as ia_student_username, "
ia.period_begin as ia_period_begin, ia.period_end as ia_period_end, ia.study_program_id as ia_study_program_id
 */
public class GradesExtractor implements ResultSetExtractor<List<Grades>>{

    @Override
    public List<Grades> extractData(ResultSet rs) throws SQLException, DataAccessException {
        
        
        List<Grades> grades=new ArrayList<Grades>();
        Grades g;
        while(rs.next()){
            g=new Grades();
            
            int grade=rs.getInt("grade"); g.setGrade(rs.wasNull()?null:grade);
            g.setGrate_text(rs.getString("grade_text"));
            User mentor=new User();
            mentor.setUsername(rs.getString("mentor_username"));
            g.setMentor(mentor);
            
            StudyCompetence sc=new StudyCompetence();
            sc.setId(rs.getInt("sc_id"));
            sc.setName(rs.getString("sc_name"));
            sc.setDescription(rs.getString("sc_description"));
            g.setStudyCompetence(sc);
            
            InternshipAgreement ia=new InternshipAgreement();
            ia.setId(rs.getInt("ia_id"));
            Organization o=new Organization();o.setId(rs.getInt("ia_organization_id"));
            ia.setOrganization(o);
            User s=new User();s.setUsername(rs.getString("ia_student_username"));
            ia.setStudent(s);
            ia.setPeriodBegin(rs.getDate("ia_period_begin"));
            ia.setPeriodEnd(rs.getDate("ia_period_end"));
            StudyProgram sp=new StudyProgram();sp.setId(rs.getInt("ia_study_program_id"));
            ia.setStudyProgram(sp);
            g.setInternshipAgreement(ia);
              
            grades.add(g);
        }
        
        return grades;
    }
    
}
