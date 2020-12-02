/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.School;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Dean
 */
public class SchoolRowMapper implements RowMapper<School> {

    @Override
    public School mapRow(ResultSet rs, int i) throws SQLException {
        String school_id=rs.getString("id");
        String public_key=rs.getString("public_key");
        String public_id=rs.getString("public_identifier");
        School school=new School();
        school.setSchool_id(school_id);
        school.setPublic_key(public_key);
        school.setPublic_identifier(public_id);
        return school;
    }
    
}
