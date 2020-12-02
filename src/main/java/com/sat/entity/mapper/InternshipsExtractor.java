/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.Internship;
import com.sat.entity.Organization;
import com.sat.entity.StudyCompetence;
import com.sat.entity.StudyProgram;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author Dean
 */
public class InternshipsExtractor implements ResultSetExtractor<List<Internship>>  {

    @Override
    public List<Internship> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Hashtable<Integer, Internship> hasht = new Hashtable<Integer, Internship>();
        
        Integer internship_id;
        Internship i;
        Organization o;
        StudyProgram sp;
        List<StudyCompetence> sc_list;
        StudyCompetence sc;
        
        
        while (rs.next()){
            internship_id=rs.getInt("intern_id");
            i=hasht.get(internship_id);
            if (i==null) {i=new Internship();i.setId(internship_id);}
            
            o=i.getOrganization();
            if (o==null) o=new Organization();
            //o_id, o_name, o_orgtype, o_school_id, o_streetname, o_cityname, o_cityzipcode, o_contact_username
            o.setId(rs.getInt("o_id"));
            o.setName(rs.getString("o_name"));
            o.setOrgtype(rs.getString("o_orgtype"));
            o.setSchool_id(rs.getString("o_school_id"));
            o.setStreetname(rs.getString("o_streetname"));
            o.setCityname(rs.getString("o_cityname"));
            o.setCityzipcode(rs.getString("o_cityzipcode"));
            o.setContact_username(rs.getString("o_contact_username"));
            i.setOrganization(o);
            
            sp=i.getStudyProgram();
            if (sp==null) sp=new StudyProgram();
            //sp_id, sp_name, sp_description
            sp.setId(rs.getInt("sp_id"));
            sp.setName(rs.getString("sp_name"));
            sp.setDescription(rs.getString("sp_description"));
            i.setStudyProgram(sp);
            
            sc_list=i.getStudyCompetence();
            if (sc_list==null) sc_list=new ArrayList<StudyCompetence>();
            
            sc=new StudyCompetence();
            //scomp_id, scomp_name, scomp_description,
            sc.setId(rs.getInt("scomp_id"));
            sc.setName(rs.getString("scomp_name"));
            sc.setDescription(rs.getString("scomp_description"));
            sc_list.add(sc);
            i.setStudyCompetence(sc_list);
            
            hasht.put(internship_id, i);
        }
        
        List<Internship> internships=new ArrayList<Internship>();
        Enumeration enu=hasht.elements();
        while(enu.hasMoreElements()){
            internships.add((Internship)enu.nextElement());
        }
            
        return internships;
    }

   
    
}
