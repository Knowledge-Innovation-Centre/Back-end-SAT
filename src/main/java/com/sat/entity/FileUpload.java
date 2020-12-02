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
 * 
 *      id INT NOT NULL AUTO_INCREMENT,
	owner_username NVARCHAR(64) NOT NULL,
	table_name VARCHAR(64) NOT NULL,
	table_id integer NULL,
	bytes BLOB,
	mime VARCHAR(64) NOT NULL,
	filename VARCHAR(256) NOT NULL,
	vrijeme DATETIME not null DEFAULT now(),
 */
public class FileUpload {
    private int id;
    private String owner_username;
    private String table_name;
    private Integer table_id;
    private String mime_type;
    private String filename;
    private Date vrijeme;
    private byte[] bytes;

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
     * @return the owner_username
     */
    public String getOwner_username() {
        return owner_username;
    }

    /**
     * @param owner_username the owner_username to set
     */
    public void setOwner_username(String owner_username) {
        this.owner_username = owner_username;
    }

    /**
     * @return the table_name
     */
    public String getTable_name() {
        return table_name;
    }

    /**
     * @param table_name the table_name to set
     */
    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    /**
     * @return the table_id
     */
    public Integer getTable_id() {
        return table_id;
    }

    /**
     * @param table_id the table_id to set
     */
    public void setTable_id(Integer table_id) {
        this.table_id = table_id;
    }

    /**
     * @return the mime_type
     */
    public String getMime_type() {
        return mime_type;
    }

    /**
     * @param mime_type the mime_type to set
     */
    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the vrijeme
     */
    public Date getVrijeme() {
        return vrijeme;
    }

    /**
     * @param vrijeme the vrijeme to set
     */
    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
    }

    /**
     * @return the bytes
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * @param bytes the bytes to set
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    
}
