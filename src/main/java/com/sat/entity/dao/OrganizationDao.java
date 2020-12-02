/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.IdHolder;
import com.sat.entity.Organization;
import com.sat.entity.StudyProgram;
import com.sat.entity.User;
import com.sat.entity.mapper.IdHolderExtractor;
import com.sat.entity.mapper.OrganizationExtractor;
import com.sat.entity.mapper.OrganizationsExtractor;
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
import org.springframework.stereotype.Service;

/**
 *
 * @author Dean
 * 
 *   `id` int(11) NOT NULL AUTO_INCREMENT,
  `orgtype` varchar(64) CHARACTER SET utf8 NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 NOT NULL,
  `cityname` varchar(128) CHARACTER SET utf8 NOT NULL,
  `cityzipcode` varchar(64) CHARACTER SET utf8 NOT NULL,
  `streetname` varchar(256) CHARACTER SET utf8 NOT NULL,
  `contact_username` varchar(64) CHARACTER SET utf8 NULL,
  `school_id` varchar(64) CHARACTER SET utf8 NOT NULL,
 * 
 */
@Repository
public class OrganizationDao implements OrganizationDaoI{
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    private String getOrgType(String orgtype){
        String ret="ORGANIZATION";
        if ("SCHOOL".equalsIgnoreCase(orgtype)) ret="SCHOOL";
        
        return ret;
    }
    @Override
    public Organization addOrganization(final Organization org, final User currentUser) {
        
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = "INSERT into ORGANIZATION (orgtype,name,cityname,cityzipcode,streetname,contact_username,school_id) values(?,?,?,?,?,?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"id"} );
                    stmt.setString(1, getOrgType(org.getOrgtype()));
                    if (org.getName()!=null) stmt.setString(2, org.getName()); else stmt.setNull(2,Types.VARCHAR);
                    if (org.getCityname()!=null) stmt.setString(3,org.getCityname()); else stmt.setNull(3,Types.VARCHAR);
                    if (org.getCityzipcode()!=null) stmt.setString(4,org.getCityzipcode()); else stmt.setNull(4,Types.VARCHAR);
                    if (org.getStreetname()!=null) stmt.setString(5,org.getStreetname()); else stmt.setNull(5,Types.VARCHAR);
                    if (org.getContact_username()==null)
                        stmt.setNull(6, Types.VARCHAR);
                    else
                        stmt.setString(6,org.getContact_username());
                    stmt.setString(7,currentUser.getSchool());
                    return stmt;
                }
            },
            keyHolder);
        int new_org_id=keyHolder.getKey().intValue();
        addOrganizationsStudyprograms(org.getStudyPrograms(),new_org_id);
        return find(new_org_id, currentUser);
    }
    
    private void addOrganizationsStudyprograms(final List<StudyProgram> programs, final int organization_id){
        // String sql1 = "INSERT into INTERNSHIP_STUDYPROGRAMS_MAP (study_program_id,organization_id) values(?,?)";
        jdbcTemplate.batchUpdate(
                " INSERT into INTERNSHIP_STUDYPROGRAMS_MAP (study_program_id,organization_id)"+
                "    select * from (select ?,?) as x "+
                "    where NOT exists(select 1 from INTERNSHIP_STUDYPROGRAMS_MAP where study_program_id=? and organization_id=?) ",
                new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement stmt, int i) throws SQLException {
                    stmt.setInt(1, programs.get(i).getId() );
                    stmt.setInt(2, organization_id );
                    stmt.setInt(3, programs.get(i).getId() );
                    stmt.setInt(4, organization_id );
                }

                @Override
                public int getBatchSize() {
                    return programs.size();
                }
            }
        );
    }

    @Override
    public void editOrganization(final Organization org, final User currentUser) {
        String sql1 = "UPDATE ORGANIZATION set orgtype=?,name=?,cityname=?,cityzipcode=?,streetname=?,contact_username=?,school_id=? where id=? and school_id=?";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                   
                    stmt.setString(1, getOrgType(org.getOrgtype()));
                    if (org.getName()!=null) stmt.setString(2, org.getName()); else stmt.setNull(2,Types.VARCHAR);
                    if (org.getCityname()!=null) stmt.setString(3,org.getCityname()); else stmt.setNull(3,Types.VARCHAR);
                    if (org.getCityzipcode()!=null) stmt.setString(4,org.getCityzipcode()); else stmt.setNull(4,Types.VARCHAR);
                    if (org.getStreetname()!=null) stmt.setString(5,org.getStreetname()); else stmt.setNull(5,Types.VARCHAR);
                    if (org.getContact_username()==null)
                        stmt.setNull(6, Types.VARCHAR);
                    else
                        stmt.setString(6, org.getContact_username());
                    stmt.setString(7, currentUser.getSchool());
                    stmt.setInt(8, org.getId() );
                    
                    stmt.setString(9,  currentUser.getSchool());
                }
            }
        );
        
        List<IdHolder> ids = jdbcTemplate.query(
            " SELECT study_program_id as id from INTERNSHIP_STUDYPROGRAMS_MAP where organization_id=? ",
            new Object[] { org.getId() }, 
            new IdHolderExtractor()
        );
        for (int i=0;i<ids.size();i++){
            final int sp_id=ids.get(i).getId();
            try{
                jdbcTemplate.update("DELETE from INTERNSHIP_STUDYPROGRAMS_MAP where organization_id=? and study_program_id=?", 
                        new PreparedStatementSetter(){
                        public void setValues(PreparedStatement stmt) throws SQLException{
                            stmt.setInt(1, org.getId());
                            stmt.setInt(2, sp_id);
                        }
                    }
                );
            }catch(Exception e){;}
        }
        addOrganizationsStudyprograms(org.getStudyPrograms(),org.getId());
        
        
        
        /*
        String sql2 = "DELETE from INTERNSHIP_STUDYPROGRAMS_MAP where organization_id=? ";
        jdbcTemplate.update(sql2, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, org.getId());
                }
            }
        );
        
        addOrganizationsStudyprograms(org.getStudyPrograms(),org.getId());
        */
    }

    @Override
    public void deleteOrganization(final Integer organization_id, final User currentUser) {
        String sql0 = "DELETE from INTERNSHIP_STUDYPROGRAMS_MAP where organization_id=? ";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setInt(1, organization_id);
                }
            }
        );
        
        String sql1 = "DELETE from ORGANIZATION where id=? and school_id=?";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                   
                    stmt.setInt(1, organization_id);
                    stmt.setString(2, currentUser.getSchool());
                }
            }
        );
    }

    @Override
    public Organization find(final Integer organization_id, final User currentUser) {
        Organization org=null;

        org = (Organization) jdbcTemplate.query(
            " SELECT "+
            "      o.id,o.orgtype,o.name,o.cityname,o.cityzipcode,o.streetname,o.contact_username,o.school_id, "+
            "      sp.id as sp_id, sp.name as sp_name, sp.description as sp_description"+
            " from ORGANIZATION as o "+
            " LEFT OUTER JOIN INTERNSHIP_STUDYPROGRAMS_MAP as isp on (isp.organization_id=o.id) "+
            " LEFT OUTER JOIN STUDY_PROGRAMS as sp on (sp.id=isp.study_program_id)"+
            " where o.id = ? and o.school_id=? ",
            new Object[] { organization_id, currentUser.getSchool() }, 
            new OrganizationExtractor()
        );

        return org;
    }

    @Override
    public List<Organization> findAll(final User currentUser, String organization_type) {
        List<Organization> orgs=new ArrayList<Organization>();

        if (organization_type==null){
            orgs = jdbcTemplate.query(
                " SELECT "+
                "      o.id,o.orgtype,o.name,o.cityname,o.cityzipcode,o.streetname,o.contact_username,o.school_id, "+
                "      sp.id as sp_id, sp.name as sp_name, sp.description as sp_description"+
                " from ORGANIZATION as o "+
                " LEFT OUTER JOIN INTERNSHIP_STUDYPROGRAMS_MAP as isp on (isp.organization_id=o.id) "+
                " LEFT OUTER JOIN STUDY_PROGRAMS as sp on (sp.id=isp.study_program_id)"+
                " where o.school_id=? ",
                new Object[] { currentUser.getSchool() }, 
                new OrganizationsExtractor()
            );
        } else {
            orgs = jdbcTemplate.query(
                " SELECT "+
                "      o.id,o.orgtype,o.name,o.cityname,o.cityzipcode,o.streetname,o.contact_username,o.school_id, "+
                "      sp.id as sp_id, sp.name as sp_name, sp.description as sp_description"+
                " from ORGANIZATION as o "+
                " LEFT OUTER JOIN INTERNSHIP_STUDYPROGRAMS_MAP as isp on (isp.organization_id=o.id) "+
                " LEFT OUTER JOIN STUDY_PROGRAMS as sp on (sp.id=isp.study_program_id)"+
                " where o.school_id=? and o.orgtype=? ",
                new Object[] { currentUser.getSchool(), organization_type }, 
                new OrganizationsExtractor()
            );
        }
        

        return orgs;
    }
    
}
