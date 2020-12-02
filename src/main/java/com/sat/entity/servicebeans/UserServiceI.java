/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.servicebeans;

import com.sat.entity.User;
import java.util.List;

/**
 *
 * @author dean
 */
public interface UserServiceI {
    public void addUser(User user);
    public void editUser(User user, String username);
    public void deleteUser(String username);
    public User find(String usernam);
    public List<User> findAll();
    public List<User> findUsersWithinOrganization(User currentUser);
    public List<User> findUsersWithinSchool(User currentUser);
}
