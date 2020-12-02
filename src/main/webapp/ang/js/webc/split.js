/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//import Split from './menu_template.js';

export default class Split extends HTMLElement{
    
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
    
    dnleft;
    dnright;
    render(){
        this.setAttribute("active","left");
        //var id=this.getAttribute("id");
        //var style=this.querySelector("style");
        //if (style==null){
            var style=document.createElement("STYLE");
            this.insertBefore(style, this.childNodes[0]); 
            
        //}
        style.innerHTML=`
            dn-split{
                display:block;
                margin-bottom:20px;
            }
            dn-split [dn-left],dn-split [dn-right]{
                display:inline-block;
                padding:0px;
                position: relative;
                vertical-align:top;
            }
            dn-split[active=left] [dn-left]{
                width: calc( 70% - 2px - 10px);
                margin-right:10px;
                transition-duration: 0.5s;
            }
            dn-split[active=left] [dn-right]{
                width: calc( 30% - 2px );
                transition-duration: 0.5s;
            }
            dn-split[active=right] [dn-left]{
                width: calc( 20% - 2px);
                margin-right:10px;
                transition-duration: 0.5s;
            }
            dn-split[active=right] [dn-right]{
                width: calc( 80% - 2px - 10px );
                transition-duration: 0.5s;
            }
            dn-split [dn-left]>label:first-child, dn-split [dn-right]>label:first-child{
                position: absolute;
                right: 20px;
                top: -14px;
                padding: 0px 16px 4px 16px;
                background-color: white;
                border-left: 1px solid rgb(229, 230, 231);
                border-right: 1px solid rgb(229, 230, 231);
                border-top: 1px solid rgb(229, 230, 231);
                margin: 0px;
                line-height: 15px;
                height: 15px;
                font-size: 10px;
                text-transform: uppercase;
                border-top-left-radius: 10px;
                border-top-right-radius: 10px;
            }
        `;
         var dnlabelleft=this.querySelector("dn-split>[dn-left]>label:nth-child(1)");
         dnlabelleft.addEventListener('click',e => this.leftClicked(e));
         var dnlabelright=this.querySelector("dn-split>[dn-right]>label:nth-child(1)");
         dnlabelright.addEventListener('click',e => this.rightClicked(e));
    }
    set activatepannel(panel){
        //console.log(panel);
        if (panel=="left")
            this.setAttribute("active","left");
        else
            this.setAttribute("active","right");
    }
    leftClicked(e){
        /*console.log("left clicked");
        console.log(e);*/
        this.setAttribute("active","left");
    }
    rightClicked(e){
        /*console.log("right clicked");
        console.log(e);*/
        this.setAttribute("active","right");
    }
}

if (!customElements.get('dn-split')) {
    customElements.define('dn-split', Split);
}