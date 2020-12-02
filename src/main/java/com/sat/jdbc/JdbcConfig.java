/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.jdbc;

import com.sat.controllers.AgreementArticleController;
import com.sat.controllers.EvaluationController;
import com.sat.controllers.FileUploadController;
import com.sat.controllers.InternshipAgreementController;
import com.sat.controllers.InternshipController;
import com.sat.controllers.OrganizationController;
import com.sat.controllers.ReportController;
import com.sat.controllers.SchoolController;
import com.sat.controllers.SecurityController;
import com.sat.controllers.StudentController;
import com.sat.controllers.StudyProgramController;
import com.sat.entity.AgreementArticle;
import com.sat.entity.Assignment;
import com.sat.entity.EvaluationAnswer;
import com.sat.entity.EvaluationQuestion;
import com.sat.entity.FileUpload;
import com.sat.entity.Grades;
import com.sat.entity.IdHolder;
import com.sat.entity.Internship;
import com.sat.entity.InternshipAgreement;
import com.sat.entity.Organization;
import com.sat.entity.School;
import com.sat.entity.SigningEntity;
import com.sat.entity.StudentWorklog;
import com.sat.entity.StudyCompetence;
import com.sat.entity.StudyModule;
import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import com.sat.entity.Worklog;
import com.sat.tools.ApiDocs;
import java.io.File;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


@Configuration
@ComponentScan(
        {
            "com.sat.entity.dao",
            "com.sat.entity.servicebeans",
            "com.sat.controllers",
            "com.sat.jdbc",
            "com.sat.security"
        }
)
public class JdbcConfig {
    
    @Autowired
    ServletContext servletContext;
       
    /*
    @Value("${spring.datasource.url}")
    private String databaseUrl;
    
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    
    @Value("${spring.datasource.password}")
    private String databasePassword;
    
    @Value("${spring.datasource.driver-class-name}")
    private String databaseDriverClass;
    */
    
    @Bean
    public DataSource dataSource() {
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        /*dataSource.setDriverClassName("org.sqlite.JDBC");
        //String dbpath=new File("C:\\Dropbox\\__POSAO\\JavaProjects\\mvnWebSpringProject\\src\\main\\resources\\sqlite\\chinook.db").getAbsolutePath();
        String dbpath=new File("D:\\workspace_netbeans\\inteling\\FluidGPS\\src\\main\\resources\\sqlite\\chinook.db").getAbsolutePath();
        String url="jdbc:sqlite:"+dbpath.replace('\\','/');
        dataSource.setUrl(url); 
        //dataSource.setUsername("guest_user");
        //dataSource.setPassword( "guest_password");
        */
        
        
        //dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");net.sourceforge.jtds.jdbc.Driver
        //String url = "jdbc:sqlserver://10.100.100.205:;databaseName=FluidGPS_Auth;instance=traccardb;user=sa;password=Alk0h0l!0";
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        //String url = "jdbc:mysql://127.0.0.1:3306/SAT-backend?useUnicode=true&characterEncoding=UTF-8";
        //String url = "jdbc:mysql://127.0.0.1:3306/satbackend?useUnicode=true&characterEncoding=UTF-8";
        //Sk\9k^jM8F6+C46c
        //dataSource.setUrl(url);
        
        //dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        /*
        dataSource.setDriverClassName(databaseDriverClass);
        String url = databaseUrl;
        dataSource.setUrl(url);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        */
        
        
        /* */
        //ovh.nizetic.hr
        String url = "jdbc:mysql://127.0.0.1:3306/SAT-backend?useUnicode=true&characterEncoding=UTF-8";
        dataSource.setUrl(url);
        dataSource.setUsername("root");
        dataSource.setPassword("c6Iu3024A41UHK852SUn1");
        /*  */
        
        /*
        // produkcijski
        String url = "jdbc:mysql://127.0.0.1:3306/satbackend?useUnicode=true&characterEncoding=UTF-8";
        dataSource.setUrl(url);
        dataSource.setUsername("dean");
        dataSource.setPassword("Sk\\9k^jM8F6+C46c");
          */
        
        return dataSource;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        
        
        File file = new File( servletContext.getRealPath("/docs/api.html") );
        Class controllers[]={
            AgreementArticleController.class, EvaluationController.class,FileUploadController.class, 
            InternshipAgreementController.class,InternshipController.class,
            OrganizationController.class, SchoolController.class, SecurityController.class,
            StudentController.class, StudyProgramController.class,
            ReportController.class
        };
        Class entities[]={
            AgreementArticle.class,EvaluationAnswer.class,EvaluationQuestion.class,
            FileUpload.class,Grades.class,IdHolder.class, 
            Internship.class,InternshipAgreement.class,Organization.class,
            School.class,SigningEntity.class,
            StudyCompetence.class,StudyModule.class,StudyProgram.class,
            User.class,
            Worklog.class, StudentWorklog.class, Assignment.class
        };
        ApiDocs.generate(file, controllers, entities);
        
        
        
        return jdbcTemplate;
    }
}