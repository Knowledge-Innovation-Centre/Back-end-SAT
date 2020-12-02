/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.Greeting;
import com.sat.entity.School;
import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import com.sat.entity.dao.SchoolDao;
import com.sat.entity.dao.StudyProgramDaoI;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sat.entity.servicebeans.UserServiceI;
import com.sat.security.CustomAuthenticationProvider;
import com.sat.security.PKCSConverter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author dean
 */
@Controller
//@RequestMapping(value = "/admin")
public class SecurityController {
    
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private UserServiceI userService;
    
    @Autowired
    private SchoolDao schoolService;
    
    @Autowired
    private StudyProgramDaoI studyProgramService;
    
    @RequestMapping(value = "/loginJWT/{jwttoken}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User loginJWTget(HttpServletRequest req, HttpServletResponse res, @PathVariable String jwttoken) throws IOException{
        return loginJWT_generic(req,res,jwttoken);
    }
    @RequestMapping(value = "/loginJWT", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User loginJWT(HttpServletRequest req, HttpServletResponse res, @RequestParam(value = "User", required = false) String jwttoken ) throws IOException{
        return loginJWT_generic(req,res,jwttoken);
    }
    @RequestMapping(value = "/loginJWT", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User loginJWT(HttpServletRequest req, HttpServletResponse res ) throws IOException{
        return loginJWT_generic(req,res,null);
    }
    
    
    
    //@RequestMapping(value = "/loginJWT", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json; charset=utf-8")
    //@ResponseBody
    //public User loginJWT(HttpServletRequest req, HttpServletResponse res) throws IOException{
    //public User loginJWT(HttpServletRequest req, HttpServletResponse res, @RequestParam(value = "User", required = false) String jwttoken ) throws IOException{
  
    private User loginJWT_generic(HttpServletRequest req, HttpServletResponse res, String jwttoken) throws IOException{
        
        String token=jwttoken;
        if (token==null){
            token=req.getHeader("Authorization");
            if (token==null) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN , "Missing Authorization header!");
                return null;
            }
            if (!token.toLowerCase().startsWith("bearer ")){
                res.sendError(HttpServletResponse.SC_FORBIDDEN , "Missing Baerer in authorization header!");
                return null;
            }
            token=token.substring(7);
        }
        
        SigningKeyResolver signingKeyResolver = new MySigningKeyResolver();
        
        Jws<Claims> jws;
        try{
        
            jws=Jwts.parser()
                .setSigningKeyResolver(signingKeyResolver)
                .setAllowedClockSkewSeconds(60*60*5)
                .parseClaimsJws(token);
        } catch (Exception e){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT token invalid!"+e.getMessage() );
            return null;
        }
        //jws.getBody().get(token, type)
        String school_id=jws.getBody().get("school", String.class);
        String user_id=jws.getBody().get("sub", String.class);
        Integer irole=jws.getBody().get("role", Integer.class);
        String first_name=jws.getBody().get("firstname", String.class);
        String last_name=jws.getBody().get("familyname", String.class);
        String streetname=jws.getBody().get("streetname", String.class);
        String cityname=jws.getBody().get("cityname", String.class);
        String cityzipcode=jws.getBody().get("cityzipcode", String.class);
        String phone=jws.getBody().get("phone", String.class);
        String email=jws.getBody().get("email", String.class);    
        String study_program=jws.getBody().get("study_program", String.class);    
                
                
        String role="";
        if (irole==1) role="ROLE_STUDENT";
        if (irole==2) role="ROLE_SCHOOLORG";
        String claim_id=jws.getBody().get("jti", String.class); // TODO: should be checked in order to ensure one claim is used only once!
        Long expiration=jws.getBody().get("exp", Long.class);
        
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long time=calendar.getTimeInMillis() / 1000L;
        if (expiration.longValue()<=time-90l*60l*3){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Claim expired!" );
            return null;
        }
        
        // TODO: Check if claim_id is not "recycled"!!!
        String hashedPwd=CustomAuthenticationProvider.hashPassword( claim_id ); // koristimo claim_id kao password za authentikaciju
        
       /* User user=userService.find(user_id);
        if (user!=null){
            // dohvatili smo ga iz baze, ali taj iz baze ne smije imati claim_id (kao hashed password) - jer bi to značilo da je već korišten
            if (hashedPwd.equals(user.getPassword())){
         */
        
        // store as new User
        User user=new User();
        user.setPassword(hashedPwd);
        user.setUsername(user_id);
        School sch=schoolService.find(school_id);
        if (sch==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Claim has invalid school!" );
            return null;
        }
        user.setSchool(school_id);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setStreetname(streetname);
        user.setCityname(cityname);
        user.setCityzipcode(cityzipcode);
        user.setPhone(phone);
        user.setEmail(email);
        user.setJwt(true);

        StudyProgram sp=null;
        if (study_program!=null && !"".equals(study_program)){
            List<StudyProgram> sps=studyProgramService.findByIdnumber(school_id, study_program);
            if (sps!=null && sps.size()>0) {
                sp=sps.get(0);
            }else{
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Claim has invalid study program!" );
                return null;
            }
            user.setStudyprogram(sp);
        }

        String roles[]=new String[1];roles[0]=role;
        user.setRoles(roles);
        
        user.setPassword(hashedPwd);
        
        User userExt=userService.find(user_id);
        if (userExt==null){
            userService.addUser(user);
        } else {
            if (irole==2) user.setRoles(userExt.getRoles()); // from non-student users we keep permissions/roles trough sessions
            userService.editUser(user, user.getUsername());
        }
        
        /*if (!user.getSchool().equals(school_id)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Claim has invalid school!" );
            return null;
        }
        if (!user.getRoles()[0].equals(role)){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Claim has invalid role!" );
            return null;
        }*/
        
        user.setPassword(hashedPwd); // ovo moramo napraviti (spremiti u bazu sa novim passwordom) kako bi AuthManager prilikom authentikacije iz baze dohvatio password i prepoznao ga kao ispravana
        //userService.editUser(user, user.getUsername());
        
        UsernamePasswordAuthenticationToken authReq  = new UsernamePasswordAuthenticationToken(user_id, hashedPwd);
        Authentication auth = authenticationManager.authenticate(authReq);
        if (auth.isAuthenticated()){
            SecurityContext sc = SecurityContextHolder.getContext();
        
            sc.setAuthentication(auth);

            HttpSession session = req.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

            //res.sendRedirect("/");
            return user;
        }
        else{
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
    }
    
    public class MySigningKeyResolver extends SigningKeyResolverAdapter {
        @Override
        public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
            
            String school_id=claims.get("school",String.class);
            if (school_id==null) return null;
            School s=schoolService.find(school_id);
            if (s==null) return null;        
            String public_key=s.getPublic_key();
            public_key=public_key.replace("-----BEGIN PUBLIC KEY-----", "");
            public_key=public_key.replace("-----END PRIVATE KEY-----", "");
            public_key=public_key.replace("-----BEGIN RSA PUBLIC KEY-----", "");
            public_key=public_key.replace("-----END RSA PUBLIC KEY-----", "");
            
            public_key=public_key.replace("\r\n", "");
            public_key=public_key.replace("\n", "");
            //public_key="MIIBCgKCAQEA33TqqLR3eeUmDtHS89qF3p4MP7Wfqt2Zjj3lZjLjjCGDvwr9cJNlNDiuKboODgUiT4ZdPWbOiMAfDcDzlOxA04DDnEFGAf+kDQiNSe2ZtqC7bnIc8+KSG/qOGQIVaay4Ucr6ovDkykO5Hxn7OU7sJp9TP9H0JH8zMQA6YzijYH9LsupTerrY3U6zyihVEDXXOv08vBHk50BMFJbE9iwFwnxCsU5+UZUZYw87Uu0n4LPFS9BT8tUIvAfnRXIEWCha3KbFWmdZQZlyrFw0buUEf0YN3/Q0auBkdbDR/ES2PbgKTJdkjc/rEeM0TxvOUf7HuUNOhrtAVEN1D5uuxE1WSwIDAQAB";

            
            //byte[] byteKey = Base64.decode(public_key.getBytes(), Base64.DEFAULT);
            try {
                byte[] byteKey = Base64.getDecoder().decode(public_key.getBytes());
                RSAPublicKey generatePublic = PKCSConverter.decodePKCS1PublicKey(byteKey);
                return generatePublic;
                
                /*X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PublicKey pk = kf.generatePublic(X509publicKey);
                
                return pk;*/
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
            } catch (InvalidKeySpecException invalidKeySpecException) {
                invalidKeySpecException.printStackTrace();
            } 
            
            return null;
        }
    }
    
