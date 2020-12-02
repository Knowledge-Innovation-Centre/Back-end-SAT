/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import com.sat.entity.dao.StudyProgramDaoI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author dean
 */
public class UserExtractor implements ResultSetExtractor<User>{
    

    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        String username=null;//=rs.getString("username");
        String password=null;//=rs.getString("password");
        String school=null;
        List<String> roles=new ArrayList<String>();
        //roles.add(rs.getString("role"));
        String phone=null;
        String email=null;
        String cityname=null;
        String cityzipcode=null;
        String streetname=null;
        String first_name=null;
        String last_name=null;
        Integer organization_id=null;
        Integer studyprogram_id=null;
        Boolean jwt=false;
        
        while(rs.next()){
            username=rs.getString("username");
            password=rs.getString("password");
            school=rs.getString("school_id");
            phone=rs.getString("phone");
            email=rs.getString("email");
            cityname=rs.getString("cityname");
            cityzipcode=rs.getString("cityzipcode");
            streetname=rs.getString("streetname");
            organization_id=rs.getInt("organization_id"); if (rs.wasNull()) organization_id=null;
            studyprogram_id=rs.getInt("studyprogram_id"); if (rs.wasNull()) studyprogram_id=null;
            first_name=rs.getString("first_name");
            last_name=rs.getString("last_name");
            jwt=rs.getBoolean("jwt");
            
            roles.add(rs.getString("role"));
        }
        
        if (username==null) return null;
        
        User u=new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setSchool(school);
        u.setRolesFromList(roles);
        
        u.setPhone(phone);
        u.setEmail(email);
        u.setCityname(cityname);
        u.setCityzipcode(cityzipcode);
        u.setStreetname(streetname);
        u.setFirst_name(first_name);
        u.setLast_name(last_name);
        u.setOrganization(organization_id);
        u.setJwt(jwt);
        if (studyprogram_id!=null) {
            
            
            StudyProgram sp=new StudyProgram();
            sp.setId(studyprogram_id.intValue());
            u.setStudyprogram(sp);
        }
        
        return u;
    }
    
}
