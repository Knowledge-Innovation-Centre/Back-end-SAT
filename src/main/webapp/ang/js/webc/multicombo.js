/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * 
 * attributes:
 *  listoptions - url for fetching ALL options to choose from
 *  value - to set/retrieve selected values
 * 
 * 
 * 
 */


export default class Multicombo extends HTMLElement{
    static get observedAttributes(){
        return ['disabled'];
    }
    
    active; // contol becomes active after if loads data (list of all the options) from server
    data;
    
    get selected() {
      return this.selectedValues;
    }
    set selected(data){
        this.selectedValues=data;
        this.renderer();
        const event = new CustomEvent('change', { detail: { data: this.selectedValues } });
        this.dispatchEvent(event);
    }
    
    attributeChangedCallback(name, oldVal, newValue){
        if (name=="value" && oldVal!=newValue){
            //console.log("new value="+newValue);
            this.renderer();
            const event = new CustomEvent('change', { detail: { data: this.selectedValues } });
            this.dispatchEvent(event);
        } 
        if (name=='disabled'){
            //console.log(name+"="+newValue);
            var input=this.querySelector("input");
            //console.log(input);
            if (input) {
                if (newValue=="true") {
                    //console.log("set disabled="+newValue);
                    input.setAttribute("disabled",newValue);
                }
                else{
                    //console.log("remove disabled");
                    input.removeAttribute("disabled");
                }
                //console.log(input);
            }
        }
    }
    
    constructor() {
        super();     
        this.selectedValues=[];
        this.active=false;
    }
    connectedCallback(){
        var login;
        login=document.querySelector('dn-login');
        
        var currentUser=login.getAttribute("currentuser");
        //console.log(currentUser);
        
        if (currentUser==null) {
            //console.log("multicombo schedule for later");
            login.addEventListener('userloggedin',e => this.userloggedin(e));
            return;
        }
        this.loadData();
    }
    userloggedin(e){
        //console.log(e);
        this.loadData()
    }
    loadData(){
        //console.log("loaddata");
        var dataurl=this.getAttribute('listoptions');
        if (!dataurl) return;
        
        var data="";
        var TH=this;
        
        document.querySelector('dn-login').fetchUrlData(dataurl, 
                    function func_callback_ok(result){
                        TH.data=result;
                        TH.active=true;
                        TH.renderer();
                    }, 
                    function func_callback_err(status){
                        console.log("error");
                        console.log(status);
                    }
                );
        
        /*
        jQuery.ajax ({
            url: dataurl,
            type: "GET",
            data: data,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function(result,status,xhr){
                if (status.toLowerCase() ==  "success" ){
                    TH.data=result;
                    TH.active=true;
                    TH.renderer();
                }
                else {
                    console.log("data load failed (status: "+status+")");
                }
            },
            error: function(status){
                console.log("error");
                console.log(status);
            }
        });
            */
    }
    
    input;
    datalist;
    ulselected;
    
    selectedValues; // array
    
