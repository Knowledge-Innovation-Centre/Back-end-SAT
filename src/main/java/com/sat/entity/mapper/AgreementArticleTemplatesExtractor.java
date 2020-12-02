/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.mapper;

import com.sat.entity.AgreementArticle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 *
 * @author Dean
 * 	id INT NOT NULL AUTO_INCREMENT,
	school_id NVARCHAR(64) NOT NULL,
	ordnum INT NOT NULL default 9999,
	content NVARCHAR(512) NOT NULL,
 */
public class AgreementArticleTemplatesExtractor implements ResultSetExtractor<List<AgreementArticle>>{

    @Override
    public List<AgreementArticle> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<AgreementArticle> articles=new ArrayList<AgreementArticle>();
        
        Integer id;
        String school_id;
        Integer ordnum;
        String content;
                
        while (rs.next()){
            id=rs.getInt("aa_id");
            school_id=rs.getString("aa_school_id");
            ordnum=rs.getInt("aa_ordnum");
            content=rs.getString("aa_content");
            
            AgreementArticle aa=new AgreementArticle();
            aa.setId(id);
            aa.setSchool_id(school_id);
            aa.setOrdnum(ordnum);
            aa.setContent(content);
            
            articles.add(aa);
        }
        return articles;
    }
    
}
