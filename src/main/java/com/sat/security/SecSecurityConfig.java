/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */





package com.sat.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
//@ComponentScan({"com.nizetic.security","com.nizetic.controllers","com.nizetic.entity.servicebeans","com.nizetic.entity.dao"})

//@ImportResource({ "classpath:webSecurityConfig.xml" })
//@ImportResource({ "security.xml"})
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {
    /*public SecSecurityConfig() {
      super();
    }*/
   
    @Autowired
    private CustomAuthenticationProvider authProvider;
   
    @Override
    protected void configure(
      AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("*****"+SecSecurityConfig.super.authenticationManagerBean().getClass());
        //authProvider=(SecSecurityConfig.super.authenticationManagerBean()).;
        auth.authenticationProvider(authProvider);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(
                            "/json/login","/json/loginJWT","/json/loginJWT/{jwttoken}","/json/logout","/json/loginGET/{username}/{password}","/json/selfRegister",
                            "/docs/**","/ang/**").permitAll()
                    .and()
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/docs/api.html")
            ;
                
    }
    
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(15*1024*1024);
        return multipartResolver;
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsc=new CorsConfiguration().applyPermitDefaultValues();
        corsc.addAllowedMethod(HttpMethod.DELETE);
        corsc.addAllowedMethod(HttpMethod.GET);
        corsc.addAllowedMethod(HttpMethod.HEAD);
        corsc.addAllowedMethod(HttpMethod.OPTIONS);
        corsc.addAllowedMethod(HttpMethod.POST);
        corsc.addAllowedOrigin("*");
        corsc.addExposedHeader("Cookie");
        corsc.addExposedHeader("Set-Cookie");
        corsc.addAllowedHeader("Cookie");
        corsc.addAllowedHeader("Authorization");
        corsc.addAllowedHeader("Content-Type");
        corsc.setAllowCredentials(Boolean.TRUE);
        source.registerCorsConfiguration("/**", corsc);;
        return source;
    }
    /*
    @Bean
    public CookieSerializer cookieSerializer() {
            DefaultCookieSerializer serializer = new DefaultCookieSerializer();
            serializer.setCookieName("JSESSIONID"); 
            serializer.setCookiePath("/"); 
            serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); 
            return serializer;
    }*/
    /*
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
                //registry
                //        .addMapping("/json/**")
                //        .allowedOrigins("*")
                //        .allowCredentials(false).maxAge(3600);
            }
        };
    }
    */
    
    /*
    @Bean(name = "corsConfigurationSource")
    CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST","DELETE","HEAD","OPTIONS","PATCH","PUT","TRACE"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
    }*/
    
}







