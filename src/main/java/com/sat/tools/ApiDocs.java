/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sat.tools;

import com.sat.tools.html.HtmlElement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author dean
 */
public class ApiDocs {
    public static void generate(File f, Class controllers[], Class _entiteis[]){
        HashMap<String,Class> entityunique=new HashMap<String,Class>();
        List<Class> entity=new ArrayList<Class>();
        for(int i=0;i<_entiteis.length;i++){
            entityunique.put(_entiteis[i].getName(), _entiteis[i]);
        }
        List<Class> controller=new ArrayList<Class>();
        for(int i=0;i<controllers.length;i++){
            controller.add(controllers[i]);
        }
        controller.sort( new Comparator<Class>() {
            @Override
            public int compare(Class arg0, Class arg1) {
                return arg0.getName().compareToIgnoreCase( arg1.getName()  );
            }
        });
        
        HtmlElement html=new HtmlElement("html");
        HtmlElement head=html.addSubelement("head");
            head.addSubelement("title","Api documentation");
        String css=""+
            "body{font-family:sans-serif;}"+
            "body>div{"+
            "    display:inline-block;"+
            "    width:auto;"+
            "    vertical-align:top;"+
            "    padding:10px;"+
            "}"+
            "h2{margin-top:40px;}"+
            "ul{display:inline-block;padding-left:0px;}"+
            "body>div>ul{padding-left:20px;}"+
            "li{margin-bottom:20px;list-style-type:none;}"+
            "table{border-spacing:0px;border: 1px solid darkslategray;}"+
            "td{padding:10px 20px;width:auto}"+
            "td:first-child{width:80px;}"+
            "a{text-decoration:none;font-style:italic;font-weight:bold;color:maroon}"+
            ".mapping{background-color:darkslategray;color:white;font-weight:600;}"+
            ".auth(border-bottom:1px solid darkslategray;}"+
            ".returntype,.payload, auth{background-color:lightgray;color:black;font-weight:normal;}"+
            "";
        
            head.addSubelement("style",css);
        HtmlElement body=html.addSubelement("body");
        
        HtmlElement controllersDiv=body.addSubelement("div");
            controllersDiv.addSubelement("H1","Controllers");
            HtmlElement contContents=controllersDiv.addSubelement("UL");
        HtmlElement con=controllersDiv.addSubelement("div");
        for(Class c:controllers){
            HtmlElement cont=con.addSubelement("div");cont.addAttribute("id", c.getName());
            cont.addSubelement("h2",getShortName(c.getName()));
            HtmlElement li=contContents.addSubelement("li");
            HtmlElement a=li.addSubelement("a",getShortName(c.getName()));
            a.addAttribute("href","#"+c.getName());
            HtmlElement methods=cont.addSubelement("UL");
            
            
            Method m[]=c.getDeclaredMethods();
            for (int i=0;i<m.length;i++){        
                                        
                if (m[i].getModifiers() == java.lang.reflect.Modifier.PUBLIC){
                    HtmlElement method=methods.addSubelement("li");
                
                    HtmlElement table=method.addSubelement("table");
                    table.addAttribute("class", "controllermethod");
                    HtmlElement row;
                    HtmlElement td;
                    Class t;
                    Type tt;
                    String rettype;
                    
                    RequestMapping mapping=m[i].getAnnotation(RequestMapping.class );
                    if (mapping==null) continue;
                    {
                        String reqmethods="";
                        RequestMethod mtds[]=mapping.method();
                        for (int j=0;j<mtds.length;j++){
                            if (j!=0) reqmethods=reqmethods+", ";
                            reqmethods=reqmethods+mtds[j].name();
                        }
                        String requrls="";
                        for (int j=0;j<mapping.value().length;j++){
                            if (j!=0) requrls=requrls+", ";
                            requrls=requrls+mapping.value()[j];
                        }
                        row=table.addSubelement("tr");
                        row.addAttribute("class", "mapping");
                            row.addSubelement("td",reqmethods);
                            row.addSubelement("td", requrls);
                    }


                    row=table.addSubelement("tr");
                    row.addAttribute("class", "auth");
                    PreAuthorize auth=m[i].getAnnotation(PreAuthorize.class );
                    if (auth!=null){
                        row.addSubelement("td","Auth:");
                        row.addSubelement("td", auth.value());
                    }
                        
                    else{
                        row.addSubelement("td","Auth:");
                        row.addSubelement("td", "-");
                    }
                    
                    //@PathVariable
                    //@RequestBody
                    //@RequestParam("file")
                   
                    Parameter p[]=m[i].getParameters();
                    for (int j=0;j<p.length;j++){
                        /*PathVariable pv=p[j].getAnnotation(PathVariable.class);
                        if (pv!=null){
                             row=table.addSubelement("tr");
                            row.addAttribute("class", "payload");
                            row.addSubelement("td",pv.name());
                            row.addSubelement("td",p[j].getType().toGenericString() + " " + p[j].getName());
                            if (pv.value()!=null) td.addSubelement("span", " "+pv.value());
                            
                        }*/
                        RequestBody rb=p[j].getAnnotation(RequestBody.class);
                        if (rb!=null){
                            row=table.addSubelement("tr");
                            row.addAttribute("class", "payload");
                            row.addSubelement("td","Payload:");
                            td=row.addSubelement("td");
                            //td.addSubelement("a", getShortName(p[j].getType().getName() )).addAttribute("href", "#"+p[j].getType().getName());
                            
                            t=p[j].getType();
                            if (t==List.class){
                                //Type gt=p[j].getParameterizedType();
                                Type gt=m[i].getGenericParameterTypes()[j];
                                Type gts[]=((ParameterizedType)(gt)).getActualTypeArguments();
                                td.addSubelement("span", "List<");
                                for (int k=0;k<gts.length;k++){
                                    if (k!=0) {
                                        td.addSubelement("span", ",");
                                    }
                                    td.addSubelement("a", getShortName(gts[k].getTypeName())).addAttribute("href", "#"+gts[k].getTypeName());
                                    //entityunique.put(gts[j].getTypeName(), gts[j].getClass());
                                    entityunique.put(gts[k].getTypeName(), (Class)gts[k]);
                                }
                                td.addSubelement("span", ">");
                            }else{
                                td.addSubelement("a", getShortName(t.getName())).addAttribute("href", "#"+t.getName());
                                entityunique.put(t.getName(), t);
                            }
                        }
                        
                        RequestParam rp=p[j].getAnnotation(RequestParam.class);
                        if (rp!=null){
                            row=table.addSubelement("tr");
                            row.addAttribute("class", "payload");
                            row.addSubelement("td","Parameter:");
                            td=row.addSubelement("td");
                            //td.addSubelement("a", getShortName(p[j].getType().getName() )).addAttribute("href", "#"+p[j].getType().getName());
                            t=p[j].getType();
                            if (t==List.class){
                                //Type gt=p[j].getParameterizedType();
                                Type gt=m[i].getGenericParameterTypes()[j];
                                Type gts[]=((ParameterizedType)(gt)).getActualTypeArguments();
                                td.addSubelement("span", "List<");
                                for (int k=0;k<gts.length;k++){
                                    if (k!=0) {
                                        td.addSubelement("span", ",");
                                    }
                                    td.addSubelement("a", getShortName(gts[k].getTypeName())).addAttribute("href", "#"+gts[k].getTypeName());
                                    //entityunique.put(gts[j].getTypeName(), gts[j].getClass());
                                    entityunique.put(gts[k].getTypeName(), (Class)gts[k]);
                                }
                                td.addSubelement("span", ">");
                            }else{
                                td.addSubelement("a", getShortName(t.getName())).addAttribute("href", "#"+t.getName());
                                entityunique.put(t.getName(), t);
                            }
                            if (rp.value()!=null) td.addSubelement("span", " "+rp.value());
                        }
                    }
                    
                    
                    
                    row=table.addSubelement("tr");
                    row.addAttribute("class", "returntype");
                    row.addSubelement("td","Returns:");
                    td=row.addSubelement("td");
                    t=m[i].getReturnType();
                    rettype=getShortName(t.getName());
                    if (t==List.class){
                        rettype=rettype+"<";
                        Type gt=m[i].getGenericReturnType();
                        Type gts[]=((ParameterizedType)(gt)).getActualTypeArguments();
                        td.addSubelement("span", "List<");
                        for (int j=0;j<gts.length;j++){
                            if (j!=0) {
                                td.addSubelement("span", ",");
                            }
                            td.addSubelement("a", getShortName(gts[j].getTypeName())).addAttribute("href", "#"+gts[j].getTypeName());
                            //entityunique.put(gts[j].getTypeName(), Class.forName(gts[j].getTypeName()));
                            entityunique.put(gts[j].getTypeName(), (Class)gts[j]);
                        }
                        td.addSubelement("span", ">");
                    }else{
                        td.addSubelement("a", rettype).addAttribute("href", "#"+t.getName());
                        entityunique.put(t.getName(), t);
                    }
                    //td.addSubelement("a", rettype).addAttribute("href", rettype);
                        
                    
                    /*
                    row=table.addSubelement("tr");
                        row.addSubelement("td","Name:");
                        row.addSubelement("td", m[i].getName());
                    */
                    
                }
            }
        }
        
        for(String key:entityunique.keySet()){
            Class c;
            c=entityunique.get(key);
            if (c!=String.class && 
                    c!=Integer.class && c!=Long.class && 
                    c!=java.sql.Date.class && c!=java.util.Date.class  && 
                    c!=Double.class &&  c!=Float.class && 
                    c!=Void.class && c!=Class.class && c!=void.class
                    )
                entity.add(entityunique.get(key));
        }
        entity.sort( new Comparator<Class>() {
            @Override
            public int compare(Class arg0, Class arg1) {
                return arg0.getName().compareToIgnoreCase( arg1.getName()  );
            }
        });
        
        HtmlElement entitiesDiv=body.addSubelement("div");
            entitiesDiv.addSubelement("H1","Entities");
            HtmlElement entContents=entitiesDiv.addSubelement("UL");
        HtmlElement ent=entitiesDiv.addSubelement("div");
        
        for (int i=0;i<entity.size();i++){
            Class c=entity.get(i);
            entContents.addSubelement("LI")
                    .addSubelement("a", getShortName(c.getName()))
                        .addAttribute("href", "#"+c.getName());
            
            HtmlElement cont=ent.addSubelement("div");cont.addAttribute("id", c.getName());
            cont.addSubelement("h2",getShortName(c.getName()));
            HtmlElement table=cont.addSubelement("table");
            HtmlElement row;
            HtmlElement td;
            table.addAttribute("class", "entity");

            Field ff[]=c.getDeclaredFields();
            for(int j=0;j<ff.length;j++){
                row=table.addSubelement("tr");
                td=row.addSubelement("td");
                if (ff[j].getType()==List.class){
                    Type gt=ff[j].getGenericType();
                    Type gts[]=((ParameterizedType)(gt)).getActualTypeArguments();
                    td.addSubelement("span", "List<");
                    for (int k=0;k<gts.length;k++){
                        if (k!=0) {
                            td.addSubelement("span", ",");
                        }
                        td.addSubelement("a", getShortName(gts[k].getTypeName())).addAttribute("href", "#"+gts[k].getTypeName());
                        //if (!entity.contains((Class)gts[k]))
                        //    entity.add((Class)gts[k]);
                    }
                    td.addSubelement("span", ">");
                }
                else{
                    if (ff[j].getType().isArray()){
                        td.addSubelement("a", getShortName(ff[j].getType().getComponentType().getName())+"[]")
                            .addAttribute("href", "#"+ff[j].getType().getComponentType().getName());
                        //if (!entity.contains(ff[j].getType().getComponentType()))
                        //    entity.add(ff[j].getType().getComponentType());
                    }
                    else{
                        td.addSubelement("a", getShortName(ff[j].getType().getName()))
                            .addAttribute("href", "#"+ff[j].getType().getName());
                        //if (!entity.contains(ff[j].getType()))
                        //    entity.add(ff[j].getType());
                    }
                }
                
                row.addSubelement("td",ff[j].getName());
                
                    
            }
            
        }
        
                
        {
            FileWriter fw=null;
            try {
                f.getParentFile().mkdirs();
                fw = new FileWriter(f);
                fw.append(html.getHtml());
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(ApiDocs.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(ApiDocs.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private static String getShortName(String classname){
        int idx=classname.lastIndexOf(".");
        return classname.substring(idx+1);
    }
}
