/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


export default class Editform extends HTMLElement{
    static get observedAttributes(){
        return ['disabled'];
    }
        
    data;
    
    get selected() {
      return this.data;
    }
    set selected(data){
        //console.log("dobio data");
        //console.log(data);
        this.data=data;
        /*var e;
        e={};
        e['detail']={};
        e.detail['data']=this.data;*/
        this.rowSelected(null);
    }
        
    attributeChangedCallback(name, oldVal, newValue){
        if (name=="value" && oldVal!=newValue){
            this.data=null;
            var v;
            if (newValue=="") v="[]"; else v=newValue;
            this.data=JSON.parse(v);
            //console.log(this.data);
            var e;
            e={};
            e['detail']={};
            e.detail['data']=this.data;
            this.rowSelected(e);
            //this.renderer();
            //const event = new CustomEvent('change', { detail: { data: this.data } });
            //this.dispatchEvent(event);
        } 
    }
    
    surpresupdate
    constructor() {
        super();   
        this.surpresupdate=false;
    }
    
    ActionsNONE=0;
    ActionsINSERT=1;
    ActionsUPDATE=2;
    action=this.ActionsNONE;
    
    StatesUNMODIFIED=0;
    StatesMODIFIED=1;
    state=this.StatesUNMODIFIED;
    
    btnNew;
    btnCancel;
    btnSave;
    btnDelete;
    
