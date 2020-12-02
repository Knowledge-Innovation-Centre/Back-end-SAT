/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * class exposes an event
 * 
 * - userloggedin - event triggers when a user is sucessfully logedin
 * with detail: { user: currentUser }
 * currentUser is a JSON object
 * 
 * - userloggedout - event triggers when user log-out
 * with detail: { user: currentUser }
 * 
 * also
 * 
 * by calling
 *      function dologout(e)
 * app can trigger logout of a user
 * 
 */

import LoginTemplate from './login_template.js';
import {L} from './localization.js';

export default class Login extends HTMLElement{

    currentUser;
    
    /*static get observedAttributes(){
        return ['currentuser','userroles','currentuserurl','loginurl','logouturl','dologout'];
    }*/
    attributeChangedCallback(name, oldVal, newValue){
    }
    
    constructor() {
        super();
        this.currentUser=null;
        this.LT=new LoginTemplate(this);        
    }
    
    urldata=[];
    exp;
    fetchUrlData(url, func_callback_ok, func_callback_err){
        //console.log("???:"+url);
        //console.log(this.urldata);
        this.exp=1000*2; // 2 sekundi
        var now=Date.now();

        if (this.urldata==null) this.urldata=[];
        for (var i=0;i<this.urldata.length;i++){
            var u=this.urldata[i];
            
            if (u.url==url){
                var notify={};
                notify.success=func_callback_ok;
                notify.error=func_callback_ok;
                
                u.notify.push(notify);
                return;
            }
        }

        var u={};
        u.url=url;
        u.expire=now;
        u.notify=[];
            var notify={};
            notify.success=func_callback_ok;
            notify.error=func_callback_ok;
            u.notify.push(notify);
        this.urldata.push(u);

        //console.log("fetch:"+url);
        this.fetchDataFromUrl(url, func_callback_ok, func_callback_err);
    }
    fetchDataFromUrl(url, func_callback_ok, func_callback_err){
        var data="";
        var TH=this;
        document.querySelector("body").style.cursor='progress';
        jQuery.ajax ({
            //url: "/mvnSWP/json/login",
            //url: "/FluidGPS/json/login",
            url: url,
            type: "GET",
            data: data,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function(result,status,xhr){
                document.querySelector("body").style.cursor='auto';
                //console.log("Status:"+status);
                //if (status.toLowerCase() ==  "success" ){
                if (xhr.status==200){
                    TH.notifyClients(true,url,result,status);
                } else {
                    TH.notifyClients(false,url,null,status);
                }
            },
            error: function(status){
                document.querySelector("body").style.cursor='auto';
                TH.notifyClients(false,url,null,status);
            }
        });
    }
    notifyClients(success,url,urldata,status){
        //console.log("notify clients:"+url);
        for (var i=this.urldata.length-1;i>=0;i--){
            var u=this.urldata[i];
            
            if (u.url==url){
                u.expire=Date.now()+this.exp;
                u.data=urldata;
                var toNotify=u.notify;
                //console.log(toNotify);
                var notify={};
                while(notify=toNotify.shift()){
                    //console.log(notify);
                    if (success==true){
                        notify.success(urldata);
                    } else {
                        notify.error(status);
                    }
                }
                this.urldata.splice(i,1);
            }
        }
    }
    
