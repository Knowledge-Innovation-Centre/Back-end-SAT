/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.Organization;
import com.sat.entity.StudyProgram;
import com.sat.entity.User;
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
//@RequestMapping(value = "/studyprogram")
public class StudyProgramController {
    
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private StudyProgramDaoI studyProgramService;
    
    @Autowired
    private UserServiceI userService;
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN','STUDENT')")
    @RequestMapping(value = "/listStudyPrograms", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<StudyProgram> listStudyPrograms(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return studyProgramService.findAll(currentUser);
    }
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN','STUDENT')")
    @RequestMapping(value = "/getStudyProgram/{studyprogram_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public StudyProgram getStudyProgram(HttpServletRequest req, HttpServletResponse res, @PathVariable int studyprogram_id) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return studyProgramService.find(studyprogram_id, currentUser);
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG')")
    @RequestMapping(value = "/createStudyProgram", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public StudyProgram createStudyProgram(HttpServletRequest req, HttpServletResponse res, @RequestBody StudyProgram sp) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        StudyProgram existingOrg=studyProgramService.find(sp.getId(), currentUser);

        if (existingOrg!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
        return studyProgramService.addStudyProgram(sp, currentUser);
        //return sp;
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG')")
    @RequestMapping(value = "/deleteStudyProgram/{studyprogram_id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public StudyProgram deleteStudyProgram(HttpServletRequest req, HttpServletResponse res, @PathVariable String studyprogram_id) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        StudyProgram existing=studyProgramService.find(Integer.valueOf(studyprogram_id), currentUser);

        if (existing==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
               
        studyProgramService.deleteStudyProgram(Integer.valueOf(studyprogram_id),currentUser);
        return existing;
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG')")
    @RequestMapping(value = "/editStudyProgram", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public StudyProgram editStudyProgram(HttpServletRequest req, HttpServletResponse res, @RequestBody StudyProgram studyProgram) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        
        StudyProgram _old=studyProgramService.find(studyProgram.getId() ,currentUser  );

        if (_old==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
             
        studyProgramService.editStudyProgram(studyProgram,currentUser);
        return studyProgramService.find(studyProgram.getId() ,currentUser  );
        //return studyProgram;
    }
    
}
