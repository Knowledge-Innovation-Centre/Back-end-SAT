/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

/**
 *
 * @author Dean
 * 
 * CREATE TABLE `STUDY_COMPETENCES` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 NOT NULL,
  `description` varchar(256) CHARACTER SET utf8 NOT NULL,
  `study_program_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `STUDY_COMPETENCES__STUDY_PROGRAMS__FK` (`study_program_id`),
  CONSTRAINT `STUDY_COMPETENCES__STUDY_PROGRAMS__FK` FOREIGN KEY (`study_program_id`) REFERENCES `STUDY_PROGRAMS` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci;

* 
 */
public class StudyCompetence {
    private int id;
    private String name;
    private String description;

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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
