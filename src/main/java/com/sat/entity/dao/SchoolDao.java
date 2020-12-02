/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.mapper.SchooslExtractor;
import com.sat.entity.School;
import com.sat.entity.User;
import com.sat.entity.mapper.SchoolRowMapper;
import com.sat.entity.mapper.UserExtractor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dean
 */
@Repository
public class SchoolDao implements SchoolDaoI{

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public void addSchool(final School school) {
        String sql1 = "INSERT into SCHOOLS (id,public_key, public_identifier) values(?,?,?)";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1,school.getSchool_id());
                    if (school.getPublic_key()!=null) stmt.setString(2,school.getPublic_key()); else stmt.setNull(2,Types.VARCHAR);
                    if (school.getPublic_identifier()!=null) stmt.setString(3,school.getPublic_identifier()); else stmt.setNull(3,Types.VARCHAR);
                }
            }
        );
    }

    @Override
    public void editSchool(final School school, final String new_school_id) {
        
        if (!new_school_id.equalsIgnoreCase(school.getSchool_id())){
            School existingSchool=find(new_school_id);
            if (existingSchool!=null) throw new IllegalArgumentException("School with id="+new_school_id+" already exists!");
        }
        String sql1 = "UPDATE SCHOOLS set id=?, public_key=?, public_identifier=? "+
                      " where id=?";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1,new_school_id);
                    if (school.getPublic_key()!=null) stmt.setString(2,school.getPublic_key()); else stmt.setNull(2,Types.VARCHAR);
                    if (school.getPublic_identifier()!=null) stmt.setString(3,school.getPublic_identifier()); else stmt.setNull(3,Types.VARCHAR);
                    stmt.setString(4,school.getSchool_id());
                }
            }
        );
    }

    @Override
    public void deleteSchool(final String school_id) {
        String sql2 = "DELETE from SCHOOLS where id=?";
        jdbcTemplate.update(sql2, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1,school_id);
                }
            }
        );
    }

    @Override
    public School find(String school_id) {
        
        
        List<School> schools=new ArrayList<School>();
        
        String sql = " SELECT s.id, s.public_key, s.public_identifier  "+
                     " from SCHOOLS s where s.id = ? ";
        schools=jdbcTemplate.query(sql,
                new Object[] {school_id}, 
                new SchooslExtractor()
        );
        if (schools.size()>0) return schools.get(0);
        return null;
        
        

        /*
        School school=null;
        
        try{
            school = (School) jdbcTemplate.query(
                "SELECT s.id, s.public_key "+
                " from SCHOOLS as s where s.id = ?",
                new Object[] { school_id }, 
                new SchoolRowMapper()
            );
        } catch (Exception e){
            return null;
        }

        return school;*/
    }

    @Override
    public List<School> findAll() {
        List<School> schools=new ArrayList<School>();
        
        String sql = " SELECT s.id, s.public_key, s.public_identifier  "+
                     " from SCHOOLS s ";
        schools=jdbcTemplate.query(sql,
                new Object[] {}, 
                new SchooslExtractor()
        );
            
        return schools;
    }
    
}
