/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

import java.util.List;

/**
 *
 * @author Dean
 */
/*
CREATE TABLE `ORGANIZATION` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orgtype` varchar(64) CHARACTER SET utf8 NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 NOT NULL,
  `cityname` varchar(128) CHARACTER SET utf8 NOT NULL,
  `cityzipcode` varchar(64) CHARACTER SET utf8 NOT NULL,
  `streetname` varchar(256) CHARACTER SET utf8 NOT NULL,
  `contact_username` varchar(64) CHARACTER SET utf8 NULL,
  `school_id` varchar(64) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ORGANIZATION_USERS_FK` (`contact_username`),
  KEY `ORGANIZATION__SCHOOL__FK` (`school_id`),
  CONSTRAINT `ORGANIZATION_USERS_FK` FOREIGN KEY (`contact_username`) REFERENCES `USERS` (`username`),
  CONSTRAINT `ORGANIZATION__SCHOOL__FK` FOREIGN KEY (`school_id`) REFERENCES `SCHOOLS` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci;
*/
public class Organization {
    private int id;
    private String orgtype;
    private String name;
    private String cityname;
    private String cityzipcode;
    private String streetname;
    private String contact_username;
    private String school_id;
    private List<StudyProgram> studyPrograms;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the orgtype
     */
    public String getOrgtype() {
        return orgtype;
    }

    /**
     * @param orgtype the orgtype to set
     */
    public void setOrgtype(String orgtype) {
        if ("SCHOOL".equalsIgnoreCase(orgtype)) 
            this.orgtype="SCHOOL";
        else
            this.orgtype = "ORGANIZATION";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the contact_username
     */
    public String getContact_username() {
        return contact_username;
    }

    /**
     * @param contact_username the contact_username to set
     */
    public void setContact_username(String contact_username) {
        this.contact_username = contact_username;
    }

    /**
     * @return the school_id
     */
    public String getSchool_id() {
        return school_id;
    }

    /**
     * @param school_id the school_id to set
     */
    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    /**
     * @return the studyPrograms
     */
    public List<StudyProgram> getStudyPrograms() {
        return studyPrograms;
    }

    /**
     * @param studyPrograms the studyPrograms to set
     */
    public void setStudyPrograms(List<StudyProgram> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }
}
