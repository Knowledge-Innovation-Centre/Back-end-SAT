/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity.servicebeans;

import com.sat.entity.User;
import com.sat.entity.dao.UserDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import com.sat.entity.dao.UserDaoI;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author dean
 */
//@Component
@Service//("userService")
public class UserService implements UserServiceI{

    @Autowired
    UserDaoI userDao;

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public void editUser(User user, String username) {
        userDao.editUser(user, username);
    }

    @Override
    public void deleteUser(String username) {
        userDao.deleteUser(username);
    }

    @Override
    public User find(String username) {
        return userDao.find(username);
    }   
 
    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> findUsersWithinOrganization(User currentUser) {
        return userDao.findUsersWithinOrganization(currentUser);
    }
    
    @Override
    public List<User> findUsersWithinSchool(User currentUser) {
        return userDao.findUsersWithinSchool(currentUser);
    }
    
}
