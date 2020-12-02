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
 * 
 * CREATE TABLE `STUDY_PROGRAMS` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `school_id` varchar(64) CHARACTER SET utf8 NOT NULL,
  `study_program_id` int(11) DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `description` varchar(256) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `STUDY_PROGRAMS_SCHOOLS_FK` (`school_id`),
  KEY `STUDY_PROGRAMS__STUDY_PROGRAMS__FK` (`study_program_id`),
  CONSTRAINT `STUDY_PROGRAMS_SCHOOLS_FK` FOREIGN KEY (`school_id`) REFERENCES `SCHOOLS` (`id`),
  CONSTRAINT `STUDY_PROGRAMS__STUDY_PROGRAMS__FK` FOREIGN KEY (`study_program_id`) REFERENCES `STUDY_PROGRAMS` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_croatian_ci;

 */
public class StudyModule {
    private int id;
    private String name;
    private String description;
    private List<StudyCompetence> studyCompetences;

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

    /**
     * @return the studyCompetences
     */
    public List<StudyCompetence> getStudyCompetences() {
        return studyCompetences;
    }

    /**
     * @param studyCompetences the studyCompetences to set
     */
    public void setStudyCompetences(List<StudyCompetence> studyCompetences) {
        this.studyCompetences = studyCompetences;
    }
}