    connectedCallback(){
        //console.log("editform connectedCallback");
        /*tableform="userstable"
        var tableform=this.getAttribute("tableform");
        if (tableform){
            var tbf=document.querySelector("#"+tableform);
            if (tbf) tbf.addEventListener('rowselected', e => this.rowSelected(e));
        }
        */
        //this.addEventListener('rowselected', e => this.rowSelected(e));
        
        var id=this.getAttribute('id');
        
        this.btnNew=this.querySelector("[dn-editform-new="+id+"]");
        if (this.btnNew) this.btnNew.addEventListener('click',e => this.buttonNewClicked(e));
        this.btnCancel=this.querySelector("[dn-editform-cancel="+id+"]");
        if (this.btnCancel) this.btnCancel.addEventListener('click',e => this.btnCancelClicked(e));
        this.btnSave=this.querySelector("[dn-editform-save="+id+"]");
        if (this.btnSave) this.btnSave.addEventListener('click',e => this.btnSaveClicked(e));
        this.btnDelete=this.querySelector("[dn-editform-delete="+id+"]");
        if (this.btnDelete) this.btnDelete.addEventListener('click',e => this.btnDeleteClicked(e));
        
        if (this.btnSave && !this.btnNew && !this.btnCancel && !this.btnDelete){
            this.state=this.StatesMODIFIED;
            this.action=this.ActionsUPDATE;
        }
        
        var inputs=this.querySelectorAll('[dn-input='+id+']');
        var i;
        for (i=0;i<inputs.length;i++){
            //console.log(inputs[i]);
            inputs[i].addEventListener('change',e => this.inputChanged(e));
            inputs[i].addEventListener('input',e => this.inputChanged(e));
        }
        this.adjustStates();
    }
    inputChanged(e){
        //console.log(this.getAttribute("id"));
        //console.log(e);
        if (this.surpresupdate) return;
        this.state=this.StatesMODIFIED;
        this.adjustStates();
    }
    buttonNewClicked(e){
        this.clearAllInputs();
        this.action=this.ActionsINSERT;
        this.state=this.StatesUNMODIFIED;
        this.adjustStates();
    }
    btnCancelClicked(e){
        this.clearAllInputs();
        this.action=this.ActionsNONE;
        this.state=this.StatesUNMODIFIED;
        this.adjustStates();
        this.setAttribute("value","");
    }
    OBJ2STRING(data){
        var ret="";
        
        if (typeof data==='object'){
            if (Array.isArray(data)){
                ret='[';
                for (var i=0;i<data.length;i++){
                    if (i!=0) ret+=',';
                    ret+=this.OBJ2STRING(data[i])
                }
                ret+=']';
            }
            else {
                var keys=Object.keys(data);
                ret='{';
                for (var i=0;i<keys.length;i++){
                    if (i!=0) ret+=',';
                    ret+='"'+keys[i]+'":'
                    ret+=this.OBJ2STRING(data[keys[i]]);
                }
                ret+='}';
            }
        }else{
            ret+='"'+data+'"';
        }
        
        return ret;
    }
    btnSaveClicked(e){
        //console.log(e)
        //console.log((this.action==this.ActionsUPDATE?"UPDATE":"INSERT"));
        var data=this.getJSONObject();
        var url;
        if (this.action==this.ActionsUPDATE){
            url=this.getAttribute("updateurl");
        }
        if (this.action==this.ActionsINSERT){
            url=this.getAttribute("inserturl");
        }
        
        
        if (!url){
            alert("greška u postavkama.... kriv je programer");
            return;
        }
        
        var targetobject=this.getAttribute("targetobject");
        //console.log(targetobject);
        //console.log(data);
        if (targetobject) data=data[targetobject];
        //console.log(data);
        
        document.querySelector("body").style.cursor='progress';
        console.log(url);
        console.log(data);
        console.log(JSON.stringify(data));
        
        //var strdata=this.OBJ2STRING(data);
        //console.log(strdata);
        
        var TH=this;
        jQuery.ajax ({
            url: url,
            type: "POST",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function(result,status,xhr){
                //console.log("Status:"+status);
                if (status.toLowerCase() ==  "success" ){
                    document.querySelector("body").style.cursor='auto';
                    //console.log("result"+JSON.stringify(result));
                    TH.action=TH.ActionsUPDATE;
                    TH.state=TH.StatesUNMODIFIED;
                    TH.adjustStates();
                    //console.log("result"+JSON.stringify(result));
                    TH.notifyModal();
                    
                    const event = new CustomEvent('datachanged', { detail: { data: result } });
                    TH.dispatchEvent(event);
                    
                }
                else {
                    document.querySelector("body").style.cursor='auto';
                    console.log("data send failed (status: "+status+")");
                }
            },
            error: function(status){
                document.querySelector("body").style.cursor='auto';
                console.log("error");
                console.log(status);
                alert(JSON.stringify(status));
                //TH.rerender();
                
            }//(xhr,status,error)
        });
        
        
        
    }
    btnDeleteClicked(e){
        //console.log(e);
        var data=this.getJSONObject();
        var url=this.getAttribute("deleteurl");
        if (!url){
            alert("greška u postavkama.... kriv je programer");
            return;
        }
        
        var targetobject=this.getAttribute(targetobject);
        if (targetobject) data=data[targetobject];
        //console.log(data);
        document.querySelector("body").style.cursor='progress';
        var TH=this;
        jQuery.ajax ({
            url: url,
            type: "DELETE",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function(result,status,xhr){
                //console.log("Status:"+status);
                if (status.toLowerCase() ==  "success" ){
                    document.querySelector("body").style.cursor='auto';
                    //console.log("result"+JSON.stringify(result));
                    //console.log("SUCESS result"+JSON.stringify(result));
                    TH.clearAllInputs();
                    TH.action=TH.ActionsNONE;
                    TH.state=TH.StatesUNMODIFIED;
                    TH.adjustStates();
                    TH.notifyModal();
                    
                    const event = new CustomEvent('datachanged', { detail: { data: result } });
                    TH.dispatchEvent(event);
                    
                }
                else {
                    console.log("data send failed (status: "+status+")");
                    document.querySelector("body").style.cursor='auto';
                }
            },
            error: function(status){
                document.querySelector("body").style.cursor='auto';
                console.log("error");
                console.log(status);
                alert(JSON.stringify(status));
                //TH.rerender();
                
            }//(xhr,status,error)
        });
              
    }
    notifyModal(){
        var nm=this.getAttribute("notifymodal");
        if (nm==null) return;
        
        var notify=document.querySelectorAll("[modaledit='"+nm+"']");
        var i;
        for(i=0;i<notify.length;i++){
            notify[i].loadData();
        }
    }
    clearAllInputs(){
        var id=this.getAttribute('id');
        var inputs=this.querySelectorAll('[dn-input='+id+']');
        var i;
        for (i=0;i<inputs.length;i++){
            //console.log("clear: "+inputs[i].getAttribute("name"));
            if(inputs[i].getAttribute("dn-object")!=null){
                //inputs[i].setAttribute("value","");
                inputs[i].selected=[];
            }
            else
                inputs[i].value="";
        }
    }
    