    connectedCallback(){
        
        var currentUser=this.getAttribute('currentuser');
        var currentUserUrl=this.getAttribute('currentuserurl');
        var loginurl=this.getAttribute('loginurl');
        
        if (currentUserUrl!=null && currentUserUrl!=""){
            
            var data="";
            var TH=this;
            jQuery.ajax ({
                //url: "/mvnSWP/json/login",
                //url: "/FluidGPS/json/login",
                url: currentUserUrl,
                type: "GET",
                data: data,
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function(result,status,xhr){
                    //console.log("Status:"+status);
                    if (status.toLowerCase() ==  "success" ){
                        currentUser=result;
                        //console.log(currentUser);
                        TH.setAttribute('currentuser',currentUser.username);
                        TH.setAttribute('userroles',JSON.stringify(currentUser.roles));
                        //console.log("currentUser"+JSON.stringify(currentUser));
                        TH.currentUser=currentUser;
                        const event = new CustomEvent('userloggedin', { detail: { user: currentUser } });
                        TH.dispatchEvent(event);
                    }
                    /*else {
                        alert("Check login failed (status: "+status+")");
                    }*/
                    document.body.style.cursor='default';
                    TH.rerender();
                },
                error: function(status){
                    document.body.style.cursor='default';
                    TH.rerender();
                    //alert("Check login failed (status: "+status+")");
                    //$(".loginform .result").html(status);
                }//(xhr,status,error)
            });
        }
        else {
            this.rerender();
        }
        
    }
    
    dologoutEVT=null;
    
    bindevents(){
        //console.log("bindevents");
        var obj=this.querySelector('#loginbutton');
        //console.log("#loginbutton="+obj);
        if (obj) {
            //console.log("AEL="+obj+" click");
            obj.addEventListener('click',e => this.loginbuttonclicked(e));
        }

        obj=this.querySelector('#loginform');
        //console.log("#loginform="+obj);
        if (obj) {
            //console.log("AEL="+obj+" keypress");
            obj.addEventListener('keypress',e => this.keypressed(e));
        }

        obj=this.querySelector('#logoutlink');
        //console.log("#logoutlink="+obj);
        if (obj) {
            //console.log("AEL="+obj+" click");
            obj.addEventListener('click',e => this.logoutbuttonclicked(e));
        }

        obj=this;
        //console.log("dn-login="+obj);
        if (obj) {
            if (this.dologoutEVT==null){
                this.dologoutEVT=obj;
                //console.log("AEL="+obj+" dologout");
                obj.addEventListener('dologout',e => this.logoutbuttonclicked(e));
            }
        }
    }
    rerender(){
        
        //var currentUser=this.getAttribute('currentuser');
        this.innerHTML = this.LT.render();
        
        this.bindevents();

            var obj=this.querySelector('#username');
            //console.log("#username="+obj);
            if (obj) obj.focus();
            
        //console.log(this.innerHTML);
    }
    
    disconnectedCallback(){
        var obj;
        
        obj=this.querySelector('#loginbutton');
        if (obj) obj.removeEventListener('click',e => this.loginbuttonclicked(e));

        obj=this.querySelector('#loginform');
        if (obj) obj.removeEventListener('keypress',e => this.keypressed(e));

        obj=this.querySelector('#logoutlink');
        if (obj) obj.removeEventListener('click',e => this.logoutbuttonclicked(e));

        if(this.dologoutEVT!=null){
            obj=this;
            if (obj) {
                obj.removeEventListener('dologout',e => this.logoutbuttonclicked(e));
                this.dologoutEVT=null;
            }
        }
    }
    
