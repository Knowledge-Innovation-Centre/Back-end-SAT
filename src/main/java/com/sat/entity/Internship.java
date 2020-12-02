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
 */
public class Internship {
    
    private int id;
    private Organization organization;
    private StudyProgram studyProgram;
    private List<StudyCompetence> studyCompetence;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the organization
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /**
     * @return the studyProgram
     */
    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    /**
     * @param studyProgram the studyProgram to set
     */
    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }

    /**
     * @return the studyCompetence
     */
    public List<StudyCompetence> getStudyCompetence() {
        return studyCompetence;
    }

    /**
     * @param studyCompetence the studyCompetence to set
     */
    public void setStudyCompetence(List<StudyCompetence> studyCompetence) {
        this.studyCompetence = studyCompetence;
    }


    
}
