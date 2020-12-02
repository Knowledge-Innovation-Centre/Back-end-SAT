/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import {L} from './localization.js';

import TrueFalse from './truefalse.js'; 

import Singleselect from './singleselect.js'; 

export default class Table extends HTMLElement{
    
    data;
    loadeddata;
    columns;
    columns_desc;
    table_id;
    
    hideMiniActions;
    
    currentPage;
    pageSize;
    
    currentSort;
    sortColumn;
    
    active; // contol becomes active after if loads data from server
    
    selectedRow;
    
    editform;
    
    static get observedAttributes(){
        return ['disabled'];
    }
    set setvalue(datarecieved){
        if (this.selectedRow==null) return;
        var keys=Object.keys(datarecieved);
        
        var selectedData=this.data[this.selectedRow];
        for(var i=0;i<keys.length;i++){
            this.addValueToObject(selectedData,keys[i],datarecieved[keys[i]]);
        }

        this.RErender();
        const event = new CustomEvent('change', { detail: { data: this.data } });
        this.dispatchEvent(event);
    }
    parent_key;
    set parentkey(id){
        //console.log("parentkey:"+id+" @"+this.getAttribute("id"));
        if (typeof(id) == "undefined") {
            this.selected=new Array();
            this.RErender();
            return;
        }
        
        this.parent_key=id;
        var url=this.getAttribute("dn-fetch");
        if (url==null) return;
        url=url.replace("{}", id);
        this.datafetch(url);
        this.notifyForEmpty();
    }
    datafetch(url){
        var login=document.querySelector('dn-login');
        if (login==null) return;
        var TH=this;
        login.fetchUrlData(url, 
            function func_callback_ok(result){
                TH.loadeddata=result.slice(0);
                TH.data=result.slice(0);
                //TH.data=result;
                //TH.selected=result;
                
                TH.RErender();
            }, 
            function func_callback_err(status){
                console.log("error");
                console.log(status);
            }
        );
        
    }
    get selected() {
        if (this.data==null) return new Array();
        //console.log(this.getAttribute("id"));
        //console.log(this.data);
        return this.data;
    }
    set selected(data){
        //console.log(this.getAttribute("id"));
        //console.log(this.data);
        if (data==null) 
            this.data=new Array();
        else
            this.data=data;
        
        this.RErender();
        //this.loadData();
        //this.renderer();
    }
    attributeChangedCallback(name, oldVal, newValue){
        //console.log(name);
        //console.log(newValue);
        if (name=="value" && oldVal!=newValue || newValue==""){
            this.data=null;
            var v;
            if (newValue=="") v="[]"; else v=newValue;
            this.data=JSON.parse(v);
            //console.log(this.data);
            this.dataChanged(null);
            this.RErender();
            
            //const event = new CustomEvent('change', { detail: { data: this.data } });
            //this.dispatchEvent(event);
        } 
        if (name=="disabled"){
            //console.log(this.name+" disabled="+newValue);
            //traba enable/disable svi dječicu
            this.notifyChildren();
        }
    }
    