    logoutbuttonclicked(e){
        //console.log(e);
        
        e = e || window.event;
        if (typeof e.stopPropagation != "undefined") {
            e.stopPropagation();
            e.preventDefault();
        } else {
            e.cancelBubble = true;
        }
        
        var currentUser=this.getAttribute('currentuser');
        var logouturl=this.getAttribute('logouturl');
        
        var TH=this;
        
        if (logouturl!=null && logouturl!=""){
            jQuery.ajax ({
                //url: "/mvnSWP/json/login",
                //url: "/FluidGPS/json/login",
                url: logouturl,
                type: "GET",
                data: "",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function(result,status,xhr){
                    //console.log("Status:"+JSON.stringify(status));
                    if (status.toLowerCase() ==  "success" ){
                        currentUser=result;
                        TH.setAttribute('currentuser','');
                        TH.setAttribute('userroles','');
                        TH.currentUser=null;
                        const event = new CustomEvent('userloggedout', { detail: { user: currentUser }});
                        TH.dispatchEvent(event);
                    }
                    else {
                        alert("Logout failed (1) (status: "+JSON.stringify(status)+")");
                    }
                    document.body.style.cursor='default';
                    TH.rerender();
                },
                error: function(status){

                    document.body.style.cursor='default';
                    alert("Logout failed (2) (status: "+JSON.stringify(status)+")");
                    //$(".loginform .result").html(status);
                }//(xhr,status,error)
            });
        }
    }
    keypressed(e){
        //console.log(e);
        //console.log(e.path.length);
        //console.log(e.path[0].id);
        if (e.path.length > 0 && e.path[0].id=="loginbutton") return;
        if (e.charCode==13 && e.ctrlKey==false && e.shiftKey==false && e.altKey==false && e.metaKey==false)
            this.loginbuttonclicked(e);
    }
    loginbuttonclicked(e){
        
        var obj=this.querySelector('#username');
        var u;
        if (obj) u=obj.value;
        
        obj=this.querySelector('#password');
        var p;
        if (obj) p=obj.value;

        var data=JSON.stringify({
            username:u,
            password:p
        });

        document.body.style.cursor='wait';
        
        var currentUser;
        var loginurl=this.getAttribute('loginurl');
        //console.log(loginurl);
        var TH=this;
        
        jQuery.ajax ({
            //url: "/mvnSWP/json/login",
            //url: "/FluidGPS/json/login",
            url: loginurl,
            type: "POST",
            data: data,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function(result,status,xhr){
                //console.log("Status:"+status);
                if (status.toLowerCase() ==  "success" ){
                    currentUser=result;
                    //console.log("currentUser.roles");
                    //console.log(currentUser.roles);
                    TH.setAttribute('currentuser',currentUser.username);
                    TH.setAttribute('userroles',JSON.stringify({roles:currentUser.roles}));
                    //TH.setAttribute('userroles',currentUser.roles);
                    //console.log("currentUser"+JSON.stringify(currentUser));
                    TH.currentUser=currentUser;
                    const event = new CustomEvent('userloggedin', { detail: { user: currentUser } });
                    TH.dispatchEvent(event);
                }
                else {
                    //alert("Login failed (xstatus: "+status+")");
                    alert(L.login.loginfailed);
                }
                document.body.style.cursor='default';
                TH.rerender();
            },
            error: function(status){
                
                document.body.style.cursor='default';
                //alert("Login failed (status: "+status+")");
                alert(L.login.loginfailed); //L.login.loginfailed
                //$(".loginform .result").html(status);
            }//(xhr,status,error)
        });
    }
    /*
    eventHandler(e){
        const bounds = this.getBoundingClientRect();
        const coords = {
            x: e.clientX - bounds.left,
            y: e.clientY - bounds.top
        };
        switch (e.type){
            case 'mousedown':
                this.isDragging = true;
                this.updateXY(coords.x, coords.y);
                this.refreshCoordinates();
                break;
            case 'mouseup':
                this.isDragging = false;
                break;
            case 'mousemove':
                if (this.isDragging) {
                    this.updateXY(coords.x, coords.y);
                    this.refreshCoordinates();
                }
                break;
        }
    }
    */
}

//window.module = window.module || {};
//module.exports = Login;

if (!customElements.get('dn-login')) {
    customElements.define('dn-login', Login);
}

/*
     * we can have a special object (i.e. link or something)
     * that we need to act as a logout button
     * we just need to add attribute dn-logoutlink to all such objects
     *      <a dn-logoutlink>Logout</a>
     * and following code will register to handle click on thoose html tags
     * and also dispacth event to dn-login custom control
     */
            var logoutObjects=document.querySelectorAll("[dn-logoutlink]");
            logoutObjects.forEach(
                function(logoutObject){
                    logoutObject.addEventListener('click',e=>dologout(e));
                }
            );
            
            function dologout(e){
                //console.log(e);
                //const event = new CustomEvent('dologout', { detail: { user: currentUser }});
                const event = new CustomEvent('dologout', { detail: { } });
                var loginObject=document.querySelector('dn-login');
                loginObject.dispatchEvent(event);
            }
