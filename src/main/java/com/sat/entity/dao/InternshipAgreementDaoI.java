/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.Grades;
import com.sat.entity.Internship;
import com.sat.entity.InternshipAgreement;
import com.sat.entity.User;
import java.util.List;

/**
 *
 * @author Dean
 */
public interface InternshipAgreementDaoI {
    public int addInternshipAgreement(InternshipAgreement internshipAgreement, User currentUser);
    public void editInternshipAgreement(InternshipAgreement internshipAgreement, User currentUser);
    public void deleteInternshipAgreement(Integer internshipAgreement_id, User currentUser);
    public InternshipAgreement find(Integer internshipAgreement_id, User currentUser);
    public List<InternshipAgreement> findAll(User currentUser, boolean unsigned_only, String student_username, Integer organization_id, Integer internship_agreement_id, String mentor_username);
    
    public void studentSign(User currentUser, Integer internshipAgreement_id);
    public void organizationSign(User currentUser, Integer internshipAgreement_id);
    public void schoolSign(User currentUser, Integer internshipAgreement_id);
    
    public InternshipAgreement applyForInternship(User currentUser, Integer internship_id);
    
    public List<Grades> getGrades(User currentUser, Integer internshipAgreement_id);
    public List<Grades> storeGrades(User currentUser, List<Grades> grades);
}
