/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.Internship;
import com.sat.entity.User;
import java.util.List;

/**
 *
 * @author Dean
 */
public interface InternshipDaoI {
    public Internship addInternship(Internship internship, User currentUser);
    public void editInternship(Internship internship, User currentUser);
    public void deleteInternship(Integer internship_id, User currentUser);
    public Internship find(Integer internship_id, User currentUser);
    public List<Internship> findAllByStudyProgram(Integer studyprogram_id, User currentUser);
    public List<Internship> findAll(User currentUser);
}
