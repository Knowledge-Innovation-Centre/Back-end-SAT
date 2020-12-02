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
 * @author Dean
 * 
 * id` int(11) NOT NULL AUTO_INCREMENT,
  `organization_id` int(11) NOT NULL,
  `student_username` varchar(64) CHARACTER SET utf8 NOT NULL,
  `period_begin` date NOT NULL,
  `period_end` date NOT NULL,
  `study_program_id` int(11) NOT NULL,
  `school_id` varchar(64) CHARACTER SET utf8 NOT NULL,
  `version_num` int(11) DEFAULT '-1',
 */
public class InternshipAgreement {
    private int id;
    private Organization organization;
    private User student;
    private Date periodBegin;
    private Date periodEnd;
    private StudyProgram studyProgram;
    private String school_id;
    private int version_num;
    private List<AgreementArticle> articles;
    private List<StudyCompetence> competences;
    
    private String signed_student_username;
    private Date signed_student_date;
    private String signed_school_username;
    private Date signed_school_date;
    private String signed_organization_username;
    private Date signed_organization_date;
    
    private User mentor;
    
    private Integer grade_exam;
    private Integer grade_final;
    private Date    grade_date;
    
    private User school_organizer;
    private String document_number;
    
    private Integer ects_points;
    private Integer sum_of_hours;

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
     * @return the student
     */
    public User getStudent() {
        return student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(User student) {
        this.student = student;
    }

    /**
     * @return the periodBegin
     */
    public Date getPeriodBegin() {
        return periodBegin;
    }

    /**
     * @param periodBegin the periodBegin to set
     */
    public void setPeriodBegin(Date periodBegin) {
        this.periodBegin = periodBegin;
    }

    /**
     * @return the periodEnd
     */
    public Date getPeriodEnd() {
        return periodEnd;
    }

    /**
     * @param periodEnd the periodEnd to set
     */
    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
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
     * @return the version_num
     */
    public int getVersion_num() {
        return version_num;
    }

    /**
     * @param version_num the version_num to set
     */
    public void setVersion_num(int version_num) {
        this.version_num = version_num;
    }

    /**
     * @return the articles
     */
    public List<AgreementArticle> getArticles() {
        return articles;
    }

    /**
     * @param articles the articles to set
     */
    public void setArticles(List<AgreementArticle> articles) {
        this.articles = articles;
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
     * @return the signed_student_date
     */
    public Date getSigned_student_date() {
        return signed_student_date;
    }

    /**
     * @param signed_student_date the signed_student_date to set
     */
    public void setSigned_student_date(Date signed_student_date) {
        this.signed_student_date = signed_student_date;
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
     * @return the signed_school_date
     */
    public Date getSigned_school_date() {
        return signed_school_date;
    }

    /**
     * @param signed_school_date the signed_school_date to set
     */
    public void setSigned_school_date(Date signed_school_date) {
        this.signed_school_date = signed_school_date;
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

    /**
     * @return the signed_organization_date
     */
    public Date getSigned_organization_date() {
        return signed_organization_date;
    }

    /**
     * @param signed_organization_date the signed_organization_date to set
     */
    public void setSigned_organization_date(Date signed_organization_date) {
        this.signed_organization_date = signed_organization_date;
    }

    /**
     * @return the competences
     */
    public List<StudyCompetence> getCompetences() {
        return competences;
    }

    /**
     * @param competences the competences to set
     */
    public void setCompetences(List<StudyCompetence> competences) {
        this.competences = competences;
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
     * @return the grade_exam
     */
    public Integer getGrade_exam() {
        return grade_exam;
    }

    /**
     * @param grade_exam the grade_exam to set
     */
    public void setGrade_exam(Integer grade_exam) {
        this.grade_exam = grade_exam;
    }

    /**
     * @return the grade_final
     */
    public Integer getGrade_final() {
        return grade_final;
    }

    /**
     * @param grade_final the grade_final to set
     */
    public void setGrade_final(Integer grade_final) {
        this.grade_final = grade_final;
    }

    /**
     * @return the grade_date
     */
    public Date getGrade_date() {
        return grade_date;
    }

    /**
     * @param grade_date the grade_date to set
     */
    public void setGrade_date(Date grade_date) {
        this.grade_date = grade_date;
    }

    /**
     * @return the school_organizer
     */
    public User getSchool_organizer() {
        return school_organizer;
    }

    /**
     * @param school_organizer the school_organizer to set
     */
    public void setSchool_organizer(User school_organizer) {
        this.school_organizer = school_organizer;
    }

    /**
     * @return the document_number
     */
    public String getDocument_number() {
        return document_number;
    }

    /**
     * @param document_number the document_number to set
     */
    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    /**
     * @return the ects_points
     */
    public Integer getEcts_points() {
        return ects_points;
    }

    /**
     * @param ects_points the ects_points to set
     */
    public void setEcts_points(Integer ects_points) {
        this.ects_points = ects_points;
    }

    /**
     * @return the sum_of_hours
     */
    public Integer getSum_of_hours() {
        return sum_of_hours;
    }

    /**
     * @param sum_of_hours the sum_of_hours to set
     */
    public void setSum_of_hours(Integer sum_of_hours) {
        this.sum_of_hours = sum_of_hours;
    }
    
    
}
