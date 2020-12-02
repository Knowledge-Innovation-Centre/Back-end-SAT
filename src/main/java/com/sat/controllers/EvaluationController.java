/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.User;
import com.sat.entity.EvaluationQuestion;
import com.sat.entity.EvaluationAnswer;
import com.sat.entity.dao.EvaluationDao;
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
public class EvaluationController {

    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private EvaluationDao evaluationDao;
    
    @Autowired
    private UserServiceI userService;
    
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/listQuestionsForStudent", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationQuestion> listQuestionsForStudent(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.getQuestionsForRole(currentUser, "STUDENT");
    }
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/storeQuestionsForStudent", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationQuestion> storeQuestionsForStudent(HttpServletRequest req, HttpServletResponse res, @RequestBody List<EvaluationQuestion> questions) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.storeQuestionsForRole(currentUser, questions, "STUDENT");
    }
    
    
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/listQuestionsForMentor", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationQuestion> listQuestionsForMentor(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.getQuestionsForRole(currentUser, "MENTOR");
    }
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/storeQuestionsForMentor", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationQuestion> storeQuestionsForMentor(HttpServletRequest req, HttpServletResponse res, @RequestBody List<EvaluationQuestion> questions) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.storeQuestionsForRole(currentUser, questions, "MENTOR");
    }
    
    
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/listQuestionsForSchool", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationQuestion> listQuestionsForSchool(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.getQuestionsForRole(currentUser, "SCHOOL");
    }
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/storeQuestionsForSchool", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationQuestion> storeQuestionsForSchool(HttpServletRequest req, HttpServletResponse res, @RequestBody List<EvaluationQuestion> questions) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.storeQuestionsForRole(currentUser, questions, "SCHOOL");
    }
    
    
    
    
    
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN','STUDENT')")
    @RequestMapping(value = "/listAnswersForStudent/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationAnswer> listAnswersForStudent(HttpServletRequest req, HttpServletResponse res, @PathVariable int internship_agreement_id) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.getAnswersForInternshipAgreement(internship_agreement_id, currentUser, "STUDENT");
    }
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN','STUDENT')")
    @RequestMapping(value = "/storeAnswersForStudent/{internship_agreement_id}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationAnswer> storeAnswersForStudent(HttpServletRequest req, HttpServletResponse res, @PathVariable int internship_agreement_id, @RequestBody List<EvaluationAnswer> answers) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.storeAnswersForInternshipAgreement(internship_agreement_id, currentUser, "STUDENT", answers);
    }
    
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN','MENTOR')")
    @RequestMapping(value = "/listAnswersForMentor/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationAnswer> listAnswersForMentor(HttpServletRequest req, HttpServletResponse res, @PathVariable int internship_agreement_id) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.getAnswersForInternshipAgreement(internship_agreement_id, currentUser, "MENTOR");
    }
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN','MENTOR')")
    @RequestMapping(value = "/storeAnswersForMentor/{internship_agreement_id}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationAnswer> storeAnswersForMentor(HttpServletRequest req, HttpServletResponse res, @PathVariable int internship_agreement_id, @RequestBody List<EvaluationAnswer> answers) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.storeAnswersForInternshipAgreement(internship_agreement_id, currentUser, "MENTOR", answers);
    }
    
    
    
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/listAnswersForSchool/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationAnswer> listAnswersForSchool(HttpServletRequest req, HttpServletResponse res, @PathVariable int internship_agreement_id) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.getAnswersForInternshipAgreement(internship_agreement_id, currentUser, "SCHOOL");
    }
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/storeAnswersForSchool/{internship_agreement_id}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EvaluationAnswer> storeAnswersForSchool(HttpServletRequest req, HttpServletResponse res, @PathVariable int internship_agreement_id, @RequestBody List<EvaluationAnswer> answers) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return evaluationDao.storeAnswersForInternshipAgreement(internship_agreement_id, currentUser, "SCHOOL", answers);
    }
}