    isKeySelected(key){
        //console.log("is key selected? "+key);
        var i,len,k;
        len=this.selectedValues.length;
        //console.log(this.selectedValues);
        //console.log(len);
        for (i=0;i<len;i++){
            k=this.selectedValues[i];
            if (k==key) {
                //console.log("is key selected? found="+k);
                return true;
            }
        }
        return false;
    }
    inputRemoved(e){
        //console.log(e);
        
        var li=e.target;
        while (li.nodeName!='LI') {
            li=li.parentElement;
            if (li.nodeName=='HTML'){
                return;
            }
        }
        
        var key=li.getAttribute("key");
        var i,len,k;
        len=this.selectedValues.length;
        for (i=len-1;i>=0;i--){
            k=this.selectedValues[i];
            if (k==key) {
                this.selectedValues.splice(i, 1);
            }
        }
        
        //this.setAttribute("value",JSON.stringify(this.selectedValues));
    }
    inputChanged(e){
        //console.log(e);
        
        if (this.input.value=="") return;
        var val=this.input.value
        //console.log(val);
        
        var i,j,len;
        len=this.data.length;
        for(i=0;i<len;i++){
            if (val==this.data[i].value){
                // to znači da imamo doista legalno odabranog
                this.selectedValues.push(this.data[i].key);
                //console.log(this.selectedValues);
                var inputs=this.datalist.childNodes;
                for (j=inputs.length-1;j>=0;j--){
                    //console.log(inputs[j]);
                    if (this.data[i].value==inputs[j].innerText){
                        //console.log(inputs[j]);
                        this.datalist.removeChild(inputs[j]);
                    }
                }
            }
        }
        this.input.value="";
        const event = new CustomEvent('change', { detail: { data: this.selectedValues } });
        this.dispatchEvent(event);
        //console.log(JSON.stringify(this.selectedValues));
        //this.setAttribute("value",JSON.stringify(this.selectedValues));
    }
    getDescriptionForKey(key){
        var i,len;
        len=this.data.length;
        for (i=0;i<len;i++){
            if (this.data[i].key==key) return this.data[i].value;
        }
    }
    renderer(){
        if (!this.active) return;
        
        var i,len;
        var name=this.getAttribute('name');
        //console.log(name);
        if (!name) return;
        
        this.innerHTML="";
        
        //console.log(this.data);
        var style=document.createElement("STYLE");
        style.innerHTML=`
            dn-multicombo li i{
                font: normal normal normal 14px/1 FontAwesome;
                margin-left:2px;
            }
            dn-multicombo ul{
                list-style-type:none;padding:0px;
            }
            dn-multicombo li{
                border:1px solid lightgray;
                display:inline-block;padding:2px 2px 2px 2px;
                margin-top:8px;margin-right:2px
            }
        `;
        this.appendChild(style);
        var input = document.createElement("INPUT");
        var attr;
        attr=document.createAttribute("list");
        attr.value=name;
        input.setAttributeNode(attr);
        var disabled=this.getAttribute("disabled");
        if (disabled){
            attr=document.createAttribute("disabled");
            attr.value=true;
            input.setAttributeNode(attr);
        }
        attr=document.createAttribute("class");
        attr.value="form-control";
        input.setAttributeNode(attr);
        input.addEventListener('change',e => this.inputChanged(e));
        this.appendChild(input);
        this.input=input;
        
        var ul=document.createElement("UL");
        /*var selectedValues=this.getAttribute('value');
        if (selectedValues==null || selectedValues=="") 
            selectedValues=[];
        else
            selectedValues=JSON.parse(selectedValues);
       
        
        this.selectedValues=selectedValues;*/
        //console.log(this.selectedValues);

        len=selectedValues.length;
        var li;
        for (i=0;i<len;i++){
            li=document.createElement("LI");
            li.innerHTML='<span>'+this.getDescriptionForKey(selectedValues[i])+'</span><i class="fa fa-times"></i>';
            attr=document.createAttribute("key");
            attr.value=selectedValues[i];
            li.setAttributeNode(attr);
            li.addEventListener('click',e => this.inputRemoved(e));
            ul.appendChild(li);
        }
        
        this.ulselected=ul;
        this.appendChild(input);
        this.appendChild(ul);
        
        var dlist=document.createElement("DATALIST");
        attr=document.createAttribute("id");
        attr.value=name;
        dlist.setAttributeNode(attr);
        var opt;
        len=this.data.length;
        for(i=0;i<len;i++){
            if (!this.isKeySelected(this.data[i].key)){
                opt=document.createElement("OPTION");
                opt.innerText=this.data[i].value;
                attr=document.createAttribute("key");
                attr.value=this.data[i].key;
                opt.setAttributeNode(attr);
                dlist.appendChild(opt);
            }
        }
        this.appendChild(dlist);
        this.datalist=dlist;
        
        //const event = new CustomEvent('datarendered', { detail: { } });
        //this.dispatchEvent(event);
    }
}

if (!customElements.get('dn-multicombo')) {
    customElements.define('dn-multicombo', Multicombo);
    //console.log(customElements.get('dn-menu'));
}