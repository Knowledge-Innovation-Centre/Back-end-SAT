/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CustomComponentTemplate from './menu_template.js';

export default class CustomComponent extends HTMLElement{
    
    static get observedAttributes(){
        return ['currentuser','userroles','currentuserurl','loginurl','logouturl','dologout'];
    }
        
    attributeChangedCallback(name, oldVal, newValue){
    }
    
    constructor() {
        super();
        this.CCT=new CustomComponentTemplate(this);
    }
    
    connectedCallback(){
        this.innerHTML=CCT.render();
    }
    
    disconnectedCallback(){
    }
    
}

if (!customElements.get('dn-cc')) {
    customElements.define('dn-cc', CustomCOmponent);
}