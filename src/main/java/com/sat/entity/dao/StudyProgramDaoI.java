/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import java.util.List;

/**
 *
 * @author Dean
 */
public interface StudyProgramDaoI {
    public StudyProgram addStudyProgram(StudyProgram studyProgram, User currentUser);
    public void editStudyProgram(StudyProgram studyProgram, User currentUser);
    public void deleteStudyProgram(int studyProgram_id, User currentUser);
    public StudyProgram find(int studyProgram_id, User currentUser);
    public List<StudyProgram> findAll(User currentUser); 
    public List<StudyProgram> findByIdnumber(String school_id, String idnumber);
}
