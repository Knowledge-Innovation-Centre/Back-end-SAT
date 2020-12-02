/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

export default class Modal extends HTMLElement{
    static get observedAttributes(){
        return ['disabled'];
    }
    
    attributeChangedCallback(name, oldVal, newValue){
        if (name=="disabled" && oldVal!=newValue ){
            this.adjust();
        } 
    }
    
    constructor() {
        super();
    }
    
    connectedCallback(){
        this.renderer();
    }
    
    adjust(){
        
    }
    renderer(){
        var style=document.createElement("STYLE");
        style.innerHTML=`
            dn-modal[disabled]{
                display: none;
            }
            dn-modal{
               display: block;
               background-color: rgba(0,0,0,0.5);
               position: absolute;
               left: 0px;
               right: 0px;
               top: 0px;
               bottom: 0px;
            }
            dn-modal>div{
                position: absolute;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
            }
            dn-modal .ibox-title i{
                font-size: 120%;
                float: right;
                margin-top: -2px;
            }
        `;
        this.appendChild(style);
        var closebutton=document.createElement("I")
        closebutton.addEventListener('click',e => this.closeButtonClicked(e));
        closebutton.innerHTML='<i class="fa fa-window-close"></i>';
        var title=this.querySelector(".ibox-title");
        title.appendChild(closebutton);
    }
    
    closeButtonClicked(e){
        this.setAttribute("disabled",true);
    }
}

if (!customElements.get('dn-modal')) {
    customElements.define('dn-modal', Modal);
    //console.log(customElements.get('dn-menu'));
}

