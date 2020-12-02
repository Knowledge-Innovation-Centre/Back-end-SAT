/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//import Split from './menu_template.js';

export default class Tab extends HTMLElement{
    
    /*static get observedAttributes(){
        return ['currentuser','userroles','currentuserurl','loginurl','logouturl','dologout'];
    }*/
        
    attributeChangedCallback(name, oldVal, newValue){
    }
    
    constructor() {
        super();
        //this.CCT=new CustomComponentTemplate(this);
    }
    
    connectedCallback(){
        this.render();
    }
    
    disconnectedCallback(){
    }
    
    render(){
        var tabcontrols=document.createElement("UL");
        var style=document.createElement("STYLE");
        if (this.hasChildNodes()){
            this.insertBefore(tabcontrols, this.childNodes[0]);
            this.insertBefore(style, this.childNodes[0]); 
        }
        style.innerHTML=`
            dn-tab>[dn-tab]{
                display:none;
            }
            dn-tab>[dn-tab=active]{
                display:block;
            }
            dn-tab ul{
                list-style-type:none;
                margin:0px;
                padding-left:0px;
            }
            dn-tab ul li{
                display:inline-block;
                background-color:rgba(255,255,255,0.6);/*rgba(229, 230, 231,0.5);*/
                padding: 0px 16px 0px 16px;
                margin-left:4   px;
                border-top-left-radius:10px;
                border-top-right-radius:10px;
                border-left: 1px solid rgb(229, 230, 231);
                border-right: 1px solid rgb(229, 230, 231);
                border-top: 1px solid rgb(229, 230, 231);
                text-transform: uppercase;
            }
            dn-tab ul li[dn-tab="active"]{
                background-color:white;
                font-weight:bold;
            }
        `;
        var tabs=this.querySelectorAll("[dn-tab]"); 
        var desc;
        for(var i=0;i<tabs.length;i++){
            desc=tabs[i].getAttribute("desc");
            var tab=document.createElement("LI");
            tab.innerText=desc
            tab.setAttribute("desc",desc);
            if (i==0){
                //this.setAttribute("activetab",desc)
                tabs[i].setAttribute("dn-tab","active");
                tab.setAttribute("dn-tab","active");
            }
            else {
                tabs[i].setAttribute("dn-tab","inactive");
                tab.setAttribute("dn-tab","inactive");
            }
            tab.addEventListener('click',e => this.tabchanged(e));
            tab.style.cursor='pointer';
            tabcontrols.appendChild(tab)
        }
    }

    tabchanged(e){
        //console.log(e);
        var desc=e.target.innerText;
        //console.log(desc);
        
        var tabs=this.querySelectorAll("dn-tab [dn-tab]"); 
        for (var i=0;i<tabs.length;i++){
            //console.log("---"+tabs[i].getAttribute("desc"));
            if (desc.toUpperCase()==tabs[i].getAttribute("desc").toUpperCase()){
                tabs[i].setAttribute("dn-tab","active");
            }else {
                tabs[i].setAttribute("dn-tab","inactive");
            }
        }
    }

}

if (!customElements.get('dn-tab')) {
    customElements.define('dn-tab', Tab);
}