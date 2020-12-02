/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import {L} from './localization.js';

export default class CustomCOmponentTemplate{
    
    constructor(){
        
    }
    render(){
        return `
            ${this.css()}
            ${this.html()}
        `;
    }
    html(){
        
            return `
                <div class="dropdown profile-element nav-header">
                    <a class="xxdropdown-toggle" href="#" id="logoutlink" title="${L.login.logout}">
                        <span class="block m-t-xs font-bold">${user.name} ${user.surname}</span>
                    </a>
                </div>
            `;
    }
    css(){
        return `
            <style>                    
            </style>
        `;
    }
}