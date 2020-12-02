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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author dean
 */
public class UsersExtractor implements ResultSetExtractor<List<User>> {
    
    

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        
        User u = null;
        String key;
        //String[] roles;
        List<String> roles=new ArrayList<String>();
        Integer organization_id=null;
        
        Hashtable<String, User> users = new Hashtable<String, User>();
        
        //for (Map row : rows){
        while (rs.next()){
            //key=(String)row.get("username");
            key=rs.getString("username");
            u=users.get(key);
            if (u==null){
                
                
                u=new User();
                u.setUsername(key);
                //u.setPassword((String)row.get("password"));
                u.setPassword(rs.getString("password"));
                u.setSchool(rs.getString("school_id"));
                roles=new ArrayList<String>();
                //roles[0]=rs.getString("role");
                roles.add(rs.getString("role"));
                u.setRolesFromList(roles);
                
                
                u.setPhone(rs.getString("phone"));
                u.setEmail(rs.getString("email"));
                u.setCityname(rs.getString("cityname"));
                u.setCityzipcode(rs.getString("cityzipcode"));
                u.setStreetname(rs.getString("streetname"));
                
                u.setFirst_name(rs.getString("first_name"));
                u.setLast_name(rs.getString("last_name"));
                
                u.setJwt(rs.getBoolean("jwt"));
                
                organization_id=rs.getInt("organization_id");
                if (rs.wasNull()) organization_id=null;
                u.setOrganization(organization_id);
                
                int spid=rs.getInt("studyprogram_id");
                if (!rs.wasNull()){
                    
                    /*StudyProgram sp=studyProgramService.find(spid, u);
                    u.setStudyprogram(sp);*/
                    
                    StudyProgram sp=new StudyProgram();
                    sp.setId(spid);
                    u.setStudyprogram(sp);
                }

                
                users.put(key,u);
            }else{
                roles=u.getRolesAsList();
                //String[] roles2=new String[roles.length+1];
                //for(int i=0;i<roles.length;i++) roles2[i]=roles[i];
                //roles2[roles.length]=rs.getString("role");
                roles.add(rs.getString("role"));
                u.setRolesFromList(roles);
                users.put(key,u);
            }
        }
        List<User> usrs=new ArrayList<User>();
        Enumeration enu=users.elements();
        while(enu.hasMoreElements()){
            User uu=(User)enu.nextElement();
            /*StudyProgram sprg=uu.getStudyprogram();
            if (sprg!=null && sprg.getId()!=0){
                StudyProgram sprg2=studyProgramService.find(sprg.getId(), uu);
                uu.setStudyprogram(sprg2);
            }*/
            usrs.add(uu);
        }
            
        return usrs;
    }
    
}
