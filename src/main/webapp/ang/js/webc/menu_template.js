/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import {L} from './localization.js';

export default class MenuTemplate{
    
    menuObject;
    
    constructor(menu){
        this.menuObject=menu;
    }
    render(){
        
        return `
            ${this.css()}
            ${this.html()}
        `;
    }
    html(){
        
        if (this.menuObject.menu!=null && this.menuObject.roles!=null)
            return `
            <ul class="nav metismenu" id="side-menu">
                ${this.menuitems()}
            </ul>
            `;
        else            
            return `
                
            `;
    }
    menuitems(){
        //console.log("menuitems ");
        var html="";
        var i;
        var menuObject=this.menuObject;
        for(i=0;i<menuObject.menu.length;i++){
            
            if ( this.userHasPermission(menuObject.menu[i].roles)){
                //console.log(menuObject.menu[i]);
                html=html+`
                    <li class="" dn-menu="${menuObject.menu[i].menuid}">
                        <a><i class="fa ${menuObject.menu[i].icon}"></i> <span class="nav-label">${L.menu[menuObject.menu[i].menuid].caption}</span> 
                        <span class="fa ${menuObject.menu[i].submenu==null || menuObject.menu[i].submenu.length==0?'':'arrow'}"></span>
                        </a>
                        ${this.menusubitems(menuObject.menu[i].menuid, menuObject.menu[i].submenu)}
                    </li>
                `;
                
            }
        }
        return html;
    }
    menusubitems(menuid, sm){
        //console.log("menusubitems ");
        //console.log(sm);
        var html="";
        var i;
        for(i=0;i<sm.length;i++){
            html=html+this.menusubitem(menuid, sm[i]);
        }
        if (html.length==0) return "";
        //console.log(menuid+"="+html.length+" "+html);
        return `
            <ul class="nav nav-second-level collapse">
                ${html}
            </ul>
         `;
    }
    menusubitem(menuid, smi){
        
        if (!this.userHasPermission(smi.roles)) return "";
        
        return `
            <li class="" dn-menuitem="${smi.menuid}"><a>${L.menu[  menuid   ].subitems[smi.menuid] }</a></li>
         `;
            
            
    }
    userHasPermission(roles){
        //console.log("roles on menuitem:"+roles);
        var i;
        var j;
        var menuObject=this.menuObject;
        //console.log("user has permission for? menuRole="+JSON.stringify(roles)+"?userRole="+JSON.stringify(menuObject.roles)+"");
        for(i=0;i<roles.length;i++){
            for(j=0;j<menuObject.roles.length;j++){
                //console.log("user has permission? menuRole=["+roles[i]+"]?userRole=["+menuObject.roles[j]+"]");
                if (roles[i]==menuObject.roles[j]) {
                    //console.log("YES perrmission!!!");
                    return true;
                }
            }
        }
        //console.log("NO perrmission!!!");
        return false;
    }
    css(){
        //var username=this.login.getAttribute('currentuser');
        if (this.menuObject.menu!=null && this.menuObject.roles!=null)
            return `
                <style>                    
                </style>
            `;
        else
            return `
            `;
    }
}