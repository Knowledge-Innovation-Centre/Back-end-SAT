/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.School;
import com.sat.entity.User;
import com.sat.entity.dao.SchoolDao;
import com.sat.entity.servicebeans.UserServiceI;
import com.sat.security.CustomAuthenticationProvider;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Dean
 */
@Controller
//@RequestMapping(value = "/student")
public class StudentController {
    
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private UserServiceI userService;
    
    @Autowired
    private SchoolDao schoolService;
    
    @RequestMapping(value = "/selfRegister", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User createUser(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
        
        User existingUsr=userService.find(user.getUsername());
        if (existingUsr!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        String school_id=user.getSchool();
        if (school_id==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN,"Student can only register with school_id!!!");
            return null;
        }
        School sch=schoolService.find(school_id);
        if (sch==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN,"Student can only register with existing school_id!!!");
            return null;
        }
        String pk=sch.getPublic_key();
        if (pk!=null && !("".equals(pk))){
            res.sendError(HttpServletResponse.SC_FORBIDDEN,"Student should login via JWT!!!");
            return null;
        }

        String hashedPwd=CustomAuthenticationProvider.hashPassword(user.getPassword());
        user.setPassword(hashedPwd);
        user.setRoles(new String[] {"ROLE_STUDENT"});
        user.setJwt(false);

        userService.addUser(user);
        return userService.find(user.getUsername());
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = "/modifySelf", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public User modifySelf(HttpServletRequest req, HttpServletResponse res, @RequestBody User user) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        if (!authname.equals(user.getUsername())){
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
        User existingUsr=userService.find(user.getUsername());
        if (existingUsr==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        if (existingUsr.getJwt()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Editng JWT users not allowed!!!");
            return null;
        }
        
        user.setUsername(authname);
        user.setRoles(new String[] {"ROLE_STUDENT"});

        return userService.find(authname);
    }
    
}
