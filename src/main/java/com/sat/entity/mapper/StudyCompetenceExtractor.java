/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.StudyCompetence;
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
public class StudyCompetenceExtractor implements ResultSetExtractor<List<StudyCompetence>> {

    @Override
    public List<StudyCompetence> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<StudyCompetence> ret=new ArrayList<StudyCompetence>();
        
        StudyCompetence sc;
        while (rs.next()){
            sc=new StudyCompetence();
            sc.setId(rs.getInt("id"));
            sc.setName(rs.getString("name"));
            sc.setDescription(rs.getString("description"));
            ret.add(sc);
        }
        
        return ret;
    }
    
}
