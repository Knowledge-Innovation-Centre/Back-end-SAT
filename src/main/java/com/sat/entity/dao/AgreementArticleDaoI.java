/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.AgreementArticle;
import com.sat.entity.User;
import java.util.List;

/**
 *
 * @author Dean
 */
public interface AgreementArticleDaoI {
    public Integer addArticle(AgreementArticle article, User currentUser);
    public AgreementArticle editArticle(AgreementArticle article, User currentUser);
    public void deleteArticle(Integer article_id, User currentUser);
    public AgreementArticle find(Integer article_id, User currentUser);
    public List<AgreementArticle> findAllByInternshipAgreement(Integer internshipagreement_id, User currentUser);
    public List<AgreementArticle> findAll(User currentUser);
}
