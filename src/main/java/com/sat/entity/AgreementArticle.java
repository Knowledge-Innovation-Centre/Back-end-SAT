/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

/**
 *
 * @author Dean
 * 	id INT NOT NULL AUTO_INCREMENT,
	school_id NVARCHAR(64) NOT NULL,
	ordnum INT NOT NULL default 9999,
	content NVARCHAR(512) NOT NULL,
 */
public class AgreementArticle {

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
     * @return the ordnum
     */
    public int getOrdnum() {
        return ordnum;
    }

    /**
     * @param ordnum the ordnum to set
     */
    public void setOrdnum(int ordnum) {
        this.ordnum = ordnum;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    private int id;
    private String school_id;
    private int ordnum;
    private String content;
}
