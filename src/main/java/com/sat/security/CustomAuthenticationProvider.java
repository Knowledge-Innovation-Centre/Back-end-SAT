/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.security;

import com.sat.entity.User;
import com.sat.entity.servicebeans.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.sat.entity.servicebeans.UserServiceI;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 *
 * @author dean
 */
@Component
@ComponentScan({"com.sat.jdbc","com.sat.entity.servicebeans"})
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private ApplicationContext appContext;
    
    @Autowired
    private UserServiceI userService;
        
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
  
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        //UserService usrServ=(UserService);
        User usrFromDb=userService.find(username);
         
        //if (usrFromDb!=null && usrFromDb.getPassword().equals(password)  ) {
        if (usrFromDb!=null && (checkPass(password, usrFromDb.getPassword()) || usrFromDb.getPassword().equals(password) )  ) {
            // use the credentials
            // and authenticate against the third-party system
            
            List<GrantedAuthority> lst=new ArrayList<GrantedAuthority>();
            for (String r:(usrFromDb.getRoles())){
                lst.add(new SimpleGrantedAuthority(r));
            }
            //lst.add(new SimpleGrantedAuthority("ROLE_USER"));
            //lst.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            
            return new UsernamePasswordAuthenticationToken(
              username, "****", lst );
        } else {
            UsernamePasswordAuthenticationToken t=new UsernamePasswordAuthenticationToken("failed","",null);
            t.setAuthenticated(false);
            return t;
        }
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    /*
    private boolean checkPass(String plainPassword, String hashedPassword) {
        if (plainPassword.equals(hashedPassword)) return true; // ovo na kraju treba maknuti
        
        if (BCrypt.checkpw(plainPassword, hashedPassword))
            return true;
        else
            return false;
    }
    
    private String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }*/
    
    public static boolean checkPass(String plainPassword, String hashedPassword) {
        if (plainPassword.equals(hashedPassword)) return true; // ovo na kraju treba maknuti
        
        if (BCrypt.checkpw(plainPassword, hashedPassword))
            return true;
        else
            return false;
    }
    
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
}
