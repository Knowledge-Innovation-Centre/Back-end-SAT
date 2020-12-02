/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.Organization;
import com.sat.entity.User;
import com.sat.entity.dao.OrganizationDao;
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
//@RequestMapping(value = "/organization")
public class OrganizationController {
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private OrganizationDao organizationService;
    
    @Autowired
    private UserServiceI userService;
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/listOrganizations", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<Organization> listOrganizations(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return organizationService.findAll(currentUser, null);
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/listOrganizationsByType/{organization_type}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<Organization> listOrganizationsByType(HttpServletRequest req, HttpServletResponse res, @PathVariable String organization_type) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return organizationService.findAll(currentUser, organization_type);
    }
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/createOrganization", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Organization createOrganization(HttpServletRequest req, HttpServletResponse res, @RequestBody Organization org) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Organization existingOrg=organizationService.find(org.getId(), currentUser);

        if (existingOrg!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
        organizationService.addOrganization(org,currentUser);
        return org;
    }
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/deleteOrganization/{organization_id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Organization deleteOrganization(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer organization_id) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Organization existing=organizationService.find(organization_id, currentUser);

        if (existing==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
               
        organizationService.deleteOrganization(organization_id,currentUser);
        return existing;
    }
    
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG','ADMIN')")
    @RequestMapping(value = "/editOrganization", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Organization editOrganization(HttpServletRequest req, HttpServletResponse res, @RequestBody Organization org) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        
        Organization _old=organizationService.find(org.getId() ,currentUser  );

        if (_old==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
             
        organizationService.editOrganization(org,currentUser);
        return org;
    }
}
