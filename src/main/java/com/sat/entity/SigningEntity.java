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
public class SigningEntity {
    private InternshipAgreement internship_agreement;
    
    private String signed_student_username;
    private String signed_school_username;
    private String signed_organization_username;

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
     * @return the signed_student_username
     */
    public String getSigned_student_username() {
        return signed_student_username;
    }

    /**
     * @param signed_student_username the signed_student_username to set
     */
    public void setSigned_student_username(String signed_student_username) {
        this.signed_student_username = signed_student_username;
    }

    /**
     * @return the signed_school_username
     */
    public String getSigned_school_username() {
        return signed_school_username;
    }

    /**
     * @param signed_school_username the signed_school_username to set
     */
    public void setSigned_school_username(String signed_school_username) {
        this.signed_school_username = signed_school_username;
    }

    /**
     * @return the signed_organization_username
     */
    public String getSigned_organization_username() {
        return signed_organization_username;
    }

    /**
     * @param signed_organization_username the signed_organization_username to set
     */
    public void setSigned_organization_username(String signed_organization_username) {
        this.signed_organization_username = signed_organization_username;
    }
}
