/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

export default class Table extends HTMLElement{
    
    data;
    columns;
    
    active; // contol becomes active after if loads data from server
    
    attributeChangedCallback(name, oldVal, newValue){
    }
    
    constructor() {
        super();     
    }
    connectedCallback(){
        
        var ed_id=this.getAttribute("editformid"); 
        if (ed_id){
            var edf=document.querySelector('#'+ed_id);
            if (edf) {
                edf.addEventListener('datachanged',e => this.dataChanged(e));
            }
        }
        
        var login;
        login=document.querySelector('dn-login');
        
        var currentUser=login.getAttribute("currentuser");
        //console.log(currentUser);
        
        if (currentUser==null) {
            //console.log("table schedule for later");
            login.addEventListener('userloggedin',e => this.userloggedin(e));
            return;
        }
        
        
        //this.addEventListener('datachanged', e => this.dataChanged(e));
        
        this.loadData();
    }
    dataChanged(e){
        console.log(e);
        this.userloggedin(e);
    }
    userloggedin(e){
        console.log(e);
        this.loadData()
    }
    loadData(){
        var obj=this.querySelector('table tbody');
        if (!obj) return;
        if (obj) obj.innerHTML="";
        
        var dataurl=this.getAttribute('dn-dataurl');
        if (!dataurl) return;
        
        var columns=this.getAttribute('dn-columns');
        if (!columns) return;
        
        this.columns=columns.split(",");
        //console.log(this.columns);
            
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
                    const event = new CustomEvent('dataloaded', { detail: { data: result } });
                    TH.dispatchEvent(event);
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
    renderer(){
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
        for(i=0;i<this.data.length;i++){
            //console.log("row data:"+JSON.stringify(this.data[i]));
            tr=document.createElement("TR");
            for(j=0;j<this.columns.length;j++){
                //console.log("column name:"+this.columns[j]);
                td=document.createElement("TD");
                //span=document.createElement("SPAN");
                td.innerText = this.data[i][this.columns[j]];
                //td.appendChild(span);
                tr.appendChild(td);
            }
            attr=document.createAttribute("dn-rownum");
            attr.value=i;
            tr.setAttributeNode(attr);
            attr=document.createAttribute("dn-rowkey");
            attr.value=Object.values(this.data[i])[0];
            tr.setAttributeNode(attr);
            //tr.setAtribute("dn-rownum",i);
            //tr.setAtribute("dn-rowkey",this.data[i][0]);
            tr.addEventListener('click',e => this.rowSelected(e));
            tbody.appendChild(tr);
            /*html+='<tr>';
            for(j=0;j<this.columns.length;j++){
                console.log("column name:"+this.columns[j]);
                html+=`
                    <td>${this.data[i][this.columns[j]]}</td>
                `;    
            }
            html+='</tr>';*/
        }
        //tbody.innerHTML=html;
        const event = new CustomEvent('datarendered', { detail: { } });
        this.dispatchEvent(event);
    }
    rowSelected(e){
        //console.log(e);
        var color=this.getAttribute("selectedcolor");
        if (!color) color="var(--table-selected-bgcolor,PowderBlue)";
        var row=e.target;
        //console.log(row);
        if (row.nodeName=='TD') row=row.parentElement;
        //console.log(row);
        var rnum=row.getAttribute("dn-rownum");
        //console.log(rnum);
        var rkey=row.getAttribute("dn-rowkey");
        //console.log(rkey);
        
        if (rkey){
            var selecteddata;
            var j,len;
            var pk=Object.keys(this.data[0])[0];
            //console.log("pk="+pk);
            len=this.data.length;
            for(j=0;j<len;j++){
                if (rkey==this.data[j][pk]){
                    selecteddata=this.data[j];
                    //console.log(selecteddata);
                    break;
                }
            }
           
            var tr=this.querySelectorAll('table tbody tr');
            //tr.style.backgroundColor="lightBlue";
            //tr.setAttribute("style","backgroundColor:lightblue");
            var i;
            //console.log(tr.length);
            
            for (i=0;i<tr.length;i++){
                //console.log(i);
                //var td=tr[i].querySelector('td');
                var rk=tr[i].getAttribute("dn-rowkey");
                //console.log(rkey+"?"+rk)
                if (rkey==rk){
                    tr[i].classList.add('tablerow-selected');
                    //tr[i].setAttribute("style","background-color:"+color);
                }
                else{
                    tr[i].classList.remove('tablerow-selected');
                    //tr[i].setAttribute("style","");
                }
            }
            
            const event = new CustomEvent('rowselected', { detail: { data: selecteddata } });
            var ed_id=this.getAttribute("editformid");
            //console.log(ed_id);
            if (ed_id){
                var edf=document.querySelector('#'+ed_id);
                //console.log(edf);
                if (edf) edf.dispatchEvent(event);
            }
            this.dispatchEvent(event);
        }
        
        
        
    }
}


if (!customElements.get('dn-table')) {
    customElements.define('dn-table', Table);
    //console.log(customElements.get('dn-menu'));
}