    @RequestMapping(value = "/loginGET/{username}/{password}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User loginGET(HttpServletRequest req, HttpServletResponse res, @PathVariable String username, @PathVariable String password) throws IOException{
        
        UsernamePasswordAuthenticationToken authReq  = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(authReq);
        if (auth.isAuthenticated()){
            SecurityContext sc = SecurityContextHolder.getContext();
        
            sc.setAuthentication(auth);

            HttpSession session = req.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

            User u=userService.find(username);
            return u;
        }
        else{
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
    }
    //@PreAuthorize("permitAll")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    //public User login(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
    public User login(HttpServletRequest req, HttpServletResponse res,@RequestBody User user) throws IOException{
                  
        String username=user.getUsername();
        String password=user.getPassword();
        
        UsernamePasswordAuthenticationToken authReq  = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(authReq);
        if (auth.isAuthenticated()){
            SecurityContext sc = SecurityContextHolder.getContext();
        
            sc.setAuthentication(auth);

            HttpSession session = req.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

            User u=userService.find(username);
            return u;
        }
        else{
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
    }
    
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/createUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User createUser(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        User existingUsr=userService.find(user.getUsername());

        if (existingUsr!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }

        String hashedPwd=CustomAuthenticationProvider.hashPassword(user.getPassword());
        user.setPassword(hashedPwd);
        user.setJwt(false);
        

        userService.addUser(user);
        return userService.find(user.getUsername());
        
    }
    
    @PreAuthorize("hasAnyRole('SCHOOLORG','ORGADMIN','SCHOOLADMIN')")
    @RequestMapping(value = "/createMentorUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User createMentorUser(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        if (currentUser==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN , "Non existing user logged in!");
            return null;
        }
        
        User existingUsr=userService.find(user.getUsername());
        if (existingUsr!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }

        String hashedPwd=CustomAuthenticationProvider.hashPassword(user.getPassword());
        user.setPassword(hashedPwd);
        user.setOrganization(currentUser.hasRole("ORGADMIN")?currentUser.getOrganization():user.getOrganization());   
        String[] roles={"ROLE_MENTOR"};
        user.setRoles(roles);
        user.setJwt(false);
        user.setSchool(currentUser.hasRole("SCHOOLADMIN") || currentUser.hasRole("ORGADMIN") || currentUser.hasRole("SCHOOLORG")?currentUser.getSchool():user.getSchool());

        userService.addUser(user);
        return userService.find(user.getUsername());     
    }
    
    @PreAuthorize("hasAnyRole('SCHOOLORG','ORGADMIN','SCHOOLADMIN')")
    @RequestMapping(value = "/createOrgadminUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User createOrgadminUser(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        if (currentUser==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN , "Non existing user logged in!");
            return null;
        }
        
        User existingUsr=userService.find(user.getUsername());
        if (existingUsr!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }

        String hashedPwd=CustomAuthenticationProvider.hashPassword(user.getPassword());
        user.setPassword(hashedPwd);
        user.setOrganization(currentUser.hasRole("ORGADMIN")?currentUser.getOrganization():user.getOrganization());   
        String[] roles={"ROLE_ORGADMIN"};
        user.setRoles(roles);
        user.setJwt(false);
        user.setSchool(currentUser.hasRole("SCHOOLADMIN") || currentUser.hasRole("ORGADMIN") || currentUser.hasRole("SCHOOLORG")?currentUser.getSchool():user.getSchool());

        userService.addUser(user);
        return userService.find(user.getUsername());     
    }
    
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/createStudentUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User createStudentUser(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        if (currentUser==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN , "Non existing user logged in!");
            return null;
        }
        
