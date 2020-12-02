/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.StudyCompetence;
import com.sat.entity.StudyModule;
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
public class StudyProgramExtractor implements ResultSetExtractor<StudyProgram> {

    @Override
    public StudyProgram extractData(ResultSet rs) throws SQLException, DataAccessException {
        StudyProgram p = null;
        StudyModule m = null;
        StudyCompetence pc=null;
        StudyCompetence mc=null;
        
        Integer id;
        String name;
        String description;
        String idnumber;
        
        Integer mid;
        String mname;
        String mdescription;
        
        Integer scpid;
        String scpname;
        String scpdescription;
        
        Integer scmid;
        String scmname;
        String scmdescription;
        
        while (rs.next()){

            id=rs.getInt("id");
            name=rs.getString("name");
            description=rs.getString("description");
            idnumber=rs.getString("idnumber");
            
            mid=rs.getInt("mid");if (rs.wasNull()) mid=null;
            mname=rs.getString("mname");
            mdescription=rs.getString("mdescription");
            
            scpid=rs.getInt("scpid");if (rs.wasNull()) scpid=null;
            scpname=rs.getString("scpname");
            scpdescription=rs.getString("scpdescription");
            
            scmid=rs.getInt("scmid");if (rs.wasNull()) scmid=null;
            scmname=rs.getString("scmname");
            scmdescription=rs.getString("scmdescription");
            
            if (p==null){
                p=new StudyProgram();
                p.setId(id);
                p.setName(name);
                p.setDescription(description);
                p.setIdnumber(idnumber);
                
                p.setStudyModules(new ArrayList<StudyModule>());
                p.setStudyCompetences(new ArrayList<StudyCompetence>());
            }
            
            if(mid!=null){
                m=null;
                for(int i=0;i<p.getStudyModules().size();i++){
                    if(p.getStudyModules().get(i).getId()==mid.intValue()){
                        m=p.getStudyModules().get(i);
                    }
                }
                if (m==null) {
                    m=new StudyModule();
                    m.setId(mid);
                    m.setName(mname);
                    m.setDescription(mdescription);
                    m.setStudyCompetences(new ArrayList<StudyCompetence>());
                    p.getStudyModules().add(m);
                }
                if (scmid!=null){
                    mc=null;
                    for (int i=0;i<m.getStudyCompetences().size();i++){
                        if (m.getStudyCompetences().get(i).getId()==scmid.intValue()  ){
                            mc=m.getStudyCompetences().get(i);
                        }
                    }
                    if (mc==null){
                        mc=new StudyCompetence();
                        mc.setId(scmid);
                        mc.setName(scmname);
                        mc.setDescription(scmdescription);
                        m.getStudyCompetences().add(mc);
                    }
                }
            }
            if (scpid!=null){
                pc=null;
                for(int i=0;i<p.getStudyCompetences().size();i++){
                    if (p.getStudyCompetences().get(i).getId()==scpid.intValue()){
                        pc=p.getStudyCompetences().get(i);
                    }
                }
                if (pc==null){
                    pc=new StudyCompetence();
                    pc.setId(scpid);
                    pc.setName(scpname);
                    pc.setDescription(scpdescription);
                    p.getStudyCompetences().add(pc);
                }
            }
        }
                    
        return p;
    }
    
}
