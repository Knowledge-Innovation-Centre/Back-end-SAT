/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.tools.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author dean
 */
public class HtmlElement {
    private String name;
    private String content;
    private List<HtmlElement> subelements;
    HashMap<String, String> attributes = new HashMap<String, String>();
    
    public HtmlElement(String name){
        this.name=name.toLowerCase();
        this.content=null;
        subelements=new ArrayList<HtmlElement>();
    }
    public HtmlElement(String name, String content){
        this.name=name.toLowerCase();
        this.content=content;
        subelements=new ArrayList<HtmlElement>();
    }
    public HtmlElement addSubelement(String name){
        return addSubelement(name,null);
    }
    public HtmlElement addSubelement(String name, String content){
        HtmlElement e=new HtmlElement(name, content);
        subelements.add(e);
        return e;
    }
    public void setContent(String content){
         this.content=content;
    }
    public String getContent(){
        return this.content;
    }
    public void addAttribute(String name, String value){
        String val=attributes.get(name);
        if (val==null) val=value;
                  else val=val+" "+value;
        
        attributes.put(name, val);
    }
    public String getHtml(){
        StringBuffer ret=new StringBuffer();
        ret.append("<"+name);
        
        for(String key:attributes.keySet()){
            ret.append(" ");
            ret.append(key);
            ret.append("=");
            ret.append(attributes.get(key));
        }
        if (content==null && subelements.size()==0){
            ret.append(" />");
            return ret.toString();
        }
        else{
            ret.append(">");
        }
        
        if (content!=null)
            ret.append(content.replace("<", "&lt;"));
        for (HtmlElement e:subelements){
            ret.append(e.getHtml());
        }
        ret.append("</"+name+">");
        
        return ret.toString();
    }
}
