/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.School;
import java.util.List;

/**
 *
 * @author Dean
 */
public interface SchoolDaoI {
    public void addSchool(School school);
    public void editSchool(School school, String new_school_id);
    public void deleteSchool(String school_id);
    public School find(String school_id);
    public List<School> findAll();
}