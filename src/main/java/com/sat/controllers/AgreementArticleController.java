/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.AgreementArticle;
import com.sat.entity.User;
import com.sat.entity.dao.AgreementArticleTemplateDao;
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
public class AgreementArticleController {
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private AgreementArticleTemplateDao agreementArticleTemplateService;
    
    @Autowired
    private UserServiceI userService;   
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG')")
    @RequestMapping(value = "/createTemplateArticle", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public AgreementArticle createTemplateArticle(HttpServletRequest req, HttpServletResponse res, @RequestBody AgreementArticle article) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Integer article_id=agreementArticleTemplateService.addArticle(article, currentUser);
        return agreementArticleTemplateService.find(article_id, currentUser);
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG')")
    @RequestMapping(value = "/deleteTemplateArticle/{article_id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public AgreementArticle deleteTemplateArticle(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer article_id) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        AgreementArticle existing=agreementArticleTemplateService.find(article_id, currentUser);

        if (existing==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
               
        agreementArticleTemplateService.deleteArticle(article_id,currentUser);
        return existing;
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG')")
    @RequestMapping(value = "/editTemplateArticle", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public AgreementArticle editTemplateArticle(HttpServletRequest req, HttpServletResponse res, @RequestBody AgreementArticle article) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        
        AgreementArticle _old=agreementArticleTemplateService.find(article.getId() ,currentUser  );

        if (_old==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
             
        return agreementArticleTemplateService.editArticle(article, currentUser); 
        //return article;
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLADMIN','SCHOOLORG')")
    @RequestMapping(value = "/listTemplateArticles", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<AgreementArticle> listTemplateArticles(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return agreementArticleTemplateService.findAll(currentUser);
    }

}