    constructor() {
        super();    
        this.currentSort='none';
        this.sortColumn='none';
        this.column_types=null;
        this.selectedRowKey=null;
        this.hideMiniActions=false;
        this.editform=null;
    }
    connectedCallback(){
        
        
        var ed_id=this.getAttribute("editformid"); 
        if (ed_id){
            var edf=document.querySelector('#'+ed_id);
            //console.log(edf);
            if (edf) {
                this.editform=edf;
                edf.addEventListener('datachanged',e => this.dataChanged(e));
            }
        }
        
        var edf_id=this.getAttribute('dn-input');
        if (edf_id!=null){ 
            var edf=document.querySelector('#'+edf_id);
            if (edf!=null){
                var id=this.getAttribute("id"); 
                var inputs=edf.querySelectorAll('[dn-input='+id+']');
                for (var i=0;i<inputs.length;i++){
                    inputs[i].addEventListener('change',e => this.inputValueChanged(e));                 
                }
            }
        }
        
        this.currentPage=1;
        this.pageSize=10;
        var ps=this.getAttribute("pagesize");
        if (ps) this.pageSize=ps;
    
        var columns=this.getAttribute('columns');
        if (!columns) return;
        
        this.columns=columns.split(",");
        
        this.template=null;
        var template=this.getAttribute('template');
        //console.log(template);
        if (!template) {
            template=null;
            //this.hideMiniActions=true;
        }
        
        if(template!=null) this.template=document.querySelector('#'+template);
        //console.log(this.template);
        
        var id=this.getAttribute('id');
        this.table_id=id;
        
        var i;
        var trans=L.table[id];
        if (trans==null){
            console.log("missing translations for TABLE "+id );
        }
        //if (trans==null) return; 
        this.columns_desc=[];
        
        for (i=0;i<this.columns.length;i++){
            if (this.columns[i]!='none') 
                this.columns_desc.push(trans[this.columns[i]]);
            else 
                this.columns_desc.push('');
        }
        if (this.getAttribute("editformid")!=null){
            this.hideMiniActions=true;
        }
        
        this.renderer();
        
        //this.addEventListener('datachanged', e => this.dataChanged(e));
                var login;
        login=document.querySelector('dn-login');
        var currentUser=login.getAttribute("currentuser");
        if (currentUser==null) {
            //console.log("table schedule for later");
            login.addEventListener('userloggedin',e => this.userloggedin(e));
            return;
        }
        
        this.loadData();
        
    }
    dataChanged(e){
        //console.log(e);
        if (this.editform!=null) this.userloggedin(e);
    }
    userloggedin(e){
        //console.log(e);
        this.loadData()
    }
    loadData(){
        /*var obj=this.querySelector('table tbody');
        if (!obj) return;
        if (obj) obj.innerHTML="";*/

        
        
        
        
        
        /*
        if (this.data && template!=null && this.data.length>0) {
            return;
        }
        */
        
        var dataurl=this.getAttribute('dataurl');
        if (!dataurl) return;
        
        //console.log(this.columns);
        //console.log(this.columns_desc);
            
        var data="";
        var TH=this;

                this.datafetch(dataurl);
                /*
                document.querySelector('dn-login').fetchUrlData(dataurl, 
                    function func_callback_ok(result){
                        TH.data=result;
                        TH.loadeddata=result.slice(0);
                        TH.currentPage=1;
                        TH.RErender();
                    }, 
                    function func_callback_err(status){
                        console.log("error");
                        console.log(status);
                    }
                )*/
        
    }
    RErender(){
        this.renderrows();
        this.renderNavigation();
    }
    renderer(){
        
        this.selectedRow=null;
        
        this.innerHTML="";
        var style=document.createElement("STYLE");
        style.innerHTML=`
                
                    .tablerow-selected td{
                        background-color: var(--table-selected-bgcolor, rgba(0,0,255,0.1));
                    }

                    dn-table table{
                        width:100%
                    }
                    
                    dn-table:not([editformid]) tbody td{
                        padding: 2px 2px 2px 2px; 
                    }
                    dn-table:not([editformid])>input{
                        display: inline-block !important;
                        width: CALC( 100% - 64px )!important;
                    }
                    dn-table table th, dn-table table td{
                        line-height: 1.42857;
                        padding: 8px;
                    }
                    dn-table[disabled="true"]>i{
                        display:none !important;
                    }    
                    dn-table tbody i{
                        height: 100%;
                    }
                    dn-table>i{
                        display: inline-block !important;
                        width: 32px !important;
                        text-align: center;
                        font-size:200%
                    }
                    dn-table table tr>td input{
                        width:100%;
                        background-color:rgba(255,255,255,1) !important;
                        border:0px !important;
                        padding-left: 4px !important;
                    }
                    
                    dn-table table tr>td dn-truefalse, dn-table table tr>td dn-truefalse li, dn-table table tr>td dn-singleselect, dn-table table tr>td dn-singleselect select{
                        background-color:rgba(255,255,255,1) !important;
                        border:0px !important;
                        margin:0px !important;
                        padding:0px !important;
                    }
                    dn-table table tr>td dn-truefalse ul{
                        padding: 8px !important;
                    }
                    dn-table .form-control[disabled], dn-table fieldset[disabled] .form-control{
                        cursor: default !important;
                    }
                    dn-table table tr>td *:disabled{
                        background-color:rgba(0,0,0,0) !important;
                        cursor: default !important;
                    }
                    dn-table table tr>td dn-truefalse[disabled], dn-table table tr>td dn-truefalse[disabled] li, dn-table table tr>td dn-singleselect[disabled], dn-table table tr>td dn-singleselect[disabled] select{
                        background-color:rgba(0,0,0,0.0) !important;
                    }
            
                    

                    dn-table table tr{
                        border-bottom-width: 1px;
                        border-bottom-style: solid;
                        border-bottom-color: var(--light-gray, rgba(220,220,220,1));
                    }
                    dn-table table tfoot tr{
                        border-bottom: none;
                    }
                    dn-table table tfoot tr td{
                       width:100% !important;
                       background-color:white !important;
                    }
                    dn-table table tr:nth-of-type(even) {
                        background-color: var(--very-light-gray, rgba(240,240,240,1));
                    }
                    dn-table table tfoot ul{
                        list-style-type:none;
                        padding:0px;
                        text-align: right;
                        line-height: 18.5px;
                        font-size:12px;
                    }
                    dn-table table tfoot ul li{
                        display: inline-block;
                        width:auto;
                        height:26px;
                        border-width:1px;
                        border-style: solid;
                        border-color: var(--light-gray, rgba(220,220,220,1));
                        text-align: center;
                        padding: 4px 10px;
                        border-radius:3px;
                        margin-left:-1px;
                    }
            
                    .sortable::after{
                        font-family: 'Glyphicons Halflings';
                        font-weight: 400;  
                        font-size:75%;
                    }
                    .sorted-none::after{
                        content: " \\e150";
                    }
                    .sorted-asc::after{
                        content: " \\e094";
                    }
                    .sorted-desc::after{
                        content: " \\e093";
                    }
                    .hide{
                        visibility: hidden;
                    }
                    .current-page{
                        background-color: var(--very-light-gray, rgba(240,240,240,1));
                    }
            
                    dn-split[active=left] [dn-right] dn-table tr,
                    dn-split[active=right] [dn-left] dn-table tr{
                        display:block
                    }
                    dn-split[active=left] [dn-right] dn-table td,
                    dn-split[active=right] [dn-left] dn-table td,
                    dn-split[active=left] [dn-right] dn-table th,
                    dn-split[active=right] [dn-left] dn-table th{
                        display:none;
                    }
                    dn-split[active=left] [dn-right] dn-table td:nth-child(1),
                    dn-split[active=right] [dn-left] dn-table td:nth-child(1),
                    dn-split[active=left] [dn-right] dn-table th:nth-child(1),
                    dn-split[active=right] [dn-left] dn-table th:nth-child(1){
                        display:inline-block;
                        width:50%;
                    }
                    dn-split[active=left] [dn-right] dn-table td:nth-child(2),
                    dn-split[active=right] [dn-left] dn-table td:nth-child(2),
                    dn-split[active=left] [dn-right] dn-table th:nth-child(2),
                    dn-split[active=right] [dn-left] dn-table th:nth-child(2){
                        display:inline-block;
                        text-align:right;
                        width:50%;
                    }
                
            `;
        this.appendChild(style);
        
        var attr;
        
        
        //<input type="text" class="form-control input-sm m-b-xs" id="filter" placeholder="Pretraga tablice">
        var input=document.createElement("INPUT");
        attr=document.createAttribute("type");
        attr.value='text';
        input.setAttributeNode(attr);
        attr=document.createAttribute("placeholder");
        attr.value=L.table.tablesearch;
        input.setAttributeNode(attr);
        input.classList.add('form-control');
        input.classList.add('input-sm');
        input.classList.add('m-b-xs');
        input.addEventListener('keyup',e => this.tablefilterchanged(e));
        this.appendChild(input);
        
        if (!this.hideMiniActions){
            
            // <i class="fa fa-asterisk"></i>
            var ii=document.createElement("I");
            ii.classList.add("fa");
            ii.classList.add("fa-asterisk");
            ii.addEventListener('click',e => this.addNewRow(e));
            this.appendChild(ii);

            // <i class="fa fa-trash"></i>
            var ii=document.createElement("I");
            ii.classList.add("fa");
            ii.classList.add("fa-trash");
            ii.addEventListener('click',e => this.deleteCurrentRow(e));
            this.appendChild(ii);
        }
        
        
        // <table id="xxx" class="footable table table-stripped" data-page-size="6" data-filter=#filter>
        var table=document.createElement("TABLE");
        this.appendChild(table);

        var thead=document.createElement("THEAD");
        table.appendChild(thead);
        
        this.renderThead();
        

        
        var tbody=document.createElement("TBODY");
        table.appendChild(tbody);
        
        var tfoot=document.createElement("TFOOT");
        table.appendChild(tfoot);
        var tr=document.createElement("TR");
        tfoot.appendChild(tr);
        var td=document.createElement("TD");
            attr=document.createAttribute("colspan");
            attr.value=(this.columns.length);
            td.setAttributeNode(attr);
        tr.appendChild(td);
        
        //////this.renderrows();
        //////this.renderNavigation();
        
        //tbody.innerHTML=html;
        //const event = new CustomEvent('datarendered', { detail: { } });
        //this.dispatchEvent(event);
    }
    renderThead(){
        var thead=this.querySelector('table thead');
        if (!thead) return;
        thead.innerHTML="";
        
        var attr;
        var tr=document.createElement("TR");
        thead.appendChild(tr);
        var td;
        var i;
        for (i=0;i<this.columns.length;i++){
            //if (this.column_types[i]!='none' || this.hideMiniActions==true){
                td=document.createElement("TH");
                td.innerText=this.columns_desc[i];
                td.classList.add('sortable');
                if (this.columns[i]==this.sortColumn){
                    if (this.currentSort='asc')
                        td.classList.add('sorted-asc');
                    else
                        td.classList.add('sorted-desc');
                }else {
                    td.classList.add('sorted-none');
                }
                attr=document.createAttribute("dn-column");
                attr.value=this.columns[i];
                td.setAttributeNode(attr);
                td.addEventListener('click',e => this.tableSortChanged(e));
                tr.appendChild(td);
            //}
        }
    }
    renderrows(){
        
        if (this.data==null) return;
            
        var tbody=this.querySelector('table tbody');
        if (!tbody) return;
        tbody.innerHTML="";
        //console.log("data length:"+this.data.length);
        //console.log("data columns:"+this.columns);
        
        var html="";
        var i;
        var j;
        var tr;
        var td;
        var span;
        var attr;
        var name,value;
        var chk;
        var input;
        
        var coltype;

        var lastVisible=this.currentPage*this.pageSize;
        var firstVisible=lastVisible-this.pageSize;
        lastVisible--;
        /*console.log("currentPage:"+this.currentPage);
        console.log("pageSize:"+this.pageSize);
        console.log("firstVisible:"+firstVisible);
        console.log("lastVisible:"+lastVisible);*/
        var template_inputs=[];
        if (this.template!=null){
            for(j=0;j<this.columns.length;j++){
                input=this.template.querySelector('[name="'+this.columns[j]+'"]');
                template_inputs.push(input);
            }
        }
        
        for(i=0;i<this.data.length;i++){
            //console.log("row data:"+JSON.stringify(this.data[i]));
            tr=document.createElement("TR");
            
            for(j=0;j<this.columns.length;j++){
                
                td=document.createElement("TD");
                name=this.columns[j];
                value=this.getClumnValue(this.data[i],this.columns[j]);
                
                if (this.template==null){
                    if (typeof value==="boolean"){
                        input=document.createElement("I")
                        input.classList.add("fa");
                        if (value==true)
                            input.classList.add("fa-check-square-o");
                        else
                            input.classList.add("fa-square-o");
                        td.appendChild(input);
                    } else {
                        td.innerText=this.getClumnValue(this.data[i],this.columns[j]);
                    }
                } else {
                    //input=this.template.querySelector('[name="'+name+'"]');
                    input=document.createElement(template_inputs[j].nodeName);
                    for(var x=0;x<template_inputs[j].attributes.length;x++){
                        input.setAttribute(template_inputs[j].attributes[x].name,template_inputs[j].attributes[x].value);
                    }
                    input.setAttribute("disabled",true);
                    input.addEventListener('change',e => this.inputValueChanged(e));
                    input.addEventListener('input',e => this.inputValueChanged(e));
                    //input=template_inputs[j];
                    
                    
                    if (input!=null){
                        
                        if(input.getAttribute("dn-object")!=null){
                            input.selected=value;
                        }
                        else
                            input.value=value;
                        
                        input.setAttribute("dn-input",this.table_id);
                        td.appendChild(input);
                    }
                }
                
                if (i<firstVisible || i>lastVisible){
                    td.classList.add('hide');
                    //console.log("hide:"+i);
                }
                tr.appendChild(td);
                
            }
            
            attr=document.createAttribute("dn-rownum");
            attr.value=i;
            tr.setAttributeNode(attr);
            attr=document.createAttribute("dn-rowkey");
            attr.value=Object.values(this.data[i])[0];
            tr.setAttributeNode(attr);
            tr.addEventListener('click',e => this.rowSelected(e));
            tbody.appendChild(tr);
        }
    }
    inputValueChanged(e){
        //console.log(this.getAttribute("id"));
        //console.log(e);
        var itm=e.target;
        var name=itm.getAttribute("name");
        var newValue=itm.value;
        //if (typeof newValue==='undefined') newValue=itm.getAttribute("value");
        if (typeof newValue==='undefined') newValue=itm.data;


        if (name==null) {
            //console.log("problem");
            return;
        }
        var names=name.split(".");
        
        try{
            var keyval=e.detail.key;
            if (typeof keyval==='undefined') keyval='';
            
            var fieldname=e.detail.name;
            if (keyval!=null && typeof fieldname!=='undefined'){
                var fieldname=e.detail.name;
                var d=e.detail.data;
                var myname=this.getAttribute("name");
                var pkname=this.getAttribute("primary_key");
                var dt=this.data;
                var i=0;
                if (this.selectedRow!=null) i=this.selectedRow;
                for (;i<dt.length;i++){
                    var dt_pk_val=dt[i][pkname];
                    if (typeof dt_pk_val==='undefined') dt_pk_val='';
                    if (dt_pk_val==keyval){
                        dt[i][fieldname]={};
                        dt[i][fieldname]=d;
                        const event = new CustomEvent('change', { detail: { data: this.data } });
                        this.dispatchEvent(event);
                        break;
                    }
                }
                //const event = new CustomEvent('change', { detail: { key: this.parent_key, name:myname, data: this.data } });
                //this.dispatchEvent(event);
                return;
            }
        }catch (err){
            //console.log(err);
        }
        var rownum=itm.getAttribute("dn-rownum");
        while(rownum==null){
            itm=itm.parentNode;
            if (itm==null) return;
            //console.log(itm);
            rownum=itm.getAttribute("dn-rownum");
            if (itm.nodeName=="HTML") return;
        }

        //console.log("newValue");
        //console.log(newValue);
        
        //console.log(rowkey);
        //console.log(name);
        
        e = e || window.event;
        if (typeof e.stopPropagation != "undefined") {
            e.stopPropagation();
            e.preventDefault();
        } else {
            e.cancelBubble = true;
        }
        
        var i,j,d,kv,keys;
        
        i=rownum*1;
        d=this.data[i];
        kv=Object.values(d)[0];
        var obj={};
        this.addValueToObject(obj,name,newValue);
        d[names[0]]=obj[names[0]];
        //this.setAttribute("value",JSON.stringify(this.data));
        
        //const event = new CustomEvent('change', { detail: { id:this.parrentkey, data: this.data } });
        var myname=this.getAttribute("name");
        const event = new CustomEvent('change', { detail: { key: this.parent_key, name:myname, data: this.data } });
        this.dispatchEvent(event);
        
    }
    deleteCurrentRow(e){
        //console.log(e);
        //console.log(this.selectedRow);
        if (this.selectedRow==null) return;
        if (this.data.length<this.selectedRow) return;
        
        this.data.splice(this.selectedRow, 1);
        
        const event = new CustomEvent('change', { detail: { data: this.data } });
        this.dispatchEvent(event);
        
        //this.dataChanged(null);
        this.RErender();
    }
    addNewRow(e){
        //console.log(e);
        var newrow={};
        /*
        columns;
        columns_desc;
        column_types;
        */
       var i;
       for(i=0;i<this.columns.length;i++){
           var obj={}
           this.addValueToObject(obj,this.columns[i],'');
           //console.log(obj);
           var cname=(this.columns[i]).split(".");
           newrow[cname[0]]=obj[cname[0]];
           //console.log(newrow);
       }
        
        //console.log(newrow);
        //console.log(this.data);
        //console.log(this.data.length);
        if (this.data==null) this.data=new Array();
        if (this.data.length==0){
            this.data.push(newrow);
        }else{
            try{
                this.data.splice(0, 0, newrow);
            }catch(err){
                this.data=new Array();
                this.data.push(newrow);
            }
        }
        /*
        if (this.data==null || this.data.length==0) {
            this.data=[];
            this.data.push(newrow);
        } else {
            this.data.splice(0, 0, newrow);
        }*/

        this.dataChanged(null);
        this.RErender();
        
        var e={};
        var idd=this.getAttribute("id");
        var tr=this.querySelector("#"+idd+" table tbody tr");
        e["target"]=tr;
        this.rowSelected(e)
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
    getClumnValue(data,column){
        var columns=column.split(".");
        var i;
        var d=data;
        for (i=0;i<columns.length;i++){
            d=d[columns[i]];
        }
        return d;
    }
    renderNavigation(){
        
        if (this.data==null) return;
        //console.log(this.getAttribute("id")+" rendering navigation");
        var attr;
        var td=this.querySelector('table tfoot td');
        if (td==null) return;
        td.innerHTML="";
        
        var ul=document.createElement("UL");
        td.appendChild(ul);
        var li;
        var totalCount=this.data.length;
        
        //console.log(this.getAttribute("id")+" tc="+totalCount+" pgsize="+this.pageSize);
        if (totalCount<=this.pageSize) return;
        
        var pages=1.0*totalCount/this.pageSize;
        //console.log(pages);
        var i;
        for (i=0;i<pages;i++){
            li=document.createElement("LI");
            li.innerText=(i+1);
            if ((i+1)==this.currentPage)
                li.classList.add('current-page');
            attr=document.createAttribute("dn-pagenum");
            attr.value=(i+1);
            li.setAttributeNode(attr);
            li.addEventListener('click',e => this.pageSelected(e));
            ul.appendChild(li);
        }
    }
    
    pageSelected(e){
        //console.log(e);
        //console.log(e.target);
        var pagenum=e.target.getAttribute("dn-pagenum");
        //console.log(pagenum);
        this.currentPage=pagenum;
        this.RErender();
    }
    
    rowSelected(e){
        //console.log(e);
        var color=this.getAttribute("selectedcolor");
        if (!color) color="var(--table-selected-bgcolor,PowderBlue)";
        
        var row=e.target;
        
        while (row.nodeName!="TR"){
            row=row.parentElement;
            if (row.nodeName=="HTML") // failed
                return;
        }
        
        var rnum=row.getAttribute("dn-rownum");
        if (rnum){
            rnum=rnum*1;
            
            var selecteddata=this.data[rnum];
            this.selectedRow=rnum;
            
            this.notifyChildren();
            
            var tr=this.querySelectorAll('table tbody tr');
            var i;
            
            for (i=0;i<tr.length;i++){
                if (i==rnum){
                    
                    tr[i].classList.add('tablerow-selected');
                    var id=this.getAttribute('id');
                    var inputs=tr[i].querySelectorAll('[dn-input='+id+']');
                    var k;
                    for(k=0;k<inputs.length;k++){
                        inputs[k].removeAttribute("disabled");
                    }
                }else{
                    
                    tr[i].classList.remove('tablerow-selected');

                    var id=this.getAttribute('id');
                    var inputs=tr[i].querySelectorAll('[dn-input='+id+']');
                    var k;
                    for(k=0;k<inputs.length;k++){
                        inputs[k].setAttribute("disabled",true);
                    }
                }
            }
            
            var ed_id=this.getAttribute("editformid");
            
            if (ed_id){
                
                var edf=document.querySelector('#'+ed_id);
                if (edf) {
                    //edf.setAttribute("value",JSON.stringify(selecteddata));
                    edf.selected=selecteddata;
                    //console.log("postavio data");
                    //console.log(edf);
                }
            }
        }
    }
    notifyForEmpty(){
        var edf_id=this.getAttribute('dn-input');
        if (edf_id==null) return;
        var edf=document.querySelector('#'+edf_id);
        if (edf==null) return;
        var id=this.getAttribute('id');
        var inputs=edf.querySelectorAll('[dn-input='+id+']');
        for (var i=0;i<inputs.length;i++){
            if (!this.isOwned(this, inputs[i])){
                var dnfetch=inputs[i].getAttribute("dn-fetch");
                inputs[i].setAttribute("disabled",this.getAttribute("disabled"));
                if (dnfetch!=null){
                    var pkcolumn=this.getAttribute("primary_key");
                    var pkval="";
                    inputs[i].parentkey=pkval;
                }else{
                    var name=inputs[i].getAttribute("name");
                    var val="";
                    inputs[i].value=val;
                    try{
                        inputs[i].selected={};
                    }catch(err){;}
                }
            }
        }
            
    }
    
    notifyChildren(){
        //console.log(this.data[this.selectedRow]);
        if (this.selectedRow==null) {
            this.notifyForEmpty();
            return;
        }
        var selectedData=this.data[this.selectedRow];
        //console.log("selectedData"+this.getAttribute("id"));
        //console.log(selectedData);
        if (selectedData==null) return;
        var id=this.getAttribute('id');
        
        var edf_id=this.getAttribute('dn-input');
        if (edf_id==null) return;
        var edf=document.querySelector('#'+edf_id);
        if (edf==null) return;
        
        var inputs=edf.querySelectorAll('[dn-input='+id+']');
        //console.log(id);
        //console.log(inputs);
        var i,val;
        for (i=0;i<inputs.length;i++){
            if (!this.isOwned(this, inputs[i])){
                inputs[i].setAttribute("disabled",this.getAttribute("disabled"));
                var dnfetch=inputs[i].getAttribute("dn-fetch");
                
                if (dnfetch!=null){
                    var pkcolumn=this.getAttribute("primary_key");
                    var pkval=this.getClumnValue(selectedData,pkcolumn);
                    inputs[i].parentkey=pkval;
                }else{
                    var name=inputs[i].getAttribute("name");
                    val=this.getClumnValue(selectedData,name);
                    //console.log(name);
                    //console.log(val);
                    if((typeof selectedData[name])=="object"){
                        if (val==null) val=new Array();
                        inputs[i].selected=val;
                    }
                    else{
                        if (val==null) val="";
                        if (inputs[i].nodeName.startsWith("DN-")){
                            inputs[i].value=this.getClumnValue(selectedData,name);
                        }else{
                            inputs[i].value=this.getClumnValue(selectedData,name);
                        }
                    }
                }
            }
        }
    }
    
    isOwned(parent, child) {
        var node = child.parentNode;
        while (node != null) {
            if (node == parent) {
                return true;
            }
            node = node.parentNode;
        }
        return false;
    }
    
    tableSortChanged(e){
        //console.log(e);
        //console.log(e.target);
        var th=e.target;
        var tr=th.parentNode;
        var cnodes=tr.childNodes;
        var column=th.getAttribute("dn-column");
        var i;
        for(i=0;i<cnodes.length;i++){
            cnodes[i].classList.remove('sorted-none');
            cnodes[i].classList.remove('sorted-asc');
            cnodes[i].classList.remove('sorted-desc');
        }
        if (this.sortColumn==column){
            if (this.currentSort=="asc")
                this.currentSort="desc";
            else
                this.currentSort="asc";
        }else{
            this.currentSort="asc";
            this.sortColumn=column;
        }
        var TH=this;
        for(i=0;i<cnodes.length;i++){
            if (cnodes[i].getAttribute("dn-column")==column){
                if (this.currentSort=="asc"){
                    cnodes[i].classList.add('sorted-asc');
                    
                    this.data.sort(function(a, b) {
                        var aa=TH.getClumnValue(a,column);
                        var bb=TH.getClumnValue(b,column);
                        if (typeof aa === "string")
                            return (aa.toUpperCase()>bb.toUpperCase()?1:-1);
                        else
                            return (aa>bb?1:-1);
                    });
                }
                else{
                   cnodes[i].classList.add('sorted-desc');

                   this.data.sort(function(a, b) {
                        var aa=TH.getClumnValue(a,column);
                        var bb=TH.getClumnValue(b,column);
                        
                        if (typeof aa === "string")
                            return (aa.toUpperCase()>bb.toUpperCase()?-1:1);
                        else
                            return (aa>bb?-1:1);
                    });
                }
            } else {
                cnodes[i].classList.add('sorted-none');
            }
        }
        this.RErender();
    }
    tablefilterchanged(e){
        //console.log(e);
        this.data=this.loadeddata.slice(0);
        var filter=e.target.value.toUpperCase();
        
        var i,j, val,found;
        var row;
        for (i=this.data.length-1;i>=0;i--){
            row=this.data[i];
            //console.log(row);
            found=false;
            for(j=0;j<this.columns.length;j++){
                //console.log(this.columns[j]);
                //console.log(row[this.columns[j]]);
                val=(''+row[this.columns[j]]).toUpperCase();
                if (val.indexOf(filter)>-1){
                    // remove
                    found=true;
                }
            }
            if (!found) {
                //console.log("removed");
                this.data.splice(i, 1);
            }
                
        }
        this.currentPage=1;
        this.RErender();
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


if (!customElements.get('dn-table')) {
    customElements.define('dn-table', Table);
    //console.log(customElements.get('dn-menu'));
}
