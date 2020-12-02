/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

/**
 *
 * @author Dean
 */
public class School {
    private String school_id;
    private String public_key;
    private String public_identifier;

    /**
     * @return the school_id
     */
    public String getSchool_id() {
        return school_id;
    }

    /**
     * @param school_id the school_id to set
     */
    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    /**
     * @return the public_key
     */
    public String getPublic_key() {
        return public_key;
    }

    /**
     * @param public_key the public_key to set
     */
    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    /**
     * @return the public_identifier
     */
    public String getPublic_identifier() {
        return public_identifier;
    }

    /**
     * @param public_identifier the public_identifier to set
     */
    public void setPublic_identifier(String public_identifier) {
        this.public_identifier = public_identifier;
    }
}
