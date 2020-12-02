/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import {L} from './localization.js';

export default class LoginTemplate{
    
    login;
    
    constructor(login){
        this.login=login;
    }
    render(){
        return `
            ${this.css()}
            ${this.html()}
        `;
    }
    mapDOM(scope){
        return {
            thumb: scope.getElementById('thumb')
        };
    }
    html(){
        //var username=this.login.getAttribute('currentuser');
        if (this.login.currentUser==null)
            return `
            <div class="modal-wrapper">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            
                            <h3 class="m-t-none m-b">${L.login.login}</h3>
                            <!--div class="title">${L.login.login}:</div-->
                            
                            <form id="loginform"> 
                                <label for="username">${L.login.username}:</label> 
                                <input type="text" name="username" id="username" class="form-control"><br /> 
                                <label for="username">${L.login.password}:</label> 
                                <input type="password" name="password" id="password" class="form-control"><br /> 
                                <input id="loginbutton" type="button" value="${L.login.login_short}" class="btn btn-sm btn-primary float-right m-t-n-xs">
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            `;
        else
            var user=this.login.currentUser;
            return "";
            /*return `
                <li style="margin-right:0px;">
                    <!--img alt="image" class="rounded-circle" src="img/profile_small.jpg"-->
                    <a id="logoutlink" style="padding:0px;" title="${L.login.logout}">
                        <span style="color: #999c9e;" class="m-r-sm font-bold">${user.name} ${user.surname}</span>
                    </a>
                </li>
            `;*/
            /*
            return `
                <div class="dropdown profile-element nav-header">
                    <!--img alt="image" class="rounded-circle" src="img/profile_small.jpg"-->
                    <a class="xxdropdown-toggle" href="#" id="logoutlink" title="${L.login.logout}">
                        <span class="block m-t-xs font-bold">${user.name} ${user.surname}</span>
                        <!--span class="text-muted text-xs block">Art Director <b class="caret"></b></span-->
                    </a>
                </div>
            `;
            */
    }
    css(){
        //var username=this.login.getAttribute('currentuser');
        if (this.login.currentUser==null)
            return `
                <style>
                    a{
                        paddin:0px;
                    }
                    li{
                        margin-right: 0px !important;
                    }
                    .modal-wrapper{
                        position: fixed;
                        left:0;
                        right:0;
                        top:0;
                        bottom:0;
                        background-color: black;
                        z-index: 2020;
                        transform: translatex(1px);;
                    }
                    
                </style>
            `;
        else
            return `
            `;
    }
}

