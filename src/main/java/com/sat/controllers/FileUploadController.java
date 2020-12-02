/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.FileUpload;
import com.sat.entity.User;
import com.sat.entity.dao.FileUploadDao;
import com.sat.entity.servicebeans.UserServiceI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author dean
 */
@Controller
public class FileUploadController {
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private UserServiceI userService;   
    
    @Autowired
    private FileUploadDao fileUploadService;
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public FileUpload uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        int new_id=fileUploadService.storeUploadedFile(currentUser, null, null, file);
        List<FileUpload> files=fileUploadService.findAll(null, null, null, null, new_id);
        if (files==null) return null;
        if (files.size()==0) return null;
        return files.get(0);
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/uploadFile/{table_name}/{table_id}", method = RequestMethod.POST)
    @ResponseBody
    public FileUpload uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String table_name, @PathVariable Integer table_id) throws IOException {
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        int new_id=fileUploadService.storeUploadedFile(currentUser, table_name, table_id, file);
        List<FileUpload> files=fileUploadService.findAll(null, null, null, null, new_id);
        if (files==null) return null;
        if (files.size()==0) return null;
        return files.get(0);
    }
    //List<FileUpload> findAll(User currentUser, String school_id, String table_name, Integer table_id, Integer id)
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/getUserFiles", method = RequestMethod.GET)
    @ResponseBody
    public List<FileUpload> getUserFiles(HttpServletResponse res) throws IOException{
        return getUserFiles(res, null);
    }
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/getUserFiles/{username}", method = RequestMethod.GET)
    @ResponseBody
    public List<FileUpload> getUserFiles(HttpServletResponse res, @PathVariable String username) throws IOException {
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        User fetch4user=null;
        if (username!=null){
            fetch4user=userService.find(username);
        } else {
            fetch4user=currentUser;
        }
        
        if (fetch4user.getUsername().equals( currentUser.getUsername()  )   ){
            return fileUploadService.findAll(fetch4user, null, null, null, null);
        } else {
            if (currentUser.hasRole("SCHOOLORG") && currentUser.getSchool().equals(  fetch4user.getSchool()  ) ){
                return fileUploadService.findAll(fetch4user, null, null, null, null);
            }else{
                res.sendError(HttpServletResponse.SC_FORBIDDEN , "User has no permission to fetch requested files!!");
                return null;
            }
        }
    }
    @PreAuthorize("hasRole('SCHOOLORG')")
    @RequestMapping(value = "/getSchoolFiles", method = RequestMethod.GET)
    @ResponseBody
    public List<FileUpload> getSchoolFiles(HttpServletResponse res) throws IOException {
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return fileUploadService.findAll(null, currentUser.getSchool(), null, null, null);
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/download/{file_id}", method = RequestMethod.GET)
    public String getUploadedFile(HttpServletResponse res, @PathVariable int file_id, HttpServletRequest request, HttpServletResponse response){
        try {
            SecurityContext sc = SecurityContextHolder.getContext();
            String authname=sc.getAuthentication().getName();
            User currentUser=null;
            if (authname!=null) currentUser=userService.find(authname);
        
            FileUpload f=fileUploadService.getUploadedFile(file_id);
            
            User owner=userService.find(f.getOwner_username());
            
            if (!owner.getUsername().equals(currentUser.getUsername())){
                if (!(
                        (
                                currentUser.hasRole("SCHOOLORG") || currentUser.hasRole("MENTOR") ||
                                currentUser.hasRole("SCHOOLADMIN") || currentUser.hasRole("ORGADMIN")
                        ) && currentUser.getSchool().equals(  owner.getSchool()  )
                     )
                   ){
                    res.sendError(HttpServletResponse.SC_FORBIDDEN , "User has no permission to fetch requested file!!");
                    return null;
                }  
            }
            
            response.setHeader("Content-Disposition", "inline;filename=\"" +f.getFilename()+ "\"");
            //response.setHeader("Content-Disposition", "attachment;filename=\"" +f.getFileName()+ "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType(f.getMime_type());
            response.setContentLength(f.getBytes().length);
            IOUtils.copy(new ByteArrayInputStream(f.getBytes()), out);
            out.flush();
            out.close();

        } catch (java.io.IOException e) {
                e.printStackTrace();
        }
        return null;
    }
    
    
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/deleteFile/{file_id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteFile(@PathVariable Integer file_id) throws IOException {
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        if (currentUser.hasRole("ADMIN") || currentUser.hasRole("SCHOOLADMIN") || currentUser.hasRole("SCHOOLORG"))
            fileUploadService.deleteSchoolFile(currentUser, file_id);
        else
            fileUploadService.deleteOwnFile(currentUser, file_id);
    }
    
    
}
