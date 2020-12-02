/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

/**
 *
 * @author dean
 */
public class EvaluationQuestion {
    private Integer id;
    private String question;
    private String options;
    private Integer show_if;
    private String show_if_value;
    private Integer ordnum;
    
    private String question_for;
    
    private String school_id;

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
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return the options
     */
    public String getOptions() {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * @return the show_if
     */
    public Integer getShow_if() {
        return show_if;
    }

    /**
     * @param show_if the show_if to set
     */
    public void setShow_if(Integer show_if) {
        this.show_if = show_if;
    }

    /**
     * @return the show_if_value
     */
    public String getShow_if_value() {
        return show_if_value;
    }

    /**
     * @param show_if_value the show_if_value to set
     */
    public void setShow_if_value(String show_if_value) {
        this.show_if_value = show_if_value;
    }

    /**
     * @return the ordnum
     */
    public Integer getOrdnum() {
        return ordnum;
    }

    /**
     * @param ordnum the ordnum to set
     */
    public void setOrdnum(Integer ordnum) {
        this.ordnum = ordnum;
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
     * @return the question_for
     */
    public String getQuestion_for() {
        return question_for;
    }

    /**
     * @param question_for the question_for to set
     */
    public void setQuestion_for(String question_for) {
        this.question_for = question_for;
    }
}
/*
Create table EVALUATION_QUESTIONS(
	id INT NOT NULL AUTO_INCREMENT,
        question VARCHAR(128) NULL,
        options VARCHAR(256) NULL,
        show_if INT NULL,
        show_if_value VARCHAR(128) NULL,
        question_for VARCHAR(64) NULL,
        ordnum INT NULL,
	CONSTRAINT EVALUATIONQUESTIONS_PK PRIMARY KEY (id)
);

*/
