/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

import java.sql.Date;

/**
 *
 * @author dean
 */
public class Worklog {
    private String log;
    private Date date_log;

    /**
     * @return the log
     */
    public String getLog() {
        return log;
    }

    /**
     * @param log the log to set
     */
    public void setLog(String log) {
        this.log = log;
    }

    /**
     * @return the date_log
     */
    public Date getDate_log() {
        return date_log;
    }

    /**
     * @param date_log the date_log to set
     */
    public void setDate_log(Date date_log) {
        this.date_log = date_log;
    }
}