        User existingUsr=userService.find(user.getUsername());
        if (existingUsr!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }

        String hashedPwd=CustomAuthenticationProvider.hashPassword(user.getPassword());
        user.setPassword(hashedPwd);
        user.setOrganization(currentUser.getOrganization());   
        String[] roles={"ROLE_STUDENT"};
        user.setRoles(roles);
        user.setJwt(false);
        user.setSchool(currentUser.getSchool());

        userService.addUser(user);
        return userService.find(user.getUsername());     
    }
    
    @PreAuthorize("hasRole('SCHOOLADMIN')")
    @RequestMapping(value = "/createSchooladminUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User createSchooladminUser(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        if (currentUser==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN , "Non existing user logged in!");
            return null;
        }
        
        User existingUsr=userService.find(user.getUsername());
        if (existingUsr!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }

        String hashedPwd=CustomAuthenticationProvider.hashPassword(user.getPassword());
        user.setPassword(hashedPwd);
        user.setOrganization(currentUser.getOrganization());   
        String[] roles={"ROLE_SCHOOLADMIN"};
        user.setRoles(roles);
        user.setJwt(false);
        user.setSchool(currentUser.getSchool());

        userService.addUser(user);
        return userService.find(user.getUsername());     
    }
    
    @PreAuthorize("hasRole('SCHOOLADMIN')")
    @RequestMapping(value = "/createSchoolorganizerUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User createSchoolorganizerUser(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        if (currentUser==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN , "Non existing user logged in!");
            return null;
        }
        
        User existingUsr=userService.find(user.getUsername());
        if (existingUsr!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }

        String hashedPwd=CustomAuthenticationProvider.hashPassword(user.getPassword());
        user.setPassword(hashedPwd);
        user.setOrganization(currentUser.getOrganization());   
        String[] roles={"ROLE_SCHOOLORG"};
        user.setRoles(roles);
        user.setJwt(false);
        user.setSchool(currentUser.getSchool());

        userService.addUser(user);
        return userService.find(user.getUsername());     
    }
    
    
    
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User deleteUser(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        User existingUsr=userService.find(user.getUsername());

        if (existingUsr==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
        
        if (currentUser.hasRole("ROLE_ADMIN") ||
                (currentUser.hasRole("SCHOOL_ADMIN") && !existingUsr.hasRole("ROLE_ADMIN")) ||
                (currentUser.hasRole("SCHOOL_ORG") && (existingUsr.hasRole("ROLE_STUDENT") )  )
                ){
            if (currentUser.getSchool().equals(existingUsr.getSchool())){
                userService.deleteUser(user.getUsername());
                return existingUsr;
            }
        }
        
        res.sendError(HttpServletResponse.SC_FORBIDDEN , "Insufficient permission level");
        return null;
        
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/modifyUserPermission", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User modifyUserPermission(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        User existingUsr=userService.find(user.getUsername());

        if (existingUsr==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }

        User newU=new User();
        newU.setUsername(existingUsr.getUsername());
        newU.setPassword(existingUsr.getPassword());
        newU.setRoles(user.getRoles());
        newU.setJwt(false);
        userService.editUser(newU, user.getUsername());
        return newU;
        
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/listUsers", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<User> listUsers(HttpServletRequest req, HttpServletResponse res) throws IOException{
        return userService.findAll();
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/listUsersWithinOrganization", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<User> listUsersWithinOrganization(HttpServletRequest req, HttpServletResponse res) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        User currentUser=null;
        if (authname!=null){
            currentUser=userService.find(authname);
        }
        
        return userService.findUsersWithinOrganization(currentUser);
    }
    
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN','STUDENT')")
    @RequestMapping(value = "/listUsersWithinSchool", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<User> listUsersWithinSchool(HttpServletRequest req, HttpServletResponse res) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        User currentUser=null;
        if (authname!=null){
            currentUser=userService.find(authname);
        }
        List<User> ret=userService.findUsersWithinSchool(currentUser);
        
        if (currentUser.hasRole("STUDENT") && !(currentUser.hasRole("SCHOOLORG") || currentUser.hasRole("SCHOOLADMIN"))){
            for (int i=ret.size()-1;i>=0;i--){
                User u=ret.get(i);
                if (!(u.hasRole("SCHOOLORG") || u.hasRole("SCHOOLADMIN"))){
                    ret.remove(i);
                }
            }
        }
        
        return ret;
    }
    
    
    
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/modifyUserPassword", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User modifyUserPassword(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        User currentUser=null;
        if (authname!=null){
            currentUser=userService.find(authname);
        }
        
        
        
        if (!authname.equals(user.getUsername()) && !(currentUser.hasRole("ADMIN") || currentUser.hasRole("SCHOOLADMIN"))){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Only ADMIN and SCHOOLADMIN can change password for other users" );
            return null;
        }
        
        User existingUsr=userService.find(user.getUsername());

        if (existingUsr==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "user does not exist" );
            return null;
        }
        
        if (existingUsr.getJwt()){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Editng JWT users not allowed!!!");
            return null;
        }
        
        if (!currentUser.getSchool().equals(existingUsr.getSchool()) ){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "currentUser and user must belong to same school" );
            return null;
        }

        String hashedPwd=CustomAuthenticationProvider.hashPassword(user.getPassword());
        //User newU=new User();
        //existingUsr.setUsername(existingUsr.getUsername());
        existingUsr.setPassword(hashedPwd);
        //existingUsr.setRoles(existingUsr.getRoles());

        userService.editUser(existingUsr, user.getUsername());
        return existingUsr;
        
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/modifyUser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User modifyUser(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=userService.find(authname);
        User nuser=userService.find(user.getUsername());
        
        if (currentUser == null || nuser==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        
        if (nuser.getJwt() && nuser.hasRole("STUDENT") && nuser.getRoles().length==1){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Editng JWT users not allowed!!!");
            return null;
        }
        
        if (user.getPassword()!=null && !nuser.getPassword().equals(user.getPassword())  ) {
            String hashedPassword=CustomAuthenticationProvider.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
        }
        
        if (authname.equals(user.getUsername())){
            // if current user modifies self - can change everything except permissions
            user.setRoles( currentUser.getRoles() );
            userService.editUser(user, user.getUsername());
            return userService.find(user.getUsername());
        }
        if (currentUser.hasRole("ADMIN")){
            // if current user is ADMIN -> can change everythhing for everyone
            userService.editUser(user, user.getUsername());
            return userService.find(user.getUsername());
        }
        if (currentUser.hasRole("SCHOOLADMIN") && currentUser.getSchool().equals( nuser.getSchool() )){
            // if current user is SCHOOLADMIN -> can change for everone in his school
            userService.editUser(user, user.getUsername());
            return userService.find(user.getUsername());
        }
        if (currentUser.hasRole("SCHOOLORG") && currentUser.getSchool().equals( nuser.getSchool() ) && !nuser.hasRole("SCHOOLADMIN")){
            // if current user is SCHOOLORG -> can change for everone in his school (except SCHOOLADMIN)
            userService.editUser(user, user.getUsername());
            return userService.find(user.getUsername());
        }
        if (currentUser.hasRole("ORGADMIN") && currentUser.getOrganization().equals( nuser.getOrganization() )   ){
            // if current user is ORGADMIN -> can change for everone in his ORGANIZATION
            userService.editUser(user, user.getUsername());
            return userService.find(user.getUsername());
        }
        
        res.sendError(HttpServletResponse.SC_FORBIDDEN );
        return null;
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public void logout(HttpServletRequest req, HttpServletResponse res){
        
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(null);
        
        HttpSession session = req.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        
        res.setStatus(HttpServletResponse.SC_OK);

        return;
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/currentUser", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User currentUser(HttpServletRequest req, HttpServletResponse res){

        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        
        if (authname!=null){
            User existingUsr=userService.find(authname);
            return existingUsr;
        }

        return null;
    }
}