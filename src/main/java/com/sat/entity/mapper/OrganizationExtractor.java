/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.Organization;
import com.sat.entity.StudyCompetence;
import com.sat.entity.StudyModule;
import com.sat.entity.StudyProgram;
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
 * @author Dean
 */
public class OrganizationExtractor implements ResultSetExtractor<Organization> {

    @Override
    public Organization extractData(ResultSet rs) throws SQLException, DataAccessException {
        Organization o = null;
        Integer key;
        String contact_username;
        Integer sp_id=null;
        
        //for (Map row : rows){
        while (rs.next()){

            if (o==null){
                o=new Organization();
                o.setId(rs.getInt("id"));
            
                // id,orgtype,name,cityname,cityzipcode,streetname,contact_username,school_id
                o.setOrgtype(rs.getString("orgtype"));
                o.setName(rs.getString("name"));
                o.setCityname(rs.getString("cityname"));
                o.setCityzipcode(rs.getString("cityzipcode"));
                o.setStreetname(rs.getString("streetname"));
                contact_username=rs.getString("contact_username");if (rs.wasNull()) contact_username=null;
                o.setContact_username(contact_username);
                o.setSchool_id(rs.getString("school_id"));
                o.setStudyPrograms(new ArrayList<StudyProgram>());
            }
            sp_id=rs.getInt("sp_id");if (rs.wasNull()) sp_id=null;
            if (sp_id!=null){
                StudyProgram sp=new StudyProgram();
                sp.setId(sp_id);
                sp.setName(rs.getString("sp_name"));
                sp.setDescription(rs.getString("sp_description"));
                sp.setStudyCompetences(new ArrayList<StudyCompetence>());
                sp.setStudyModules(new ArrayList<StudyModule>());
                o.getStudyPrograms().add(sp);
            }
        }
            
        return o;
    }
    
}
