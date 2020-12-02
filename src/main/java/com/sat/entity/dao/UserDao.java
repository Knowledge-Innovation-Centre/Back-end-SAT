/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import com.sat.entity.mapper.UserExtractor;
import com.sat.entity.mapper.UserRowMapper;
import com.sat.entity.mapper.UsersExtractor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/*
    CREATE TABLE `USERS` (
      `username` varchar(64) CHARACTER SET utf8 NOT NULL,
      `password` varchar(64) CHARACTER SET utf8 NOT NULL,
      `school_id` varchar(64) CHARACTER SET utf8 NOT NULL,

      `phone` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
      `email` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
      `cityname` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
      `cityzipcode` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
      `streetname` varchar(256) CHARACTER SET utf8 DEFAULT NULL,
      `contact_username` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
      PRIMARY KEY (`username`),
      KEY `USERS_SCHOOLS_FK` (`school_id`),
      CONSTRAINT `USERS_SCHOOLS_FK` FOREIGN KEY (`school_id`) REFERENCES `SCHOOLS` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci;
*/

/**
 *
 * @author dean
 */
@Repository
//@Qualifier("userDao")
public class UserDao implements UserDaoI {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    private StudyProgramDaoI studyProgramService;
    
    
    User eagerLoadStudyProgramData(User u){
        if (u==null) return null;
        if (u.getStudyprogram()==null) return u;
        
        int spid=u.getStudyprogram().getId();
        StudyProgram sp=studyProgramService.find(spid, u);
        
        u.setStudyprogram(sp);
        
        return u;
    }
    
    List<User> eagerLoadStudyProgramData(List<User> users){
        
        List<User> ret=new ArrayList<User>();
        
        for(int i=0;i<users.size();i++){
            ret.add(eagerLoadStudyProgramData(users.get(i)));
        }
        
        return ret;
    }
        
