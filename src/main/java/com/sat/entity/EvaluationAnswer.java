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
public class EvaluationAnswer {
    private Integer id;
    private EvaluationQuestion question;
    private InternshipAgreement internship_agreement;
    private String answer;
    private String userid;

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
    public EvaluationQuestion getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(EvaluationQuestion question) {
        this.question = question;
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
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @param answer the answer to set
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }
}

/*
Create table EVALUATION_ANSWERS(
	id INT NOT NULL AUTO_INCREMENT,
        question_id INT NOT NULL,
        internship_agreement_id INT NULL,
        answer VARCHAR(256) NULL,
        userid NVARCHAR(64) not null,
	CONSTRAINT EVALUATIONANSWERS_PK PRIMARY KEY (id),
        CONSTRAINT EVALUATIONANSWERS_EVALUATIONQUESTIONS_FK FOREIGN KEY (question_id) REFERENCES EVALUATION_QUESTIONS(id),
	CONSTRAINT EVALUATIONANSWERS_INTERNSHIPAGREEMENT_FK FOREIGN KEY (internship_agreement_id) REFERENCES INTERNSHIP_AGREEMENT(id)
);
*/
