/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

import java.sql.Date;

/**
 *
 * @author dean
 */
public class StudentWorklog {
    private Integer id;
    
    //private Assignment assignment;
    
    private String student_log;
    
    private Date date_studentlog;
    
    private String mentor_log;
    
    private Date date_mentorlog;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the student_log
     */
    public String getStudent_log() {
        return student_log;
    }

    /**
     * @param student_log the student_log to set
     */
    public void setStudent_log(String student_log) {
        this.student_log = student_log;
    }

    /**
     * @return the date_studentlog
     */
    public Date getDate_studentlog() {
        return date_studentlog;
    }

    /**
     * @param date_studentlog the date_studentlog to set
     */
    public void setDate_studentlog(Date date_studentlog) {
        this.date_studentlog = date_studentlog;
    }

    /**
     * @return the mentor_log
     */
    public String getMentor_log() {
        return mentor_log;
    }

    /**
     * @param mentor_log the mentor_log to set
     */
    public void setMentor_log(String mentor_log) {
        this.mentor_log = mentor_log;
    }

    /**
     * @return the date_mentorlog
     */
    public Date getDate_mentorlog() {
        return date_mentorlog;
    }

    /**
     * @param date_mentorlog the date_mentorlog to set
     */
    public void setDate_mentorlog(Date date_mentorlog) {
        this.date_mentorlog = date_mentorlog;
    }
}
/*


create table StudentWorklog(
	id INT NOT NULL AUTO_INCREMENT,
        assignment_id INT NOT NULL,
	student_log TEXT NULL,
        date_studentlog DATE null,
	mentor_log TEXT NULL,
        date_mentorlog DATE null,
	CONSTRAINT StudentWorklog_PK PRIMARY KEY (id),
	CONSTRAINT StudentWorklog__Assignment__FK FOREIGN KEY (assignment_id) REFERENCES Assignment(id)
);


*/