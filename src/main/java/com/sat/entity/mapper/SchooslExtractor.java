/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.School;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author Dean
 */
public class SchooslExtractor implements ResultSetExtractor<List<School>>  {

    @Override
    public List<School> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<School> schools=new ArrayList<School>();
        
        String school_id;
        String public_key;
        String public_id;
        while (rs.next()){
            school_id=rs.getString("id");
            public_key=rs.getString("public_key");
            public_id=rs.getString("public_identifier");
            School s=new School();
            s.setSchool_id(school_id);
            s.setPublic_key(public_key);
            s.setPublic_identifier(public_id);
            schools.add(s);
        }
        return schools;
    }
    
}
