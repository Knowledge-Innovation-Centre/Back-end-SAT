/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

import java.util.List;

/**
 *
 * @author Dean
 * 
 * id INT NOT NULL AUTO_INCREMENT,
	internship_agreement_id int not null,
	study_competences_id int not null,
	grade int null,
	grade_text NVARCHAR(512) null,
	mentor_username NVARCHAR(64) not null,
 */
public class Grades {
    private InternshipAgreement internshipAgreement;
    private StudyCompetence studyCompetence;
    private Integer grade;
    private String grate_text;
    private User mentor;

    /**
     * @return the internshipAgreement
     */
    public InternshipAgreement getInternshipAgreement() {
        return internshipAgreement;
    }

    /**
     * @param internshipAgreement the internshipAgreement to set
     */
    public void setInternshipAgreement(InternshipAgreement internshipAgreement) {
        this.internshipAgreement = internshipAgreement;
    }

    /**
     * @return the grade
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * @return the grate_text
     */
    public String getGrate_text() {
        return grate_text;
    }

    /**
     * @param grate_text the grate_text to set
     */
    public void setGrate_text(String grate_text) {
        this.grate_text = grate_text;
    }

    /**
     * @return the mentor
     */
    public User getMentor() {
        return mentor;
    }

    /**
     * @param mentor the mentor to set
     */
    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    /**
     * @return the studyCompetence
     */
    public StudyCompetence getStudyCompetence() {
        return studyCompetence;
    }

    /**
     * @param studyCompetence the studyCompetence to set
     */
    public void setStudyCompetence(StudyCompetence studyCompetence) {
        this.studyCompetence = studyCompetence;
    }
    
    
}
