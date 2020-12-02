/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author dean
 */
public class Assignment {

    private Integer id;
    
    private String description;
    
    private List<StudyCompetence> study_competences;
    
    private InternshipAgreement internship_agreement;
    
    private Date deadline;
    
    private List<StudentWorklog> student_worklog;

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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the study_competences
     */
    public List<StudyCompetence> getStudy_competences() {
        return study_competences;
    }

    /**
     * @param study_competences the study_competences to set
     */
    public void setStudy_competences(List<StudyCompetence> study_competences) {
        this.study_competences = study_competences;
    }

    /**
     * @return the internship_agreement
     */
    public InternshipAgreement getInternship_agreement() {
        return internship_agreement;
    }

    /**
     * @param internship_agreement the internship_agreement to set
     */
    public void setInternship_agreement(InternshipAgreement internship_agreement) {
        this.internship_agreement = internship_agreement;
    }

    /**
     * @return the deadline
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * @param deadline the deadline to set
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * @return the student_worklog
     */
    public List<StudentWorklog> getStudent_worklog() {
        return student_worklog;
    }

    /**
     * @param student_worklog the student_worklog to set
     */
    public void setStudent_worklog(List<StudentWorklog> student_worklog) {
        this.student_worklog = student_worklog;
    }
}

/*

create table Assignment(
	id INT NOT NULL AUTO_INCREMENT,
        internship_agreement_id INT NOT NULL,
	description TEXT NULL,
        deadline DATE null,
	CONSTRAINT Assignment_PK PRIMARY KEY (id),
	CONSTRAINT Assignment__InternshipAgreement__FK FOREIGN KEY (internship_agreement_id) REFERENCES INTERNSHIP_AGREEMENT(id)
);

create table Assignment_StudyCompetences_Map(
	id INT NOT NULL AUTO_INCREMENT,
        assignment_id INT NOT NULL,
        studycompetence_id INT NOT NULL,
	CONSTRAINT Assignment_StudyCompetences_Map_PK PRIMARY KEY (id),
	CONSTRAINT Ass_StudyComp_Map__Assignment__FK FOREIGN KEY (assignment_id) REFERENCES Assignment(id),
        CONSTRAINT Ass_StudyComp_Map__STUDY_COMPETENCES__FK FOREIGN KEY (assignment_id) REFERENCES STUDY_COMPETENCES(id)
);


*/