    getJSONObject(){
        var object={};
        var id=this.getAttribute('id');
        var inputs=this.querySelectorAll('[dn-input='+id+']');
        var i;
        var value;
        for (i=0;i<inputs.length;i++){
            //console.log(inputs[i].nodeName);
            value="";
            var name=inputs[i].getAttribute("name");
            if (name) {
                //console.log(inputs[i].getAttribute("value"));
                if(inputs[i].getAttribute("dn-object")!=null){
                    //value=JSON.parse(inputs[i].getAttribute("value"));
                    value=inputs[i].selected;
                }
                else
                    if (inputs[i].nodeName.startsWith("DN-")){
                        //value=inputs[i].getAttribute("value");
                        value=inputs[i].value;
                    }
                    else
                        value=inputs[i].value;
            }
            this.addValueToObject(object,name,value)
        }
        //console.log(object);
        return object;
    }
    addValueToObject(object,field,value){
        //console.log(field);
        //console.log(value);
        //console.log(object);
        var fields=field.split(".");
        var len=fields.length;
        var o=object;
        var i;
        for (i=0;i<fields.length;i++){
            if (i<len-1){
                if (typeof o[fields[i]] === 'undefined'){
                    o[fields[i]]={};
                }
                o=o[fields[i]];
            }
            else{
                o[fields[i]]=value
            }
        }
        //console.log(object);
    }
    adjustStates(){
        this.adjustButtonStates();
        this.adjustInputStates();
    }
    adjustButtonStates(){
        if (this.action==this.ActionsNONE){
            if (this.btnNew)    this.btnNew.disabled = false;
            if (this.btnCancel) this.btnCancel.disabled = true;
            if (this.btnSave)   this.btnSave.disabled = true;
            if (this.btnDelete) this.btnDelete.disabled = true;
        }
        if (this.action==this.ActionsUPDATE || this.action==this.ActionsINSERT){
            if (this.state==this.StatesUNMODIFIED){
                if (this.btnNew)    this.btnNew.disabled = true;
                if (this.btnCancel) this.btnCancel.disabled = false;
                if (this.btnSave)   this.btnSave.disabled = true;
                
                if (this.action==this.ActionsUPDATE)
                    if (this.btnDelete)   this.btnDelete.disabled = false;
                
                if (this.action==this.ActionsINSERT)
                    if (this.btnDelete)   this.btnDelete.disabled = true;
            }
            if (this.state==this.StatesMODIFIED){
                if (this.btnNew)    this.btnNew.disabled = true;
                if (this.btnCancel) this.btnCancel.disabled = false;
                if (this.formValidates())
                    if (this.btnSave)   this.btnSave.disabled = false;
                else
                    if (this.btnSave)   this.btnSave.disabled = true;
                
                if (this.action==this.ActionsUPDATE)
                    if (this.btnDelete)   this.btnDelete.disabled = false;
                
                if (this.action==this.ActionsINSERT)
                    if (this.btnDelete)   this.btnDelete.disabled = true;
            }
        }
    }
    adjustInputStates(){
        var id=this.getAttribute('id');
        var inputs=this.querySelectorAll('[dn-input='+id+']');
        var i;
        for(i=0;i<inputs.length;i++){
            //console.log(inputs[i]);
            var pk=inputs[i].getAttribute("dn-primaryfield");
            //console.log(inputs[i]);
            //console.log(pk);
            if (this.action==this.ActionsNONE){
                //console.log("enable");
                inputs[i].setAttribute("disabled",true);
            }
            
            if (this.action==this.ActionsUPDATE || this.action==this.ActionsINSERT){
                if (pk!=null && this.action==this.ActionsUPDATE){
                    //console.log("disable");
                    inputs[i].setAttribute("disabled",true);
                }
                else if (pk=="auto" && this.action==this.ActionsINSERT){
                    //console.log("disable");
                    inputs[i].setAttribute("disabled",true);
                }
                else {
                    //console.log("enable");
                    inputs[i].removeAttribute("disabled");
                }
            }
        }
    }
    formValidates(){
        return true;
    }
    
    // const event = new CustomEvent('rowselected', { detail: { data: selecteddata } });
    rowSelected(e){
        //console.log(e);
        
        if (this.data==null) return;
        if (this.data.length==0) return;
        
        this.surpresupdate=true;
        ///////////this.data=e.detail.data;
        //console.log(this.data);
        var id=this.getAttribute('id');
        var inputs=this.querySelectorAll('[dn-input='+id+']');
        //console.log(id);
        //console.log(inputs);
        var i;
        for (i=0;i<inputs.length;i++){
            var name=inputs[i].getAttribute("name");
            
            var dnfetch=inputs[i].getAttribute("dn-fetch");
            if (dnfetch!=null){
                var pkcolumn=this.getAttribute("primary_key");
                var pkval=this.getClumnValue(this.data,pkcolumn);
                //console.log("set parenkey = "+ pkval)
                //console.log(inputs[i]);
                inputs[i].parentkey=pkval;
            } else {
                if((typeof this.data[name])=="object"){
                    //console.log("set selected = "+ this.getClumnValue(this.data,name))
                    //console.log(inputs[i]);
                    //console.log(this.getClumnValue(this.data,name));
                    inputs[i].selected=this.getClumnValue(this.data,name);
                }
                else{
                    //console.log("set value = "+ this.getClumnValue(this.data,name))
                    //console.log(inputs[i]);
                    
                    if (inputs[i].nodeName.startsWith("DN-")){
                        inputs[i].value=this.getClumnValue(this.data,name);
                    }else{
                        inputs[i].value=this.getClumnValue(this.data,name);
                    }
                }
            }
        }
        this.action=this.ActionsUPDATE;
        this.state=this.StatesUNMODIFIED;
        this.adjustStates();
        this.surpresupdate=false;
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


if (!customElements.get('dn-editform')) {
    customElements.define('dn-editform', Editform);
    //console.log(customElements.get('dn-menu'));
}