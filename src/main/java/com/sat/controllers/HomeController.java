/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.Greeting;
import com.sat.entity.User;
import java.io.IOException;

import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@Controller
public class HomeController {
    @RequestMapping(value="/home", method = RequestMethod.GET)
    public String viewHome(){
        return "home";
    }
    
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    //@PreAuthorize("hasAuthority('ROLE_USER')") //ovo radi!!!
    @PreAuthorize("hasRole('USER')") //ovo radi!!!
    //@PreAuthorize("hasRole('SUPERUSER')") // i ovo radi - vraća FORBIDDEN
    //@Secured("ROLE_TELLER")
    @GetMapping("/hello-world")
    @ResponseBody
    public Greeting sayHello(HttpServletRequest req, @RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
        int x;
        x=1;
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        
        return new Greeting(counter.incrementAndGet(), String.format(template, currentPrincipalName));
        
        
    }
    
    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    // @PreAuthorize("permitAll()")
    public User test(HttpServletRequest req, HttpServletResponse res) throws IOException{
        
        User u=new User();
        u.setUsername("TEST");
        
        return u;
    }
    
}
