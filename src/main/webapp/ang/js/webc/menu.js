/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import MenuTemplate from './menu_template.js';
/*
 * Menu exposes an event
 *  - navigationevent
 *  with detail: { selectedmenuitem: selected_menu_item_ID }
 * 
 */
export default class Menu extends HTMLElement{
    
    static get observedAttributes(){
        return ['selectedmenuitem'];
    }
    menu;
    roles;
    
    attributeChangedCallback(name, oldVal, newValue){
        switch (name) {
            case 'selectedmenuitem':
                //console.log("selectedmenuitem "+newValue);
                if (oldVal!=newValue) 
                    this.selectMenu();
                break;
        }
    }
    
    selectMenu(){
        var selecteditm=this.getAttribute('selectedmenuitem');
        
        var mnu;
        mnu=this.querySelectorAll('[dn-menu]');
        mnu.forEach(
            function(mnu){
                //console.log(mnu);
                mnu.classList.remove('active');
                var mnu_ul=mnu.querySelector('ul');
                                
                if (mnu_ul) {
                    //console.log(mnu_ul);
                    mnu_ul.classList.remove('in');
                }
                
                //console.log(mnu);
                var mnuitem=mnu.querySelectorAll('[dn-menuitem]');
                mnuitem.forEach(
                    function(mnuitem){
                        //console.log(mnuitem);
                        mnuitem.classList.remove('active');
                        var mnuitem_id=mnuitem.getAttribute('dn-menuitem');
                        if (selecteditm==null || mnuitem_id==selecteditm){
                            //console.log("selecteditm:"+selecteditm);
                            //console.log("menuitem_id:"+mnuitem_id);
                            if (!mnuitem.classList.contains('active')){
                                selecteditm=mnuitem_id;
                                mnuitem.classList.add('active');
                                mnu.classList.add('active');
                                if (mnu_ul) mnu_ul.classList.add('in');
                            }
                        }
                    }
                )
                var mnuitem_id=mnu.getAttribute('dn-menu');
                if (selecteditm==null || mnuitem_id==selecteditm){
                    if (!mnu.classList.contains('active')){
                        selecteditm=mnuitem_id;
                        mnu.classList.add('active');
                        if (mnu_ul) mnu_ul.classList.add('in');
                    }
                }
            }
        );
        this.setAttribute('selectedmenuitem',selecteditm);
        const event = new CustomEvent('navigationevent', { detail: { selectedmenuitem: selecteditm } });
        this.dispatchEvent(event);
        
        var url=window.location.href;
        var i=url.indexOf("#");
        if (i!=-1) url=url.substring(0,i);
        url=url+"#"+selecteditm;
        //console.log(url);
        window.location.href=url;
        
        this.showhide_menuaction();
    }
    
    showhide_menuaction(){
        
        var selecteditm=this.getAttribute('selectedmenuitem');
        //console.log(selecteditm);
        //if (selecteditm==null || selecteditm=="") return;
        
        //console.log("showhidemenuaction");
        
        var mnuactions=document.querySelectorAll('[dn-menuaction]');
        var id;
        mnuactions.forEach(
            function(mnuactions){
                id=mnuactions.getAttribute('dn-menuaction');
                //console.log(mnuactions);
                //console.log(id+" ? "+selecteditm);
                if (id==selecteditm){
                    mnuactions.style.display='block';
                } else {
                    mnuactions.style.display='none';
                }
                
            }
        );
    }
    
    
    constructor() {
        super();
        
        this.MT=new MenuTemplate(this);
    }
    
    connectedCallback(){
        this.addEventListener('setmenu',e => this.setMenuObject(e));
        
        var selecteditm=this.getAttribute('selectedmenuitem');
        if (selecteditm!=null) this.selectMenu();
    }
    
    disconnectedCallback(){
        this.addEventListener('setmenu',e => this.setMenuObject(e));
    }
    setMenu(e){
        // event for: clicked on first level menu
        /*
         * when 1st level menu is clicked 
         * - it opens (if it has subitems) - by adding the 'in' to the class of its <UL> 
         * - closes all other <UL>'s by removing 'in' from theirs classList 
         * 
         * if 1st level item has no elements then it shoukld beheive as a 1nd level (meniitem) - there is no open and close
         */
        
        //console.log(e);
        //console.log(e.target);
        
        var dnmenu=e.target;
        var menu_id=dnmenu.getAttribute('dn-menu');
        while (menu_id==null){
            if (dnmenu.nodeName=='HTML') {
                console.log('did not found!!');
                return;
            }
            dnmenu=dnmenu.parentElement;
            //console.log(dnmenu);
            menu_id=dnmenu.getAttribute('dn-menu');
        }
        
        e = e || window.event;
        if (typeof e.stopPropagation != "undefined") {
            e.stopPropagation();
            e.preventDefault();
        } else {
            e.cancelBubble = true;
        }
        
        // jel prazan (ima li podstavaka)?
        var menuitems=dnmenu.querySelectorAll('ul>li');
        if (menuitems.length>0){
            //console.log("ima podstavaka");
            var ul=dnmenu.querySelector('ul');
            if (ul!=null){
                //console.log("toggle open/closed");
                ul.classList.toggle("in");
            } // else - something is wrong!!
        }
        else{
            //console.log("nema podstavaka");
            //console.log("trigger menu event!!!");
            //console.log(menu_id);
            this.setAttribute('selectedmenuitem',menu_id);
        }
        
    }
    setMenuItem(e){
        // event for: clicked on second level menu (menuitem)
        //console.log(e);
        //console.log(e.target);
        
        var dnmenu=e.target;
        console.log(e);
        var menu_id=dnmenu.getAttribute('dn-menuitem');
        while (menu_id==null){
            if (dnmenu.nodeName=='HTML') {
                console.log('did not found!!');
                return;
            }
            dnmenu=dnmenu.parentElement;
            //console.log(dnmenu.nodeName);
            menu_id=dnmenu.getAttribute('dn-menuitem');
        }
        
        if (menu_id){
            var ul=dnmenu.parentElement;
            if (ul.nodeName=='UL' && ul.classList.contains("dropdown-menu"))
                    ul.classList.remove("show");
        }
        
        e = e || window.event;
        if (typeof e.stopPropagation != "undefined") {
            e.stopPropagation();
            e.preventDefault();
        } else {
            e.cancelBubble = true;
        }
        //dnmenu.classList.toggle("active");
        //console.log(menu_id);
        this.setAttribute('selectedmenuitem',menu_id);
    }
    
