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
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author dean
 */
public class UserRowMapper implements RowMapper<User> {
    

    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        String username=rs.getString("username");
        String password=rs.getString("password");
        String school=rs.getString("school_id");
        List<String> roles=new ArrayList<String>();
        //roles.add(rs.getString("role"));
        
        String phone=rs.getString("phone");
        String email=rs.getString("email");
        String cityname=rs.getString("cityname");
        String cityzipcode=rs.getString("cityzipcode");
        String streetname=rs.getString("streetname");
        Integer organization_id=rs.getInt("organization_id"); if (rs.wasNull()) organization_id=null;
        Integer spid=rs.getInt("studyprogram_id"); if (rs.wasNull()){spid=null;} 
        String first_name=rs.getString("first_name");
        String last_name=rs.getString("last_name");
        Boolean jwt=rs.getBoolean("jwt");
        
        do{
            roles.add(rs.getString("role"));
        }while(rs.next());
        
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
        if (spid!=null) {
            
            StudyProgram sp=new StudyProgram();
            sp.setId(spid.intValue());
            u.setStudyprogram(sp);
        }
        
        return u;
    }
    
}
