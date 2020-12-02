/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import LoginTemplate from './login_template.js';
import Login from './webc/login.js';


    /*
     * register to receive events
     *      userloggedin
     *      userloggedout
     * if you need to handle them in any way 
     */
            var loginObject=document.querySelector('dn-login');
            if (loginObject) loginObject.addEventListener('userloggedin',e => userloggedin(e));
            if (loginObject) loginObject.addEventListener('userloggedout',e => userloggedout(e));
            
            function userloggedin(e){
                console.log(e);
                
                var wrapper=document.querySelector('#wrapper');
                if (wrapper) wrapper.style.visibility='visible';
                
                var dn_login_name=document.querySelectorAll('[dn-login-name]');
                //console.log(dn_login_name);
                dn_login_name.forEach(
                    function(lname){
                        lname.innerHTML=e.detail.user.name+" "+e.detail.user.surname;
                    }
                );
                var dn_login_title=document.querySelectorAll('[dn-login-title]');
                //console.log(dn_login_title);
                dn_login_title.forEach(
                    function(ltitle){
                        ltitle.innerHTML=e.detail.user.title;
                    }
                );
                var dn_login_image=document.querySelectorAll('[dn-login-image]');
                //console.log(dn_login_title);
                dn_login_image.forEach(
                    function(limage){
                        limage.src='./img/'+(e.detail.user.image?e.detail.user.image:'profile_default.jpg');
                    }
                );
                
                //var obj=document.querySelector('dn-login');
                //console.log(obj.getAttribute("currentuser"));
                //console.log(obj.getAttribute("userroles"));
                var userRoles=e.detail.user.roles;
                var loginObject=document.querySelector('dn-login');
                //userRoles=JSON.parse(loginObject.getAttribute("userroles")).roles;
                
                var menuObject=document.querySelector('dn-menu');
                //console.log(menuObject);
                //console.log(userRoles);
                if (menuObject){
                    // nakon što se korisnik ulogirao možemo izgenerirati menu (shodno njegovim dozvolama za pristup pojedinim stavkama)
                    var menu=appMenu.menu;
                    const event = new CustomEvent('setmenu', { detail: { "userroles":userRoles, "menu":menu } });
                    menuObject.dispatchEvent(event);
                }
                
                //const scevent = new CustomEvent('rowselected', { detail: { data: e.detail.user } });
                var scedf=document.querySelector('#selfcareEdit');
                //console.log(scedf);
                if (scedf) scedf.selected=e.detail.user;//scedf.dispatchEvent(scevent);
            }
            
            function userloggedout(e){
                console.log(e);
                //var obj=document.querySelector('dn-login');
                //console.log(obj.getAttribute("currentuser"));
                //console.log(obj.getAttribute("userroles"));
            }
            
import Menu from './webc/menu.js';

    var menuObject=document.querySelector('dn-menu');
    if (menuObject) menuObject.addEventListener('navigationevent',e => navigationEvent(e));
    
    function navigationEvent(e){
        console.log(e);
    }
            
            
    var appMenu = 
        {
            menu:[
                {
                    menuid:"mnuHome",
                    icon:"fa-home",
                    roles:["ROLE_ADMIN","ROLE_IMOVINA","ROLE_HR"],
                    submenu:[]
                }
            ]
        };
        
import TrueFalse from './webc/truefalse.js'; 

import Singleselect from './webc/singleselect.js'; 
    
import Table from './webc/table.js';            
    var userTableObject=document.querySelector('#userstable');
    if (userTableObject) {
        userTableObject.addEventListener('dataloaded',e => userTableDataLoaded(e));
        userTableObject.addEventListener('datarendered',e => userTableDataRendered(e));
        userTableObject.addEventListener('rowselected',e => userTableRowSelected(e));
    }

    //var f=null;
    function userTableDataLoaded(e){
        console.log(e);
    }
    function userTableDataRendered(e){
        console.log(e);
        //if (f==null) f=
        //if (f==null) f=$('#userstable table.footable').footable();
    }
    function userTableRowSelected(e){
        console.log(e);
    }
    
import Multicombo from './webc/multicombo.js';  

var rolesMComboObject=document.querySelector('#userEditform [name=roles]');
    if (rolesMComboObject) {
        rolesMComboObject.addEventListener('dataloaded',e => userMComboDataLoaded(e));
        rolesMComboObject.addEventListener('datarendered',e => userMComboDataRendered(e));
        rolesMComboObject.addEventListener('changed',e => userMComboSelectionChanged(e));
    }
    
    function userMComboDataLoaded(e){
        console.log(e);
        //$("#userEditform dn-multicombo .chosen-select").chosen({});
    }
    function userMComboDataRendered(e){
        console.log(e);
    }
    function userMComboSelectionChanged(e){
        console.log(e);
    }



import Multicheck from './webc/multicheck.js';  

    var rolesMCheckObject=document.querySelector('#userEditform [name=roles]');
    if (rolesMCheckObject) {
        rolesMCheckObject.addEventListener('dataloaded',e => userMCheckDataLoaded(e));
        rolesMCheckObject.addEventListener('datarendered',e => userMCheckDataRendered(e));
        rolesMCheckObject.addEventListener('changed',e => userMCheckSelectionChanged(e));
    }
    
    function userMCheckDataLoaded(e){
        console.log(e);
        //$("#userEditform dn-multicombo .chosen-select").chosen({});
    }
    function userMCheckDataRendered(e){
        console.log(e);
    }
    function userMCheckSelectionChanged(e){
        console.log(e);
    }
    
import Editform from './webc/editform.js'; 

import Modal from './webc/modal.js'; 

import Split from './webc/split.js'; 

import Tab from './webc/tab.js'; 

