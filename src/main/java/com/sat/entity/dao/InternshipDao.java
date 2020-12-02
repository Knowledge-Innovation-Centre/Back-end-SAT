/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.Internship;
import com.sat.entity.StudyCompetence;
import com.sat.entity.User;
import com.sat.entity.mapper.InternshipsExtractor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dean
 */
@Repository
public class InternshipDao implements InternshipDaoI{
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Internship addInternship(final Internship internship, User currentUser) {
        
        KeyHolder keyHolder=new GeneratedKeyHolder();
        
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = "INSERT into INTERNSHIP_STUDYPROGRAMS_MAP (organization_id,study_program_id) values(?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"id"} );
                    stmt.setInt(1, internship.getOrganization().getId());
                    stmt.setInt(2, internship.getStudyProgram().getId());
                    return stmt;
                }
            },
            keyHolder);
        int new_id=keyHolder.getKey().intValue();
        if (internship.getStudyCompetence()!=null && internship.getStudyCompetence().size()>0) 
            addOrganizationsStudyprogramCompetences(internship.getStudyCompetence(),new_id);
        
        return find(new_id, currentUser);
    }
    
    private void addOrganizationsStudyprogramCompetences(final List<StudyCompetence> competences, final int internship_id){
        String sql1 = "INSERT into INTERNSHIP_STUDYPROGRAMS_COMPETENCES_MAP (internship_studyprograms_map_id,study_competences_id) values(?,?)";
        jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt, int i) throws SQLException {
                    stmt.setInt(1, internship_id);
                    stmt.setInt(2, competences.get(i).getId());
                }

                @Override
                public int getBatchSize() {
                    return competences.size();
                }
            }
        );
    }

    @Override
    public void editInternship(final Internship internship, User currentUser) {
        
        String sql1 = "UPDATE INTERNSHIP_STUDYPROGRAMS_MAP set organization_id=?,study_program_id=? WHERE id=?";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                   
                    stmt.setInt(1, internship.getOrganization().getId());
                    stmt.setInt(2, internship.getStudyProgram().getId());
                    stmt.setInt(3, internship.getId() );
                }
            }
        );
        
        String sql2 = "DELETE from INTERNSHIP_STUDYPROGRAMS_COMPETENCES_MAP where internship_studyprograms_map_id=? ";
        jdbcTemplate.update(sql2, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, internship.getId());
                }
            }
        );
        
        addOrganizationsStudyprogramCompetences(internship.getStudyCompetence(),internship.getId());
    }

    @Override
    public void deleteInternship(final Integer internship_id, User currentUser) {
        
        String sql0 = "DELETE from INTERNSHIP_STUDYPROGRAMS_COMPETENCES_MAP where internship_studyprograms_map_id=? ";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, internship_id);
                }
            }
        );
        
        String sql1 = "DELETE from INTERNSHIP_STUDYPROGRAMS_MAP where id=? ";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, internship_id);
                }
            }
        );
        
    }

    @Override
    public Internship find(Integer internship_id, User currentUser) {
        List<Internship> interns=new ArrayList<Internship>();
                
        interns = jdbcTemplate.query(
                
                " select  "+
                "         intern.id as intern_id, "+
                "         o.id as o_id, o.name as o_name, o.orgtype as o_orgtype, o.school_id as o_school_id, o.streetname as o_streetname, o.cityname as o_cityname, o.cityzipcode as o_cityzipcode, o.contact_username as o_contact_username, "+
                "         sp.id as sp_id, sp.name as sp_name, sp.description as sp_description, "+
                "         scomp.id as scomp_id, scomp.name as scomp_name, scomp.description as scomp_description "+
                " from INTERNSHIP_STUDYPROGRAMS_MAP as intern  "+
                " JOIN ORGANIZATION as o on (o.id = intern.organization_id) "+
                " JOIN STUDY_PROGRAMS as sp on (sp.id = intern.study_program_id and sp.school_id=o.school_id) "+
                " LEFT OUTER JOIN INTERNSHIP_STUDYPROGRAMS_COMPETENCES_MAP as ic ON (intern.id = ic.internship_studyprograms_map_id) "+
                " LEFT OUTER JOIN STUDY_COMPETENCES as scomp ON (scomp.id = ic.study_competences_id) "+
                " where o.school_id = ? and intern.id = ? ",
                new Object[] { currentUser.getSchool(), internship_id }, 
                new InternshipsExtractor()
            );
        if (interns!=null && interns.size()>0) return interns.get(0);
        return null;
    }

    @Override
    public List<Internship> findAllByStudyProgram(Integer studyprogram_id, User currentUser) {
        List<Internship> interns=new ArrayList<Internship>();
                
        interns = jdbcTemplate.query(
                
                " select  "+
                "         intern.id as intern_id, "+
                "         o.id as o_id, o.name as o_name, o.orgtype as o_orgtype, o.school_id as o_school_id, o.streetname as o_streetname, o.cityname as o_cityname, o.cityzipcode as o_cityzipcode, o.contact_username as o_contact_username, "+
                "         sp.id as sp_id, sp.name as sp_name, sp.description as sp_description, "+
                "         scomp.id as scomp_id, scomp.name as scomp_name, scomp.description as scomp_description "+
                " from INTERNSHIP_STUDYPROGRAMS_MAP as intern  "+
                " JOIN ORGANIZATION as o on (o.id = intern.organization_id) "+
                " JOIN STUDY_PROGRAMS as sp on (sp.id = intern.study_program_id and sp.school_id=o.school_id) "+
                " LEFT OUTER JOIN INTERNSHIP_STUDYPROGRAMS_COMPETENCES_MAP as ic ON (intern.id = ic.internship_studyprograms_map_id) "+
                " LEFT OUTER JOIN STUDY_COMPETENCES as scomp ON (scomp.id = ic.study_competences_id) "+
                " where o.school_id = ? and ( sp.id = ? or sp.study_program_id = ? )",
                new Object[] { currentUser.getSchool(), studyprogram_id, studyprogram_id }, 
                new InternshipsExtractor()
            );
        return interns;
    }

    @Override
    public List<Internship> findAll(User currentUser) {
        List<Internship> interns=new ArrayList<Internship>();
              
        String sql=" select  "+
                "         intern.id as intern_id, "+
                "         o.id as o_id, o.name as o_name, o.orgtype as o_orgtype, o.school_id as o_school_id, o.streetname as o_streetname, o.cityname as o_cityname, o.cityzipcode as o_cityzipcode, o.contact_username as o_contact_username, "+
                "         sp.id as sp_id, sp.name as sp_name, sp.description as sp_description, "+
                "         scomp.id as scomp_id, scomp.name as scomp_name, scomp.description as scomp_description "+
                " from INTERNSHIP_STUDYPROGRAMS_MAP as intern  "+
                " JOIN ORGANIZATION as o on (o.id = intern.organization_id) "+
                " JOIN STUDY_PROGRAMS as sp on (sp.id = intern.study_program_id and sp.school_id=o.school_id) "+
                " LEFT OUTER JOIN INTERNSHIP_STUDYPROGRAMS_COMPETENCES_MAP as ic ON (intern.id = ic.internship_studyprograms_map_id) "+
                " LEFT OUTER JOIN STUDY_COMPETENCES as scomp ON (scomp.id = ic.study_competences_id) "+
                " where o.school_id = ? ";
        interns = jdbcTemplate.query(
                
                sql,
                new Object[] { currentUser.getSchool() }, 
                new InternshipsExtractor()
            );
        return interns;
    }
    
}