    @Override
    public void addUser(final User user) {
        String sql1 = "INSERT into USERS (username,password,school_id, phone,email,cityname,cityzipcode,streetname,organization_id, studyprogram_id, first_name, last_name, jwt) values(?,?,?, ?,?,?, ?,?,?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1,user.getUsername());
                    stmt.setString(2,user.getPassword());
                    stmt.setString(3,user.getSchool());
                    if (user.getPhone()!=null) stmt.setString(4,user.getPhone()); else stmt.setNull(4, Types.VARCHAR);
                    if (user.getEmail()!=null) stmt.setString(5,user.getEmail()); else stmt.setNull(5, Types.VARCHAR);
                    if (user.getCityname()!=null) stmt.setString(6,user.getCityname()); else stmt.setNull(6, Types.VARCHAR);
                    if (user.getCityzipcode()!=null) stmt.setString(7,user.getCityzipcode()); else stmt.setNull(7, Types.VARCHAR);
                    if (user.getStreetname()!=null) stmt.setString(8,user.getStreetname()); else stmt.setNull(8, Types.VARCHAR);
                    if (user.getOrganization()!=null) stmt.setInt(9,user.getOrganization()); else stmt.setNull(9, Types.INTEGER);
                    if (user.getStudyprogram()!=null) stmt.setInt(10,user.getStudyprogram().getId()); else stmt.setNull(10, Types.INTEGER);
                    if (user.getFirst_name()!=null) stmt.setString(11,user.getFirst_name()); else stmt.setNull(11, Types.VARCHAR);
                    if (user.getLast_name()!=null) stmt.setString(12,user.getLast_name()); else stmt.setNull(12, Types.VARCHAR);
                    if (user.getJwt()!=null) stmt.setBoolean(13,user.getJwt()); else stmt.setNull(13, Types.BOOLEAN);
                }
            }
        );
        
        jdbcTemplate.batchUpdate("INSERT into USER_PERMISSIONS (username,role) values(?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, user.getUsername());
                        ps.setString(2, user.getRoles()[i]);
                    }
                    @Override
                    public int getBatchSize() {
                        return user.getRoles().length;
                    }
                }
            );       
    }

    @Override
    public void editUser(final User user, final String oldUsername) {
        
        
        String sql1 = "UPDATE USERS set username=IFNULL(?,username), password=IFNULL(?,password), "+
                      " phone=IFNULL(?,phone), "+
                      " email=IFNULL(?,email), "+
                      " cityname=IFNULL(?,cityname), "+
                      " cityzipcode=IFNULL(?,cityzipcode), "+
                      " streetname=IFNULL(?,streetname), "+
                      " organization_id=IFNULL(?,organization_id), "+
                      " studyprogram_id=IFNULL(?,studyprogram_id), "+
                      " first_name=IFNULL(?,first_name), "+
                      " last_name=IFNULL(?,last_name), "+
                      " jwt=IFNULL(?,jwt) "+
                      " where username=?";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1,user.getUsername());
                    stmt.setString(2,user.getPassword());
                                        
                    if (user.getPhone()!=null) stmt.setString(3,user.getPhone()); else stmt.setNull(3, Types.VARCHAR);
                    if (user.getEmail()!=null) stmt.setString(4,user.getEmail()); else stmt.setNull(4, Types.VARCHAR);
                    if (user.getCityname()!=null) stmt.setString(5,user.getCityname()); else stmt.setNull(5, Types.VARCHAR);
                    if (user.getCityzipcode()!=null) stmt.setString(6,user.getCityzipcode()); else stmt.setNull(6, Types.VARCHAR);
                    if (user.getStreetname()!=null) stmt.setString(7,user.getStreetname()); else stmt.setNull(7, Types.VARCHAR);
                    if (user.getOrganization()!=null) stmt.setInt(8,user.getOrganization()); else stmt.setNull(8, Types.INTEGER);
                    if (user.getStudyprogram()!=null) stmt.setInt(9,user.getStudyprogram().getId()); else stmt.setNull(9, Types.INTEGER);
                    if (user.getFirst_name()!=null) stmt.setString(10,user.getFirst_name()); else stmt.setNull(10, Types.VARCHAR);
                    if (user.getLast_name()!=null) stmt.setString(11,user.getLast_name()); else stmt.setNull(11, Types.VARCHAR);
                    if (user.getJwt()!=null) stmt.setBoolean(12,user.getJwt()); else stmt.setNull(12, Types.BOOLEAN);
                    
                    stmt.setString(13,oldUsername);
                }
            }
        );
        
        if (user.getRoles()!=null){
            if (user.getRoles().length>0){
                String sql2 = "DELETE from USER_PERMISSIONS where username=?";
                jdbcTemplate.update(sql2, new PreparedStatementSetter(){
                        public void setValues(PreparedStatement stmt) throws SQLException{
                            stmt.setString(1,oldUsername);
                        }
                    }
                );

                jdbcTemplate.batchUpdate("INSERT into USER_PERMISSIONS (username,role) values(?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, user.getUsername());
                            ps.setString(2, user.getRoles()[i]);
                        }
                        @Override
                        public int getBatchSize() {
                            return user.getRoles().length;
                        }
                    }
                );  
            }
        }
    }

    @Override
    public void deleteUser(final String username) {
        String sql1 = "DELETE from USER_PERMISSIONS where username=?";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1,username);
                }
            }
        );
        String sql2 = "DELETE from USERS where username=? ";
        jdbcTemplate.update(sql2, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1,username);
                }
            }
        );
    }

    /*@Override
    public User find(String username) {
        User user=null;
        try{
            user = (User) jdbcTemplate.queryForObject(
                "SELECT u.username,u.password,up.\"ROLE\" from USERS u join USER_PERMISSIONS up on (up.USERNAME=u.username) where u.username = ? ORDER BY u.username",
                new Object[] { username }, 
                new UserRowMapper()
            );
        }
        catch(Exception e){
            return null;
        }
        
        return user;
    }*/
    @Override
    public User find(String username) {
        User user=null;

        user = (User) jdbcTemplate.query(
            "SELECT u.username,u.password,u.school_id,up.ROLE, "+
            " u.phone,u.email,u.cityname,u.cityzipcode,u.streetname, u.organization_id, u.studyprogram_id, u.first_name, u.last_name, u.jwt "+
            " from USERS u join USER_PERMISSIONS up on (up.USERNAME=u.username) where u.username = ? ORDER BY u.username",
            new Object[] { username }, 
            new UserExtractor()
        );

        
        return eagerLoadStudyProgramData(user);
    }
    
    @Override
    public List<User> findAll() {
        
        //List<User> usrs=null;
        List<User> usrs=new ArrayList<User>();
        
        String sql = "SELECT u.username,u.password,u.school_id,up.ROLE, "+
                " u.phone,u.email,u.cityname,u.cityzipcode,u.streetname, u.organization_id, u.studyprogram_id, u.first_name, u.last_name, u.jwt "+
                "from USERS u join USER_PERMISSIONS up on (up.USERNAME=u.username) ORDER BY u.username";
        usrs=jdbcTemplate.query(
                sql,
                new Object[] {}, 
                new UsersExtractor()
        );
            
        return eagerLoadStudyProgramData(usrs);
    }

    /*
    @Override
    public List<User> findAll() {
        String sql = "SELECT u.username,u.password,up.\"ROLE\" from USERS u join USER_PERMISSIONS up on (up.USERNAME=u.username)";
        
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        User u = null;
        String key;
        String[] roles;
        
        Hashtable<String, User> users = new Hashtable<String, User>();
        
        for (Map row : rows){
            key=(String)row.get("username");
            u=users.get(key);
            if (u==null){
                u=new User();
                u.setUsername(key);
                u.setPassword((String)row.get("password"));
                roles=new String[1];
                roles[0]=(String)row.get("role");
                u.setRoles(roles);
                users.put(key,u);
            }else{
                roles=u.getRoles();
                String[] roles2=new String[roles.length+1];
                for(int i=0;i<roles.length;i++) roles2[i]=roles[i];
                roles2[roles.length]=(String)row.get("role");
                u.setRoles(roles2);
                users.put(key,u);
            }
        }
        List<User> usrs=new ArrayList<User>();
        Enumeration enu=users.elements();
        while(enu.hasMoreElements()){
            usrs.add((User)enu.nextElement());
        }
            
        return usrs;
    }
    
    */
    
    /*public List<User> xfindAll() {
        String sql = "SELECT u.username,u.password,up.\"ROLE\" from USERS u join USER_PERMISSIONS up on (up.USERNAME=u.username) ORDER BY u.username";
		 
	List<User> users = new ArrayList<User>();
	
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        User u = null;
        List<String> roles;
        boolean isNew=true;
        u=new User();
        roles=new ArrayList<String>();
        
	for (Map row : rows) {
            if (!isNew){
                if(u.getUsername().equals(  (String)row.get("username")   )  ){
                    roles.add((String)row.get("role"));
                }
                else{
                    u.setRolesFromList(roles);
                    users.add(u);
                    u=new User();
                    isNew=true;
                    roles=new ArrayList<String>();
                    roles.add((String)row.get("role"));
                }
            }
            if (isNew){
                u.setUsername((String)row.get("username"));
                u.setPassword((String)row.get("password"));
                roles.add((String)row.get("role"));
                isNew=false;
            }		
	}
	if (roles.size()>0){
            u.setRolesFromList(roles);
            users.add(u);
        }	
	return users;
    }*/

    @Override
    public List<User> findUsersWithinOrganization(User currentUser) {
        //List<User> usrs=null;
        List<User> usrs=new ArrayList<User>();
        
        String sql = " SELECT u.username,u.password,u.school_id,up.ROLE, "+
                     " u.phone,u.email,u.cityname,u.cityzipcode,u.streetname, u.organization_id, u.studyprogram_id, u.first_name, u.last_name, u.jwt "+
                     " from USERS u join USER_PERMISSIONS up on (up.USERNAME=u.username)"
                + "    WHERE u.organization_id=? "
                + "     ORDER BY u.username";
        usrs=jdbcTemplate.query(
                sql,
                new Object[] {currentUser.getOrganization()}, 
                new UsersExtractor()
        );
            
        return eagerLoadStudyProgramData(usrs);
    }
    @Override
    public List<User> findUsersWithinSchool(User currentUser) {
        //List<User> usrs=null;
        List<User> usrs=new ArrayList<User>();
        
        String sql = " SELECT u.username,u.password,u.school_id,up.ROLE, "+
                     " u.phone,u.email,u.cityname,u.cityzipcode,u.streetname, u.organization_id, u.studyprogram_id, u.first_name, u.last_name, u.jwt "+
                     " from USERS u join USER_PERMISSIONS up on (up.USERNAME=u.username)"
                + "    WHERE u.school_id=? "
                + "     ORDER BY u.username";
        usrs=jdbcTemplate.query(
                sql,
                new Object[] {currentUser.getSchool()}, 
                new UsersExtractor()
        );
            
        return eagerLoadStudyProgramData(usrs);
    }
    
}
