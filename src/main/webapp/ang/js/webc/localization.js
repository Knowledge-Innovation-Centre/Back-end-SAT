/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import {L_hr} from './localization_hr.js';

//console.log("init localization");

var language="en";

var lang;
var node=document.querySelector('html');
if (node) {
    if (node.hasAttribute("lang")) 
        language=node.getAttribute("lang");
}
switch (language){
    case 'hr':
        lang=JSON.parse(L_hr);
        break;
    default:
        lang=JSON.parse(L_hr);
        break;
}
export {lang as L};