    setMenuObject(e){
        // event for: MenuObject (JSON description) has been assignet to the component
        //console.log(e);
        
        e = e || window.event;
        if (typeof e.stopPropagation != "undefined") {
            e.stopPropagation();
            e.preventDefault();
        } else {
            e.cancelBubble = true;
        }
        
        //console.log(e.detail.userroles);
        //console.log(e.detail.menu);
        this.menu=e.detail.menu;
        this.roles=e.detail.userroles;
        
        var url=window.location.href;
        var i=url.indexOf("#");
        if (i!=-1) {
            url=url.substring(i+1);
            //console.log("trenutno odabrani menu="+url);
            //console.log(this.roles);
            //console.log(this.menu);
            var rolesNeeded=this.getRolesForMenu(url); // koje role su mu potrebne da bi vidio trenutni menu?
            //console.log(rolesNeeded);
            if (rolesNeeded && this.hasUserRole(this.roles,rolesNeeded)){
                this.setAttribute('selectedmenuitem',url); // ovo se smije postaviti samo ako user ima rolu koja mu dozvoljava da pristupi tom meniju
            } else {
                console.log("user nema potrebnu ulogu za pristup meniju "+url);
                
                var url=window.location.href;
                var i=url.indexOf("#");
                if (i!=-1) url=url.substring(0,i);
                url=url;
                //console.log(url);
                window.location.href=url;
                this.setAttribute('selectedmenuitem',""); 
            }
        }
        
        
        this.innerHTML = this.MT.render();
        this.addEventListeners();
        this.selectMenu();
    }
    getRolesForMenu(mnuId){
        var i,j;
        var len;
        len=this.menu.length;
        for(i=0;i<len;i++){
            //console.log(this.menu[i].menuid);
            if (this.menu[i].menuid==mnuId){
                return this.menu[i].roles;
            }
            if (this.menu[i].submenu.length){
                for(j=0;j<this.menu[i].submenu.length;j++){
                    //console.log("   "+this.menu[i].submenu[j].menuid);
                    if (this.menu[i].submenu[j].menuid==mnuId){
                        return this.menu[i].submenu[j].roles;
                    }
                }
            }
        }
    }
    hasUserRole(userRoles,needRoles){
        var i,j;
        //console.log(userRoles);
        //console.log(needRoles);
        for(i=0;i<userRoles.length;i++){
            for(j=0;j<needRoles.length;j++){
                if (userRoles[i]==needRoles[j]) return true;
            }
        }
    }
    addEventListeners(){
        var TH=this;
        
        var mnu;
        mnu=document.querySelectorAll('[dn-menu]');
        mnu.forEach(
            function(mnu){
                //console.log(mnu);
                mnu.addEventListener('click', e => TH.setMenu(e));
            }
        );

        var mnuitem;
        mnuitem=document.querySelectorAll('[dn-menuitem]');
        mnuitem.forEach(
            function(mnuitem){
                //console.log(mnuitem);
                mnuitem.addEventListener('click', e => TH.setMenuItem(e));
            }
        );
    }
    
    /*
    addEventListeners(){
        var TH=this;
        var mnu;
        var mnuitem
        mnu=this.querySelectorAll('[dn-menu]');
        mnu.forEach(
            function(mnu){
                //console.log(mnu);
                mnu.addEventListener('click', e => TH.setMenu(e));
                mnuitem=mnu.querySelectorAll('[dn-menuitem]');
                mnuitem.forEach(
                    function(mnuitem){
                        //console.log(mnuitem);
                        mnuitem.addEventListener('click', e => TH.setMenuItem(e));
                    }
                );
            }
        );
    }
    */
}

if (!customElements.get('dn-menu')) {
    customElements.define('dn-menu', Menu);
    //console.log(customElements.get('dn-menu'));
}