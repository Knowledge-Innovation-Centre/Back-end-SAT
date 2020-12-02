/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.controllers;

import com.sat.entity.FileUpload;
import com.sat.entity.Grades;
import com.sat.entity.Internship;
import com.sat.entity.InternshipAgreement;
import com.sat.entity.Organization;
import com.sat.entity.SigningEntity;
import com.sat.entity.StudyCompetence;
import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import com.sat.entity.dao.FileUploadDao;
import com.sat.entity.dao.InternshipAgreementDao;
import com.sat.entity.dao.InternshipDao;
import com.sat.entity.dao.OrganizationDao;
import com.sat.entity.dao.StudyProgramDaoI;
import com.sat.entity.servicebeans.UserServiceI;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Dean
 */
@Controller
public class InternshipAgreementController {
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
    
    @Autowired
    private InternshipAgreementDao internshipAgreementService;
    
    @Autowired
    private OrganizationDao organizationService;
    
    @Autowired
    private StudyProgramDaoI studyProgramService;
    
    @Autowired
    private InternshipDao internshipService;
    
    @Autowired
    private UserServiceI userService;   
    
    @Autowired
    private FileUploadDao fileUploadService;
    
    //@PreAuthorize("hasRole('SCHOOLADMIN')")
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/listInternshipAgreements", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<InternshipAgreement> listInternshipAgreements(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return internshipAgreementService.findAll(currentUser, false, null,null,null, null);
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/getInternshipAgreement/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public InternshipAgreement getInternshipAgreement(HttpServletRequest req, HttpServletResponse res,@PathVariable Integer internship_agreement_id) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        //return internshipAgreementService.findAll(currentUser, false, null,null, internship_agreement_id);
        return internshipAgreementService.find(internship_agreement_id, currentUser);
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/listUnsignedInternshipAgreements", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<InternshipAgreement> listUnsignedInternshipAgreements(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return internshipAgreementService.findAll(currentUser, true, null, null, null, null);
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/deleteInternshipAgreement/{internship_agreement_id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public InternshipAgreement deleteInternshipAgreement(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer internship_agreement_id) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        InternshipAgreement existing=internshipAgreementService.find(internship_agreement_id, currentUser);

        if (existing==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
               
        internshipAgreementService.deleteInternshipAgreement(internship_agreement_id, currentUser);
        return existing;
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/createInternshipAgreement", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public InternshipAgreement createInternshipAgreement(HttpServletRequest req, HttpServletResponse res, @RequestBody InternshipAgreement internshipAgreement) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        InternshipAgreement existing=internshipAgreementService.find(internshipAgreement.getId(), currentUser);

        if (existing!=null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
        
        int new_id=internshipAgreementService.addInternshipAgreement(internshipAgreement, currentUser);
        InternshipAgreement newia=internshipAgreementService.find(new_id, currentUser);
        return newia;
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/editInternshipAgreement", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public InternshipAgreement editInternshipAgreement(HttpServletRequest req, HttpServletResponse res, @RequestBody InternshipAgreement agreement) throws IOException{
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        
        InternshipAgreement _old=internshipAgreementService.find(agreement.getId() ,currentUser  );

        if (_old==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN );
            return null;
        }
             
        internshipAgreementService.editInternshipAgreement(agreement,currentUser);
        InternshipAgreement newia=internshipAgreementService.find(agreement.getId(), currentUser);
        return newia;
    }
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/signSchoolInternshipAgreements/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public void signSchoolInternshipAgreements(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer internship_agreement_id){
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        internshipAgreementService.schoolSign(currentUser, internship_agreement_id);
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/signInternshipAgreement", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public InternshipAgreement signInternshipAgreement(HttpServletRequest req, HttpServletResponse res, @RequestBody SigningEntity singing) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Integer internshipAgreement_id=singing.getInternship_agreement().getId();
        InternshipAgreement ia=internshipAgreementService.find(internshipAgreement_id, currentUser);
        if (ia==null){
            res.sendError(HttpServletResponse.SC_FORBIDDEN , "Internship agreement does not exist!!!");
        }
        
        
        if (singing.getSigned_student_username()!=null){
            User student=userService.find(singing.getSigned_student_username());
            if (student!=null) {
                if (!student.getUsername().equals(ia.getStudent().getUsername())){
                    res.sendError(HttpServletResponse.SC_FORBIDDEN , "Student can only sigh his/hers own agreement");
                    return null;
                }
                internshipAgreementService.studentSign(student, internshipAgreement_id);
            }
        }
        
        if (singing.getSigned_school_username()!=null){
            User school=userService.find(singing.getSigned_school_username());
            if (school!=null) {
                if (!school.getSchool().equals(ia.getSchool_id())){
                    res.sendError(HttpServletResponse.SC_FORBIDDEN , "School signer does not belong to school in agreement");
                    return null;
                }
                if (!school.hasRole("SCHOOLORG") && !school.hasRole("SCHOOLADMIN")){
                    res.sendError(HttpServletResponse.SC_FORBIDDEN , "School signer does not have signing permission");
                    return null;
                }
                internshipAgreementService.schoolSign(school, internshipAgreement_id);
            }
        }
        
        if (singing.getSigned_organization_username()!=null){
            User org=userService.find(singing.getSigned_organization_username());
            if (org!=null) {
                if (!org.getOrganization().equals(ia.getOrganization().getId())){
                    res.sendError(HttpServletResponse.SC_FORBIDDEN , "Organization signer does not belong to organization in agreement");
                    return null;
                }
                if (org.hasRole("ORGADMIN")){
                    res.sendError(HttpServletResponse.SC_FORBIDDEN , "Organization signer does not have signing permission");
                    return null;
                }
                internshipAgreementService.studentSign(org, internshipAgreement_id);
            }
        }
        return internshipAgreementService.find(internshipAgreement_id, currentUser);
    }
    
    @PreAuthorize("hasAnyRole('SCHOOLORG','ORGADMIN','STUDENT','MENTOR','ORGADMIN')")
    @RequestMapping(value = "/getInternshipAgreementFiles/{internship_agreement_id}", method = RequestMethod.GET)
    @ResponseBody
    public List<FileUpload> getInternshipAgreementFiles(HttpServletResponse res, @PathVariable Integer internship_agreement_id) throws IOException {
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return fileUploadService.findAll(null, currentUser.getSchool(), "internship_agreement_table", internship_agreement_id, null);
        //findAll(User user, String school_id, String table_name, Integer table_id, Integer id)
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/deleteInternshipAgreementFile/{internship_agreement_id}/{file_id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteInternshipAgreementFile(@PathVariable Integer internship_agreement_id,@PathVariable Integer file_id) throws IOException {
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        fileUploadService.deleteTableFile(currentUser, "internship_agreement_table", internship_agreement_id, file_id);
                
    }
    
    //@PreAuthorize("hasRole('SCHOOLORG')")
    @PreAuthorize("hasAnyRole('SCHOOLORG','SCHOOLADMIN')")
    @RequestMapping(value = "/uploadInternshipAgreementFile/{internship_agreement_id}", method = RequestMethod.POST)
    @ResponseBody
    public FileUpload uploadInternshipAgreementFile(@RequestParam("file") MultipartFile file, @PathVariable Integer internship_agreement_id) throws IOException {
        
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        int new_id=fileUploadService.storeUploadedFile(currentUser, "internship_agreement_table", internship_agreement_id, file);
        //storeUploadedFile(final User currentUser, final String tablename, final Integer tablerecordid, final MultipartFile file)
        List<FileUpload> files=fileUploadService.findAll(null, null, "internship_agreement_table", internship_agreement_id, new_id);
        if (files==null) return null;
        if (files.size()==0) return null;
        return files.get(0);
    }
    @PreAuthorize("hasAnyRole('SCHOOLORG','ORGADMIN','STUDENT')")
    @RequestMapping(value = "/downloadInternshipAgreementFile/{internship_agreement_id}/{file_id}", method = RequestMethod.GET)
    public String getUploadedFile(HttpServletResponse res, @PathVariable int internship_agreement_id, @PathVariable int file_id, HttpServletRequest request, HttpServletResponse response){
        try {
            SecurityContext sc = SecurityContextHolder.getContext();
            String authname=sc.getAuthentication().getName();
            User currentUser=null;
            if (authname!=null) currentUser=userService.find(authname);
            
            InternshipAgreement ia=internshipAgreementService.find(internship_agreement_id, currentUser);
            if (currentUser.hasRole("STUDENT") && !currentUser.hasRole("SCHOOLORG")){
                if (!ia.getStudent().getUsername().equals(currentUser.getUsername())){
                    res.sendError(HttpServletResponse.SC_FORBIDDEN , "User has no permission to fetch requested file!!");
                    return null;
                }
            }
            
            if (currentUser.hasRole("ORGADMIN") && !currentUser.hasRole("SCHOOLORG")){
                if (ia.getOrganization().getId()!=currentUser.getOrganization()){
                    res.sendError(HttpServletResponse.SC_FORBIDDEN , "User has no permission to fetch requested file!!");
                    return null;
                }
            }
            
            List<FileUpload> files=fileUploadService.findAll(null, currentUser.getSchool(), "internship_agreement_table", internship_agreement_id, file_id);
            if (files==null || files.size()==0){
                res.sendError(HttpServletResponse.SC_FORBIDDEN , "User has no permission to fetch requested file!!");
                return null;
            }
            
            FileUpload f=fileUploadService.getUploadedFile(file_id);
            
            User owner=userService.find(f.getOwner_username());
            
            if (!currentUser.getSchool().equals(  owner.getSchool()  )){
                res.sendError(HttpServletResponse.SC_FORBIDDEN , "User has no permission to fetch requested file!!");
                return null;
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
    
    
    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = "/listStudentInternshipAgreements", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<InternshipAgreement> listStudentInternshipAgreements(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return internshipAgreementService.findAll(currentUser, false, currentUser.getUsername(), null, null, null);
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = "/listUnsignedStudentInternshipAgreements", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<InternshipAgreement> listUnsignedStudentInternshipAgreements(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return internshipAgreementService.findAll(currentUser, true, currentUser.getUsername(), null, null, null);
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = "/signStudentInternshipAgreements/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public void signStudentInternshipAgreements(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer internship_agreement_id){
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        internshipAgreementService.studentSign(currentUser, internship_agreement_id);
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = "/applyForInternship/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public InternshipAgreement applyForInternship(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer internship_agreement_id){
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        InternshipAgreement ret=internshipAgreementService.applyForInternship(currentUser, internship_agreement_id);
        
        return ret;
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = "/studentProposal/{study_program_id}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public InternshipAgreement studentProposal(HttpServletRequest req, HttpServletResponse res, @RequestBody Organization organization, @PathVariable Integer study_program_id) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        StudyProgram study_program=studyProgramService.find(study_program_id, currentUser);
        if (study_program==null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN," You can apply only for existing study programs!" );
            return null;
        }
        List<StudyProgram> programs=new ArrayList<StudyProgram>();
        programs.add(study_program);
        organization.setStudyPrograms(programs);
        Organization org=organizationService.addOrganization(organization, currentUser);
        
        Internship internship=new Internship();
        internship.setOrganization(org);
        internship.setStudyProgram(study_program);
        Internship newinternship=internshipService.addInternship(internship, currentUser);
        
        InternshipAgreement ret=internshipAgreementService.applyForInternship(currentUser, newinternship.getId());
        
        return ret;
    }
    
    @PreAuthorize("hasAnyRole('ORGADMIN','MENTOR')")
    @RequestMapping(value = "/listOrganizationInternshipAgreements", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<InternshipAgreement> listOrganizationInternshipAgreements(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return internshipAgreementService.findAll(currentUser, false, null, currentUser.getOrganization() , null, currentUser.getUsername());
    }
    
    @PreAuthorize("hasAnyRole('ORGADMIN','MENTOR')")
    @RequestMapping(value = "/listUnsignedOrganizationInternshipAgreements", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<InternshipAgreement> listUnsignedOrganizationInternshipAgreements(HttpServletRequest req, HttpServletResponse res) throws IOException{
   
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        return internshipAgreementService.findAll(currentUser, true, null, currentUser.getOrganization() , null, currentUser.getUsername());
    }
   
    @PreAuthorize("hasAnyRole('ORGADMIN','MENTOR')")
    @RequestMapping(value = "/signOrganizationInternshipAgreements/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public void signOrganizationInternshipAgreements(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer internship_agreement_id){
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        internshipAgreementService.organizationSign(currentUser, internship_agreement_id);
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/getGrades/{internship_agreement_id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<Grades> getGrades(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer internship_agreement_id) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        InternshipAgreement agreement=internshipAgreementService.find(internship_agreement_id, currentUser);
        boolean is_student=false;
        for (int i=0;i<currentUser.getRoles().length;i++){
            if (currentUser.getRoles()[i].equals("STUDENT")) {
                is_student=true;
                if (!agreement.getStudent().getUsername().equals(currentUser.getUsername())) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN,"You are not allowed to peek at other students grades!" );
                    return null;
                }
            }
        }
        return internshipAgreementService.getGrades(currentUser, internship_agreement_id);
    }
    
    @PreAuthorize("hasRole('MENTOR')")
    @RequestMapping(value = "/storeGrades", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<Grades> storeGrades(HttpServletRequest req, HttpServletResponse res, @RequestBody List<Grades> grades) throws IOException{
        SecurityContext sc = SecurityContextHolder.getContext();
        String authname=sc.getAuthentication().getName();
        User currentUser=null;
        if (authname!=null) currentUser=userService.find(authname);
        
        Hashtable<Integer, InternshipAgreement> hash = new Hashtable<Integer, InternshipAgreement>();
        List<Grades> grades_translated=new ArrayList<Grades>();
        for(int i=0;i<grades.size();i++){
            Grades g=grades.get(i);
            int ia_id=g.getInternshipAgreement().getId();
            
            InternshipAgreement ia=hash.get(ia_id);
            if (ia==null) ia=internshipAgreementService.find(ia_id, currentUser);
            if (ia==null) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN,"Only existing InternshipAgreement can be graded!!!" );
                return null;
            }
            if (ia.getOrganization()==null){
                res.sendError(HttpServletResponse.SC_FORBIDDEN,"InternshipAgreement can only be graded by mentor from contracted organization only!!! (InternshipAgreement has no contracted organization)!" );
                return null;
            }
            if (ia.getOrganization().getId()!=currentUser.getOrganization().intValue()){
                res.sendError(HttpServletResponse.SC_FORBIDDEN,"Only one InternshipAgreement can be graded at a time and by mentor from contracted organization only!!!" );
                return null;
            }
            hash.put(ia_id, ia);
            g.setInternshipAgreement(ia);
            
            int sc_id=g.getStudyCompetence().getId();
            StudyCompetence ia_sc=null;
            for(int j=0;j<ia.getCompetences().size();j++){
                if (sc_id==ia.getCompetences().get(j).getId()) ia_sc=ia.getCompetences().get(j);
            }
            if (ia_sc==null){
                res.sendError(HttpServletResponse.SC_FORBIDDEN,"Found StudyCompetence that does not belong to InternshipAgreement!!!" );
                return null;
            }
            g.setStudyCompetence(ia_sc);
            Integer grade=g.getGrade();
            if (grade!=null){
                if (grade>5 || grade<1){
                    res.sendError(HttpServletResponse.SC_FORBIDDEN,"Grades must be between 1 and 5" );
                    return null;
                }
            }

            grades_translated.add(g);
        }
        
        return internshipAgreementService.storeGrades(currentUser, grades_translated);
    }
}
