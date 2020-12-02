/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.AgreementArticle;
import com.sat.entity.User;
import com.sat.entity.mapper.AgreementArticleTemplatesExtractor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AgreementArticleTemplateDao implements AgreementArticleDaoI{
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public Integer addArticle(final AgreementArticle article, final User currentUser) {
        
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    String sql = "INSERT into AGREEMENT_ARTICLES_TEMPLATE (school_id,content) values(?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"id"} );
                    if (currentUser.getSchool()!=null) stmt.setString(1,currentUser.getSchool()); else stmt.setNull(1,Types.VARCHAR);
                    if (article.getContent()!=null) stmt.setString(2,article.getContent()); else stmt.setNull(2,Types.VARCHAR);
                    return stmt;
                }
            },
            keyHolder);
        int new_org_id=keyHolder.getKey().intValue();
        
        reorderArticles(currentUser);
        
        return Integer.valueOf(new_org_id);
    }
    
    private void reorderArticles(final User currentUser){
        String sql1 ="update AGREEMENT_ARTICLES_TEMPLATE as AA\n" +
                        "INNER JOIN \n" +
                        "(\n" +
                        "	select aat.*, (select count(aaa.id) from AGREEMENT_ARTICLES_TEMPLATE aaa where aaa.school_id=? and aaa.ordnum<=aat.ordnum) as rownum \n" +
                        "	from AGREEMENT_ARTICLES_TEMPLATE as aat \n" +
                        "	where aat.school_id=?\n" +
                        ") AS RN ON AA.id=RN.id\n" +
                        "SET AA.ordnum=RN.rownum*10\n" +
                        "where AA.school_id=?";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1,currentUser.getSchool());
                    stmt.setString(2,currentUser.getSchool());
                    stmt.setString(3,currentUser.getSchool());
                }
            }
        );
    }

    @Override
    public AgreementArticle editArticle(final AgreementArticle article, final User currentUser) {
        String sql1 = "update AGREEMENT_ARTICLES_TEMPLATE "
                + "    SET school_id=?, "
                + "        ordnum=?, "
                + "        content=? "
                + "    where id= ? ";
        jdbcTemplate.update(sql1, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1,currentUser.getSchool());
                    stmt.setInt(2,article.getOrdnum());
                    if (article.getContent()!=null) stmt.setString(3,article.getContent()); else stmt.setNull(3,Types.VARCHAR);
                    stmt.setInt(4,article.getId());
                }
            }
        );
        reorderArticles(currentUser);
        return find(article.getId(),currentUser);
    }

    @Override
    public void deleteArticle(final Integer article_id, final User currentUser) {
        String sql0 = "DELETE from AGREEMENT_ARTICLES_TEMPLATE where school_id=? and id=? ";
        jdbcTemplate.update(sql0, new PreparedStatementSetter(){
                public void setValues(PreparedStatement stmt) throws SQLException{
                    stmt.setString(1, currentUser.getSchool());
                    stmt.setInt(2, article_id);
                }
            }
        );
        reorderArticles(currentUser);
    }

    @Override
    public AgreementArticle find(Integer article_id, User currentUser) {
        List<AgreementArticle> aas=new ArrayList<AgreementArticle>();
                
        aas = jdbcTemplate.query(
                
                " select "+
                "         aat.id as aa_id, "+
                "         aat.school_id as aa_school_id, "+
                "         aat.ordnum as aa_ordnum, "+
                "         aat.content as aa_content "+
                " from AGREEMENT_ARTICLES_TEMPLATE as aat  "+
                " where aat.school_id = ? and aat.id = ? ",
                new Object[] { currentUser.getSchool(),article_id }, 
                new AgreementArticleTemplatesExtractor()
            );
        if (aas!=null && aas.size()>0) return aas.get(0);
        return null;
    }

    @Override
    public List<AgreementArticle> findAllByInternshipAgreement(Integer internshipagreement_id, User currentUser) {
        throw new UnsupportedOperationException("Not supported!!!"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AgreementArticle> findAll(User currentUser) {
        List<AgreementArticle> aas=new ArrayList<AgreementArticle>();
                
        aas = jdbcTemplate.query(
                
                " select "+
                "         aat.id as aa_id, "+
                "         aat.school_id as aa_school_id, "+
                "         aat.ordnum as aa_ordnum, "+
                "         aat.content as aa_content "+
                " from AGREEMENT_ARTICLES_TEMPLATE as aat  "+
                " where aat.school_id = ? ",
                new Object[] { currentUser.getSchool() }, 
                new AgreementArticleTemplatesExtractor()
            );
        return aas;
    }
    
}
