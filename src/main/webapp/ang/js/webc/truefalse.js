/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import {L} from './localization.js';

export default class TrueFalse extends HTMLElement{
    static get observedAttributes(){
        return ['disabled'];
    }
    get value() {
      return this.data;
    }
    set value(data){
        
        if (data!=true) data=false;
        //console.log(data);
        
        this.data=data;
        this.renderer();
        const event = new CustomEvent('change', { detail: { data: this.data } });
        this.dispatchEvent(event);
    }
    data;
    attributeChangedCallback(name, oldVal, newValue){
        if (name=="value" && oldVal!=newValue){
            this.renderer();
            
            const event = new CustomEvent('change', { detail: { data: (newValue==true) } });
            this.dispatchEvent(event);
        } 
    }
    constructor() {
        super();
        //var v=this.getBooleanValue();
        //this.setAttribute("value",v);
    }
    connectedCallback(){
        this.renderer();
    }
    renderer(){
        
        var value=this.getBooleanValue();
        var dninput=this.getAttribute("dn-input");
        if (dninput==null) return;
        //console.log(dninput);
        //console.log(name);
        var name=this.getAttribute("name");
        //console.log(L.editform);
        var trans=L.editform[dninput];
        //console.log(name);
        var desc="";
        try{
            desc=trans[name];
        }catch(err){
            console.log(err);
            console.log("dn-input="+dninput);
            console.log(this);
            return;
        }
        
        this.innerHTML="";
        
        var style=document.createElement("STYLE");
        style.innerHTML=`
            dn-truefalse li i{
                font: normal normal normal 14px/1 FontAwesome;
                margin-left:4px;
            }
            dn-truefalse ul{
                list-style-type:none;padding:0px;
            }
            dn-truefalse li{
                border:1px solid lightgray;
                display:inline-block;padding:2px 2px 2px 2px;
                /*margin-top:8px;*/
                margin:2px 2px 0px 0px;
            
                background-color: #FFFFFF;
                background-image: none;
                border: 1px solid #e5e6e7;
                line-height: 1.42857143;
                border-radius: 2px;
                color: inherit;
                display: inline-block;
                padding: 6px 15px;
                transition: border-color 0.15s ease-in-out 0s, box-shadow 0.15s ease-in-out 0s;
            }
            dn-truefalse[disabled] li{
                background-color: #EEEEEE;
            }
        `;
        this.appendChild(style);
        
        var ul=document.createElement("UL");
        var li=document.createElement("LI");
        var span=document.createElement("SPAN");
        span.innerText=desc;
        var input=document.createElement("I");
        input.classList.add('fa');
        input.classList.add( (value==true)?'fa-check-square-o':'fa-square-o');
        li.addEventListener('click',e => this.inputToggle(e));

        li.appendChild(span);
        li.appendChild(input);
        ul.appendChild(li);
        this.appendChild(ul);
       
    }
    inputToggle(e){
        console.log(e);

        var disabled=this.getAttribute("disabled");
        //console.log(disabled)
        if (disabled!=null) return;

        var value=this.getBooleanValue();
        //console.log(value);
        //this.setAttribute("value",!(value));
        this.data=!(value);
        this.renderer();
        //console.log(this.data);
        const event = new CustomEvent('change', { detail: { data: this.data } });
        this.dispatchEvent(event);
    }
    getBooleanValue(){
        var value=this.data;
        if (value==null) value=false;
        if (typeof value === "string"){
            if (value.toUpperCase()=="TRUE") value=true; else value=false;
        }
        return value;
    }

}


if (!customElements.get('dn-truefalse')) {
    customElements.define('dn-truefalse', TrueFalse);
    //console.log(customElements.get('dn-menu'));
}