/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.IdHolder;
import com.sat.entity.StudyModule;
import com.sat.entity.StudyProgram;
import com.sat.entity.StudyCompetence;
import com.sat.entity.User;
import com.sat.entity.mapper.IdHolderExtractor;
import com.sat.entity.mapper.StudyProgramExtractor;
import com.sat.entity.mapper.StudyProgramsExtractor;
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
public class StudyProgramDao implements StudyProgramDaoI{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public StudyProgram addStudyProgram(final StudyProgram studyProgram, final User currentUser) {
        
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = "INSERT into STUDY_PROGRAMS (name,description,school_id, idnumber) values(?,?,?, ?)";
                    PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"} );
                    if (studyProgram.getName()!=null) ps.setString(1, studyProgram.getName()); else ps.setNull(1,Types.VARCHAR);
                    if (studyProgram.getDescription()!=null) ps.setString(2, studyProgram.getDescription()); else ps.setNull(2, Types.VARCHAR);
                    ps.setString(3, currentUser.getSchool());
                    ps.setString(4, studyProgram.getIdnumber());
                    return ps;
                }
            },
            keyHolder);
        final int new_sp_id=keyHolder.getKey().intValue();
        
        String sql1 = "INSERT into STUDY_COMPETENCES (study_program_id,name,description) values(?,?,?)";
        jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt, int i) throws SQLException {
                    stmt.setInt(1, new_sp_id );
                    if (studyProgram.getStudyCompetences().get(i).getName()!=null) stmt.setString(2, studyProgram.getStudyCompetences().get(i).getName()); else stmt.setNull(2,Types.VARCHAR);
                    if (studyProgram.getStudyCompetences().get(i).getDescription()!=null) stmt.setString(3, studyProgram.getStudyCompetences().get(i).getDescription()  ); else stmt.setNull(3, Types.VARCHAR);
                }

                @Override
                public int getBatchSize() {
                    return studyProgram.getStudyCompetences().size();
                }
            }
        );
        
        for(int i=0;i<studyProgram.getStudyModules().size();i++){
            addStudyModule(studyProgram.getStudyModules().get(i), currentUser, new_sp_id);
        }
        
        /*
        String sql1 = "INSERT into STUDY_PROGRAMS (study_program_id,name,description,school_id) values(?,?,?,?)";
        jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt, int i) throws SQLException {
                    stmt.setInt(1, new_sp_id );
                    stmt.setString(2, studyProgram.getStudyModules().get(i).getName());
                    stmt.setString(3, studyProgram.getStudyModules().get(i).getDescription()  );
                    stmt.setString(4, currentUser.getSchool());
                }

                @Override
                public int getBatchSize() {
                    return studyProgram.getStudyModules().size();
                }
            }
        );*/
        
        return find(new_sp_id, currentUser);
    }
    
    private void addStudyModule(final StudyModule module,final User currentUser, final int program_id){
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = "INSERT into STUDY_PROGRAMS (study_program_id,name,description,school_id) values(?,?,?,?)";
                    PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"} );
                    ps.setInt   (1, program_id);
                    if (module.getName()!=null) ps.setString(2, module.getName()); else ps.setNull(2, Types.VARCHAR);
                    if (module.getDescription()!=null) ps.setString(3, module.getDescription()); else ps.setNull(3,Types.VARCHAR);
                    ps.setString(4, currentUser.getSchool());
                    return ps;
                }
            },
            keyHolder);
        final int new_sm_id=keyHolder.getKey().intValue();
        
        String sql1 = "INSERT into STUDY_COMPETENCES (study_program_id,name,description) values(?,?,?)";
        jdbcTemplate.batchUpdate(sql1, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt, int i) throws SQLException {
                    stmt.setInt(1, new_sm_id );
                    if (module.getStudyCompetences().get(i).getName()!=null)stmt.setString(2, module.getStudyCompetences().get(i).getName()); else stmt.setNull(2,Types.VARCHAR);
                    if (module.getStudyCompetences().get(i).getDescription()!=null) stmt.setString(3, module.getStudyCompetences().get(i).getDescription()  ); else stmt.setNull(3,Types.VARCHAR);
                }

                @Override
                public int getBatchSize() {
                    return module.getStudyCompetences().size();
                }
            }
        );
    }

    @Override
    public void editStudyProgram(final StudyProgram studyProgram, final User currentUser) {
        String sql = "UPDATE STUDY_PROGRAMS set name=?, description=?, school_id=? ,idnumber=? where id=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                   
                    if (studyProgram.getName()!=null) stmt.setString(1, studyProgram.getName() ); else stmt.setNull(1,Types.VARCHAR);
                    if (studyProgram.getDescription()!=null) stmt.setString(2, studyProgram.getDescription() );else stmt.setNull(2,Types.VARCHAR);
                    stmt.setString(3, currentUser.getSchool()  );
                    stmt.setString(4, studyProgram.getIdnumber()  );
                    stmt.setInt   (5, studyProgram.getId()   );
                }
            }
        );
        
        //fetch current ID's for study modules (need to know which ones are abandoned and need to be deleted
        String sql_modules="select id from STUDY_PROGRAMS where study_program_id=? and school_id=? ";
        List<IdHolder> ids=new ArrayList<IdHolder>();
        ids=jdbcTemplate.query(
                sql_modules,
                new Object[] {studyProgram.getId(),currentUser.getSchool()}, 
                new IdHolderExtractor()
        );
        //remove survivors (rest is to be deleted)
        for(int i=0;i<studyProgram.getStudyModules().size();i++){
            int survivor_id=studyProgram.getStudyModules().get(i).getId();
            for (int j=ids.size()-1;j>=0;j--){
                if (ids.get(j).getId()==survivor_id){
                    ids.remove(j);
                    break;
                }
            }
        }
        //delete abandoned modules
        for (int j=ids.size()-1;j>=0;j--){
            deleteStudyModule(ids.get(j).getId(), currentUser);
        }
        
        // and update surviving ones
        for(int i=0;i<studyProgram.getStudyModules().size();i++){
            editStudyModule(studyProgram.getStudyModules().get(i), studyProgram.getId(), currentUser);
        }
        
        //fetch current ID's for study modules (need to know which ones are abandoned and need to be deleted
        String sql_competences="select id from STUDY_COMPETENCES where study_program_id=?";
        List<IdHolder> cids_todelete=new ArrayList<IdHolder>();
        cids_todelete=jdbcTemplate.query(
                sql_competences,
                new Object[] {studyProgram.getId()}, 
                new IdHolderExtractor()
        );
        List<StudyCompetence> scs=studyProgram.getStudyCompetences();
        for (int i=0;i<scs.size();i++){
            final StudyCompetence sc=scs.get(i);
            if (sc.getId()==0){
                // insert
                String sqlM = "INSERT into STUDY_COMPETENCES (name,description,study_program_id) values(?,?,?)";
                jdbcTemplate.update(sqlM, new PreparedStatementSetter(){
                        public void setValues(PreparedStatement stmt) throws SQLException{
                            if (sc.getName()!=null)        stmt.setString(1, sc.getName());        else stmt.setNull(1, Types.VARCHAR);
                            if (sc.getDescription()!=null) stmt.setString(2, sc.getDescription()); else stmt.setNull(1, Types.VARCHAR);
                            stmt.setInt(3, studyProgram.getId());
                        }
                    }
                );
                
            } else {
                // update
                IdHolder id=new IdHolder();
                id.setId(sc.getId());
                //cids_todelete.remove(id);
                for(int j=cids_todelete.size()-1;j>=0;j--){
                    IdHolder idx=cids_todelete.get(j);
                    if (id.getId()==idx.getId()){
                        cids_todelete.remove(j);
                    }
                }
                
                // STUDY_COMPETENCES (study_program_id,name,description) values(?,?,?)
                String sqlU = "UPDATE STUDY_COMPETENCES set name=?, description=?, study_program_id=? where id=?";
                jdbcTemplate.update(sqlU, new PreparedStatementSetter(){
                        public void setValues(PreparedStatement stmt) throws SQLException{
                            if (sc.getName()!=null)        stmt.setString(1, sc.getName());        else stmt.setNull(1, Types.VARCHAR);
                            if (sc.getDescription()!=null) stmt.setString(2, sc.getDescription()); else stmt.setNull(1, Types.VARCHAR);
                            stmt.setInt(3, studyProgram.getId());
                            stmt.setInt(4, sc.getId());
                        }
                    }
                );
                
                
            }
            
        }
        final List<IdHolder> cidstodelete=cids_todelete;
        String sqlM = "DELETE from STUDY_COMPETENCES where id=? and NOT EXISTS (select 1 from INTERNSHIPAGREEMENT_STUDYCOMPETENCES_MAP MM where MM.study_competences_id=STUDY_COMPETENCES.id)";
            jdbcTemplate.batchUpdate(sqlM, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement stmt, int i) throws SQLException {
                        stmt.setInt(1, cidstodelete.get(i).getId() );
                    }

                    @Override
                    public int getBatchSize() {
                        return cidstodelete.size();
                    }
                }
            );
        
    }
    
    private void editStudyModule(final StudyModule module, final int program_id, final User currentUser){
        if (module.getId()==0){
            // insert
            addStudyModule(module,currentUser,program_id);
            /*String sqlI = "INSERT into STUDY_PROGRAMS (study_program_id,name,description,school_id) values(?,?,?,?)";
            jdbcTemplate.update(sqlI, new PreparedStatementSetter(){
                    public void setValues(PreparedStatement stmt) throws SQLException{

                        stmt.setInt   (1, program_id  );
                        stmt.setString(2, module.getName()   );
                        stmt.setString(3, module.getDescription()   );
                        stmt.setString(4, currentUser.getSchool() );
                    }
                }
            );*/
        } else {
            // update
            String sqlU = "UPDATE STUDY_PROGRAMS set name=?, description=?, school_id=?, study_program_id=? where id=?";
            jdbcTemplate.update(sqlU, new PreparedStatementSetter(){
                    public void setValues(PreparedStatement stmt) throws SQLException{

                        if (module.getName()!=null) stmt.setString(1, module.getName() ); else stmt.setNull(1,Types.VARCHAR);
                        if (module.getDescription()!=null) stmt.setString(2, module.getDescription() );else stmt.setNull(2,Types.VARCHAR);
                        stmt.setString(3, currentUser.getSchool()  );
                        stmt.setInt   (4, program_id   );
                        stmt.setInt   (5, module.getId()   );
                    }
                }
            );
            
            String sqlD = "DELETE from STUDY_COMPETENCES where study_program_id in (select sm.id from STUDY_PROGRAMS sm where sm.id=? and sm.school_id=? and sm.study_program_id is not null)";
            jdbcTemplate.update(sqlD, new PreparedStatementSetter(){
                    public void setValues(PreparedStatement stmt) throws SQLException{
                        stmt.setInt(1, module.getId());
                        stmt.setString(2, currentUser.getSchool());
                    }
                }
            );
            
            String sqlM = "INSERT into STUDY_COMPETENCES (study_program_id,name,description) values(?,?,?)";
            jdbcTemplate.batchUpdate(sqlM, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement stmt, int i) throws SQLException {
                        stmt.setInt(1, module.getId() );
                        if (module.getStudyCompetences().get(i).getName()!=null) stmt.setString(2, module.getStudyCompetences().get(i).getName()); else stmt.setNull(2,Types.VARCHAR);
                        if (module.getStudyCompetences().get(i).getDescription()!=null) stmt.setString(3, module.getStudyCompetences().get(i).getDescription()  ); else stmt.setNull(2,Types.VARCHAR);
                    }

                    @Override
                    public int getBatchSize() {
                        return module.getStudyCompetences().size();
                    }
                }
            );
        }
    }
    
    private void deleteStudyModule(final int module_id, final User currentUser){
        
        String sql0 = "DELETE from STUDY_COMPETENCES where study_program_id in (select sm.id from STUDY_PROGRAMS sm where sm.id=? and sm.school_id=? and sm.study_program_id is not null)";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, module_id);
                    stmt.setString(2, currentUser.getSchool());
                }
            }
        );
        
        String sql3 = "DELETE from STUDY_PROGRAMS where id=? and school_id=? and study_program_id is not null";
        jdbcTemplate.update(sql3, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                   
                    stmt.setInt(1, module_id);
                    stmt.setString(2, currentUser.getSchool());
                }
            }
        );
    }

    @Override
    public void deleteStudyProgram(final int studyProgram_id, final User currentUser) {

        String sql0 = "DELETE from STUDY_COMPETENCES where study_program_id in (select sp.id from STUDY_PROGRAMS sp where sp.study_program_id=? and sp.school_id=?)";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, studyProgram_id);
                    stmt.setString(2, currentUser.getSchool());
                }
            }
        );
        
        String sql1 = "DELETE from STUDY_PROGRAMS where study_program_id=? and school_id=?";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                   
                    stmt.setInt(1, studyProgram_id);
                    stmt.setString(2, currentUser.getSchool());
                }
            }
        );
        
        String sql2 = "DELETE from STUDY_COMPETENCES where study_program_id in (select sp.id from STUDY_PROGRAMS sp where sp.id=? and sp.school_id=?)";
        jdbcTemplate.update(sql2, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, studyProgram_id);
                    stmt.setString(2, currentUser.getSchool());
                }
            }
        );
        
        String sql3 = "DELETE from STUDY_PROGRAMS where id=? and school_id=?";
        jdbcTemplate.update(sql3, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                   
                    stmt.setInt(1, studyProgram_id);
                    stmt.setString(2, currentUser.getSchool());
                }
            }
        );
    }

    @Override
    public StudyProgram find(int studyProgram_id, final User currentUser) {
        String sql = 
                
                " SELECT  "+
                "       sp.id, sp.name, sp.description, sp.idnumber, "+
                "            scp.id as scpid, scp.name as scpname, scp.description as scpdescription, "+
                "       sm.id as mid, sm.name as mname,sm.description as mdescription, "+
                "            scm.id as scmid, scm.name as scmname, scm.description as scmdescription "+
                " from STUDY_PROGRAMS sp "+
                " LEFT OUTER JOIN STUDY_PROGRAMS sm ON (sp.id=sm.study_program_id) "+
                " LEFT OUTER JOIN STUDY_COMPETENCES scp ON (sp.id=scp.study_program_id) "+
                " LEFT OUTER JOIN STUDY_COMPETENCES scm ON (sm.id=scm.study_program_id) "+
                " where sp.id=? and sp.study_program_id is null and sp.school_id=? ";
        
        StudyProgram usrs=(StudyProgram)jdbcTemplate.query(
                sql,
                new Object[] { Integer.valueOf(studyProgram_id), currentUser.getSchool()}, 
                new StudyProgramExtractor()
        );
                
        return usrs;
    }

    @Override
    public List<StudyProgram> findAll(final User currentUser) {
        List<StudyProgram> usrs=new ArrayList<StudyProgram>();
        
        String sql = 
                
                " SELECT "+
                "       sp.id, sp.name,sp.description, sp.idnumber, "+
                "            scp.id as scpid, scp.name as scpname, scp.description as scpdescription, "+
                "       sm.id as mid, sm.name as mname,sm.description as mdescription, "+
                "            scm.id as scmid, scm.name as scmname, scm.description as scmdescription "+
                " FROM STUDY_PROGRAMS sp "+
                " LEFT OUTER JOIN STUDY_PROGRAMS sm ON (sp.id=sm.study_program_id) "+
                " LEFT OUTER JOIN STUDY_COMPETENCES scp ON (sp.id=scp.study_program_id) "+
                " LEFT OUTER JOIN STUDY_COMPETENCES scm ON (sm.id=scm.study_program_id) "+
                " where sp.study_program_id is null and sp.school_id=? ";
        
        usrs=jdbcTemplate.query(
                sql,
                new Object[] {currentUser.getSchool()}, 
                new StudyProgramsExtractor()
        );
            
        return usrs;
    }
    @Override
    public List<StudyProgram> findByIdnumber(String school_id, String idnumber) {
        List<StudyProgram> usrs=new ArrayList<StudyProgram>();
        
        String sql = 
                
                " SELECT "+
                "       sp.id, sp.name,sp.description, sp.idnumber, "+
                "            scp.id as scpid, scp.name as scpname, scp.description as scpdescription, "+
                "       sm.id as mid, sm.name as mname,sm.description as mdescription, "+
                "            scm.id as scmid, scm.name as scmname, scm.description as scmdescription "+
                " FROM STUDY_PROGRAMS sp "+
                " LEFT OUTER JOIN STUDY_PROGRAMS sm ON (sp.id=sm.study_program_id) "+
                " LEFT OUTER JOIN STUDY_COMPETENCES scp ON (sp.id=scp.study_program_id) "+
                " LEFT OUTER JOIN STUDY_COMPETENCES scm ON (sm.id=scm.study_program_id) "+
                " where sp.study_program_id is null and sp.school_id=? and sp.idnumber=? ";
        
        usrs=jdbcTemplate.query(
                sql,
                new Object[] {school_id, idnumber}, 
                new StudyProgramsExtractor()
        );
            
        return usrs;
    }
}

