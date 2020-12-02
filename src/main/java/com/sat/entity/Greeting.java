/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.entity;

/**
 *
 * @author dean
 */
public class Greeting {
    
    private final long id;
    private final String content;

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
    
    public Greeting(long id, String content){
        this.id=id;
        this.content=content;
    }
    
}
