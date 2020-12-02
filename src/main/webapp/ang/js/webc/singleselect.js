/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


export default class Singleselect extends HTMLElement{
    
    data;
        
    static get observedAttributes(){
        return ['disabled'];
    }
    
    get value() {
      return this.selectedValue;
    }
    set value(data){
        //console.log("primio value=");
        //console.log(data);
        this.selectedValue=data;
        //console.log("primio value="+this.selectedValue +"@"+this.getAttribute("dn-input"));
        this.renderer();
        const event = new CustomEvent('change', { detail: { data: this.selectedValue } });
        this.dispatchEvent(event);
    }
    selectedValue;
    attributeChangedCallback(name, oldVal, newValue){
        //console.log(name+"="+newValue);
        if (name=="value" && oldVal!=newValue){
            this.renderer();
            const event = new CustomEvent('change', { detail: { data: (newValue==true) } });
            this.dispatchEvent(event);
        } 
        if (name=="disabled"){
            this.renderer();
        }
    }
    constructor() {
        super();
    }
    login;
    modaledit;
    connectedCallback(){
        
        //console.log("primio kroz value="+this.getAttribute("value")+"@"+this.getAttribute("dn-input"));
        var v=this.getAttribute("value");
        if (v!=null) this.selectedValue=v;
        //console.log("connected callback");
        //console.log(this);
        var login;
        login=document.querySelector('dn-login');
        this.login=login;
        
        this.modaledit=this.getAttribute('modaledit')
        
        var currentUser=login.getAttribute("currentuser");
        
        if (currentUser==null) {
            //console.log("multicombo schedule for later");
            //console.log(this);
            login.addEventListener('userloggedin',e => this.userloggedin(e));
            return;
        }
        this.loadData();
    }
    userloggedin(e){
        //console.log("userloggedin");
        //console.log(this);
        this.loadData();
    }
    loadData(){
        //console.log("loaddata");
        //console.log(this);
        var dataurl=this.getAttribute('listoptions');
        if (!dataurl) return;
        
        var data="";
        var TH=this;
        
        this.login.fetchUrlData(dataurl, 
                    function func_callback_ok(result){
                        TH.data=result;
                        TH.renderer();
                    }, 
                    function func_callback_err(status){
                        console.log("error");
                        console.log(status);
                    }
                );
    }
    xxxloadData(){
        //console.log("loaddata");
        //console.log(this);
        var dataurl=this.getAttribute('listoptions');
        if (!dataurl) return;
        
        var data="";
        var TH=this;
        jQuery.ajax ({
            url: dataurl,
            type: "GET",
            data: data,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function(result,status,xhr){
                //console.log("Status:"+status);
                if (status.toLowerCase() ==  "success" ){
                    //console.log("result"+JSON.stringify(result));
                    TH.data=result;
                    //console.log("data loaded");
                    //console.log(TH.data);
                    //const event = new CustomEvent('dataloaded', { detail: { data: TH.data } });
                    //TH.dispatchEvent(event);
                    TH.renderer();
                }
                else {
                    console.log("data load failed (status: "+status+")");
                }
            },
            error: function(status){
                console.log("error");
                console.log(status);
                //TH.rerender();
                
            }//(xhr,status,error)
        });
    }
    findDataRow(id){
        if (id==null) return {};
        var i,d;
        for (i=0;i<this.data.length;i++){
            
            d=Object.values(this.data[i]);

            if (d[0]==id) {
                return d;
            }
        }
        return {};
    }
    renderer(){
        //console.log("renderer");
        if (this.data==null) return;
        
        
        var disabled=this.getAttribute("disabled");
        if (disabled!=null) disabled=true; else disabled=false;
        
        //var value=this.getAttribute("value");
        var value=this.selectedValue;
        //console.log("selected value="+this.selectedValue +"@"+this.getAttribute("dn-input"));
        this.innerHTML="";
        
        
        if (this.modaledit){
            var style=document.createElement("STYLE");
            style.innerHTML=`
                dn-singleselect select{
                    width: CALC( 100% - 35px)  !important;
                    display: inline-block  !important;
                }
                dn-singleselect i{
                    width: 35px;
                    display: inline-block !important;
                    padding-left:5px;
                    font-size:120%;
                    margin: 0px;
                    text-align:center;
                    height: 34px !important;
                    background-color: white;
                    vertical-align: top;
                    padding-top: 11px;
                }
            `;
            this.appendChild(style);
        }
        
        var attr;
        
        if (disabled==true){
            var span=document.createElement("INPUT");
            span.classList.add("form-control");
            attr=document.createAttribute("disabled");
            span.setAttributeNode(attr);
            
            var d=this.findDataRow(value);

            var j;
            var v='';
            for(j=1;j<d.length;j++){
                if (j>1) v=v+' ';
                v=v+d[j];
            }
            attr=document.createAttribute("value");
            attr.value=v;
            span.setAttributeNode(attr);
            
            this.appendChild(span);
            return;
        }
        
        
        var sel=document.createElement("SELECT");
        
        if (disabled==true){
            attr=document.createAttribute("disabled");
            sel.setAttributeNode(attr);
        }
        //class="form-control m-b"
        sel.classList.add("form-control");
        //sel.classList.add("m-b");
        var len;
        len=this.data.length;
        var i,j;
        var d;
        var opt=document.createElement("OPTION");
        opt.innerText='';
        sel.appendChild(opt);
        for (i=0;i<len;i++){
            d=Object.values(this.data[i]);
            opt=document.createElement("OPTION");
            if (d[0]==value){
                attr=document.createAttribute("selected");
                opt.setAttributeNode(attr);
            }
            attr=document.createAttribute("value")
            attr.value=d[0];
            
            opt.innerText='';
            for(j=1;j<d.length;j++){
                if (j>1) opt.innerText=opt.innerText+' ';
                opt.innerText=opt.innerText+d[j];
            }
            opt.setAttributeNode(attr);
            sel.appendChild(opt);
        }
        sel.addEventListener('change',e => this.inputToggle(e));
        this.appendChild(sel);
        if (this.modaledit){
            var input=document.createElement("I");
            input.classList.add("fa");
            input.classList.add("fa-pencil-square-o");
            //input.outerHTML='<i class="fa fa-pencil-square-o"></i>';
            input.addEventListener('click',e => this.openModalEditor(e));
            this.appendChild(input);
        }
    }
    openModalEditor(e){
        var me=document.querySelector("#"+this.modaledit);
        if (me) me.removeAttribute("disabled");
    }
    inputToggle(e){
        //console.log(e);
        
        var disabled=this.getAttribute("disabled");
        //console.log("disabled="+disabled);
        if (disabled!=null) return;
        
        //console.log(e.target.value);
        this.selectedValue=e.target.value;
        //console.log(this.selectedValue);
        //this.setAttribute("value",e.target.value);
        
        this.renderer();

        const event = new CustomEvent('change', { detail: { data: this.selectedValue } });
        this.dispatchEvent(event);
    }
    getClumnValue(data,column){
        var columns=column.split(".");
        var i;
        var d=data;
        for (i=0;i<columns.length;i++){
            d=d[columns[i]];
        }
        return d;
    }
}


if (!customElements.get('dn-singleselect')) {
    customElements.define('dn-singleselect', Singleselect);
    //console.log(customElements.get('dn-menu'));
}