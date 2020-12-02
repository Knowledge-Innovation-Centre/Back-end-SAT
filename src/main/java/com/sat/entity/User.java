/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dean
 */
public class User {
    
    private String first_name;
    private String last_name;
    
    private String username;
    private String password;
    private String school;
    //private List<String> roles;
    private String[] roles;
    
    private String phone;
    private String email;
    private String cityname;
    private String cityzipcode;
    private String streetname;
    
    private Integer organization;
    
    private StudyProgram studyprogram;
    
    private Boolean jwt=false;
    
    public boolean hasRole(String role){
        for(int i=0;i<roles.length;i++)
            if (roles[i].equals(role) || roles[i].equals("ROLE_"+role)) 
                return true;
        
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getRoles() {
        return roles;
    }
    
    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public void setRolesFromList(List<String> roles) {
        //this.roles = roles;
        String[] r=new String[roles.size()];
        roles.toArray(r);
        this.roles = r;
    }
    
    @JsonIgnore
    public List<String> getRolesAsList(){
        List<String> ret=new ArrayList<String>();
        for(int i=0;i<roles.length;i++)
            ret.add(roles[i]);
        
        return ret;
    }

    /**
     * @return the school
     */
    public String getSchool() {
        return school;
    }

    /**
     * @param school the school to set
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the cityname
     */
    public String getCityname() {
        return cityname;
    }

    /**
     * @param cityname the cityname to set
     */
    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    /**
     * @return the cityzipcode
     */
    public String getCityzipcode() {
        return cityzipcode;
    }

    /**
     * @param cityzipcode the cityzipcode to set
     */
    public void setCityzipcode(String cityzipcode) {
        this.cityzipcode = cityzipcode;
    }

    /**
     * @return the streetname
     */
    public String getStreetname() {
        return streetname;
    }

    /**
     * @param streetname the streetname to set
     */
    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    /**
     * @return the organization
     */
    public Integer getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(Integer organization) {
        this.organization = organization;
    }

    /**
     * @return the studyprogram
     */
    public StudyProgram getStudyprogram() {
        return studyprogram;
    }

    /**
     * @param studyprogram the studyprogram to set
     */
    public void setStudyprogram(StudyProgram studyprogram) {
        this.studyprogram = studyprogram;
    }

    /**
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the jwt
     */
    public Boolean getJwt() {
        return jwt;
    }

    /**
     * @param jwt the jwt to set
     */
    public void setJwt(Boolean jwt) {
        this.jwt = jwt;
    }

    


}
