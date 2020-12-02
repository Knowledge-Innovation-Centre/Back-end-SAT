/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.dao;

import com.sat.entity.Organization;
import com.sat.entity.User;
import java.util.List;

/**
 *
 * @author Dean
 */
public interface OrganizationDaoI {
    public Organization addOrganization(Organization org, User currentUser);
    public void editOrganization(Organization org, User currentUser);
    public void deleteOrganization(Integer organization_id, User currentUser);
    public Organization find(Integer organization_id, User currentUser);
    public List<Organization> findAll(User currentUser, String organization_type);
}
