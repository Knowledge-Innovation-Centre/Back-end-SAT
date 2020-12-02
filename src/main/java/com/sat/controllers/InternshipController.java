/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.Internship;
import com.sat.entity.Organization;
import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import com.sat.entity.dao.InternshipDao;
import com.sat.entity.dao.OrganizationDao;
import com.sat.entity.dao.StudyProgramDaoI;
import com.sat.entity.servicebeans.UserServiceI;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class InternshipController {
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private InternshipDao internshipService;
    
    @Autowired
    private OrganizationDao organizationService;
    
    @Autowired
    private StudyProgramDaoI studyProgramService;
    
    @Autowired
    private UserServiceI userService;   
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/listInternships", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<Internship> listInternships(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return internshipService.findAll(currentUser);
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/listInternships/{study_program_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<Internship> listInternships(HttpServletRequest req, HttpServletResponse res, @PathVariable int study_program_id) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return internshipService.findAllByStudyProgram(study_program_id,currentUser);
    }
    
    @PreAuthorize("hasRole('SCHOOLORG')")
    @RequestMapping(value = "/createInternship", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Internship createInternship(HttpServletRequest req, HttpServletResponse res, @RequestBody Internship internship) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Organization existingOrg=organizationService.find(internship.getOrganization().getId(), currentUser);
        if (existingOrg==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Internship should be created only for existing organizations! ");
            return null;
        }
        StudyProgram existingStudyProgram=studyProgramService.find(internship.getStudyProgram().getId(), currentUser);
        if (existingStudyProgram==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Internship should be created only for existing studyprograms! ");
            return null;
        }
        
        internshipService.addInternship(internship, currentUser);
        return internship;
    }
    
    @PreAuthorize("hasRole('SCHOOLORG')")
    @RequestMapping(value = "/deleteInternship/{internship_id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Internship deleteInternship(HttpServletRequest req, HttpServletResponse res, @PathVariable int internship_id) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Internship internship=internshipService.find(internship_id, currentUser);
        internshipService.deleteInternship(internship_id, currentUser);
        return internship;
    }
    
    @PreAuthorize("hasRole('SCHOOLORG')")
    @RequestMapping(value = "/editInternship", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Internship editInternship(HttpServletRequest req, HttpServletResponse res, @RequestBody Internship internship) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Organization existingOrg=organizationService.find(internship.getOrganization().getId(), currentUser);
        if (existingOrg==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Internship should be created only for existing organizations! ");
            return null;
        }
        StudyProgram existingStudyProgram=studyProgramService.find(internship.getStudyProgram().getId(), currentUser);
        if (existingStudyProgram==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Internship should be created only for existing studyprograms! ");
            return null;
        }
             
        internshipService.editInternship(internship, currentUser);
        return internship;
    }
}
