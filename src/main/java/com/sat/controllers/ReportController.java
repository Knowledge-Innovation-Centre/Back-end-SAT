/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.Assignment;
import com.sat.entity.InternshipAgreement;
import com.sat.entity.Organization;
import com.sat.entity.StudentWorklog;
import com.sat.entity.User;
import com.sat.entity.Worklog;
import com.sat.entity.dao.InternshipAgreementDao;
import com.sat.entity.dao.OrganizationDao;
import com.sat.entity.dao.ReportDao;
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
 * @author dean
 */
@Controller
public class ReportController {
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private ReportDao reportDao;
    
    @Autowired
    private UserServiceI userService;
    
    @Autowired
    private InternshipAgreementDao internshipAgreementService;
    
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN','ORGADMIN','MENTOR','STUDENT')")
    @RequestMapping(value = "/listAssignments/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<Assignment> listAssignments(HttpServletRequest req, HttpServletResponse res, @PathVariable int internship_agreement_id) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        InternshipAgreement ia=internshipAgreementService.find(internship_agreement_id, currentUser);
        
        if (currentUser.hasRole("STUDENT")){
            String iauser=ia.getStudent().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "STUDENT can work only own internship agreements");
                return null;
            }
        }
        
        if (currentUser.hasRole("MENTOR")){
            String iauser=ia.getMentor().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
        }
                
        List<Assignment> ret=reportDao.fetchAssigmentsForInternshipAgreement(currentUser, internship_agreement_id);
        return ret;
    }
    
    @PreAuthorize("hasRole('MENTOR')")
    @RequestMapping(value = "/createAssignment", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Assignment listAssignments(HttpServletRequest req, HttpServletResponse res, @RequestBody Assignment assignment) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        if (assignment.getInternship_agreement()==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Internship agreements can not be empty");
            return null;
        }
        
        InternshipAgreement ia=internshipAgreementService.find(assignment.getInternship_agreement().getId(), currentUser);
               
        if (currentUser.hasRole("MENTOR")){
            if (ia.getMentor()==null){
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
            String iauser=ia.getMentor().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
        }
        
        Assignment a= reportDao.createAssignment(currentUser, assignment);
        return a;
    }
    @PreAuthorize("hasRole('MENTOR')")
    @RequestMapping(value = "/deleteAssignment/{assignment_id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Assignment deleteAssignment(HttpServletRequest req, HttpServletResponse res, @PathVariable int assignment_id) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Assignment a= reportDao.findById(currentUser, assignment_id);
        InternshipAgreement ia=a.getInternship_agreement();
        
        if (currentUser.hasRole("MENTOR")){
            if (ia.getMentor()==null){
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
            String iauser=ia.getMentor().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
        }
        reportDao.deleteAssignment(currentUser, assignment_id);
        return a;
    }
    @PreAuthorize("hasRole('MENTOR')")
    @RequestMapping(value = "/updateAssignment", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Assignment updateAssignment(HttpServletRequest req, HttpServletResponse res, @RequestBody Assignment assignment) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Assignment a= reportDao.findById(currentUser, assignment.getId());
        InternshipAgreement ia=a.getInternship_agreement();
        
        if (currentUser.hasRole("MENTOR")){
            if (ia.getMentor()==null){
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
            String iauser=ia.getMentor().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
        }
        return reportDao.updateAssignment(currentUser, assignment);
    }
    
    
    @PreAuthorize("hasAnyRole('MENTOR','STUDENT')")
    @RequestMapping(value = "/listWorklog/{assignment_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<StudentWorklog> listWorklog4Assignment(HttpServletRequest req, HttpServletResponse res, @PathVariable int assignment_id) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Assignment a= reportDao.findById(currentUser, assignment_id);
        InternshipAgreement ia=a.getInternship_agreement();
        
        if (currentUser.hasRole("STUDENT")){
            String iauser=ia.getStudent().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "STUDENT can work only own internship agreements");
                return null;
            }
        }
        
        if (currentUser.hasRole("MENTOR")){
            if (ia.getMentor()==null){
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
            String iauser=ia.getMentor().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
        }
                
        List<StudentWorklog> ret=reportDao.fetchWorklog4Assignment(currentUser, assignment_id);
        return ret;
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = "/addWorklog/{assignment_id}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<StudentWorklog> addWorklog4Assignment(HttpServletRequest req, HttpServletResponse res, @RequestBody Worklog wlog, @PathVariable int assignment_id) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Assignment a= reportDao.findById(currentUser, assignment_id);
        InternshipAgreement ia=a.getInternship_agreement();
        
        if (currentUser.hasRole("STUDENT")){
            String iauser=ia.getStudent().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "STUDENT can work only own internship agreements");
                return null;
            }
        }
        /*if (currentUser.hasRole("MENTOR")){
            String iauser=ia.getMentor().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
        }*/
        reportDao.addWorklog4Assignment(currentUser, wlog, assignment_id);
        
        return reportDao.fetchWorklog4Assignment(currentUser, assignment_id);
    }
    
    @PreAuthorize("hasRole('MENTOR')")
    @RequestMapping(value = "/commentWorklog/{worklog_id}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<StudentWorklog>  commentWorklog(HttpServletRequest req, HttpServletResponse res, @RequestBody Worklog wlog, @PathVariable int worklog_id) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Assignment a= reportDao.findByWorklogId(currentUser, worklog_id);
        InternshipAgreement ia=a.getInternship_agreement();
        
        /*if (currentUser.hasRole("STUDENT")){
            String iauser=ia.getStudent().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "STUDENT can work only own internship agreements");
                return null;
            }
        */
        if (currentUser.hasRole("MENTOR")){
            if (ia.getMentor()==null){
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
            String iauser=ia.getMentor().getUsername();
            if (!currentUser.getUsername().equals(iauser)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "MENTOR can work only own internship agreements");
                return null;
            }
        }
        reportDao.commentWorklog(currentUser, wlog, worklog_id);
        return reportDao.fetchWorklog4Assignment(currentUser, a.getId());
    }
}
