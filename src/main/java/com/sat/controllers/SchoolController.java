/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.School;
import com.sat.entity.dao.SchoolDao;
import com.sat.entity.servicebeans.UserServiceI;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Dean
 */
@Controller
//@RequestMapping(value = "/school")
public class SchoolController {
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private SchoolDao schoolService;
    
    @RequestMapping(value = "/listSchools", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<School> listSchools(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        return schoolService.findAll();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/createSchool", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public School createSchool(HttpServletRequest req, HttpServletResponse res, @RequestBody School school) throws IOException{
        
        School existingSchool=schoolService.find(school.getSchool_id());

        if (existingSchool!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
        schoolService.addSchool(school);
        return school;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/deleteSchool/{school_id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public School createSchool(HttpServletRequest req, HttpServletResponse res, @PathVariable String school_id) throws IOException{
        
        School existingSchool=schoolService.find(school_id);

        if (existingSchool==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
        schoolService.deleteSchool(school_id);
        return existingSchool;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/editSchool/{new_school_id}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public School editSchool(HttpServletRequest req, HttpServletResponse res, @RequestBody School school, @PathVariable String new_school_id) throws IOException{
        
        School existingSchool_old=schoolService.find(school.getSchool_id());

        if (existingSchool_old==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
        
        
        if (!new_school_id.equalsIgnoreCase(existingSchool_old.getSchool_id())){
            School existingSchool_new=schoolService.find(new_school_id);
            if (existingSchool_new!=null) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN );
                return null;
            }
        }
        
        
        schoolService.editSchool(school, new_school_id);
        return schoolService.find(new_school_id);
    }
}
