import { ConfigVarContext } from './context/ConfigVarContext';
import React, { useState, useContext } from "react";
import ReactDOMServer from 'react-dom/server';

const defaultCurency = {name:"USD",code:"$",main:"yes",type:"left",rate:"1"};
const defaultLang = "en";
const defaultAdminLang = "en";

export const headers=()=>{
  var id = localStorage.getItem("id");
  var token = getCookie("tokenauth");
  //var userIdentifier = random(40) getCookie("tokenauth");

  return { headers :{ id: id,
                     //Access-Control-Allow-Origin: "*",
                     //Access-Control-Allow: "*",
                     //Access-Control-Allow-Methods: "GET,HEAD,OPTIONS,POST,PUT",
                     //Access-Control-Allow-Headers: "*",
                     Authorization: token !=null && token !="" ? 'Bearer ' + token : '',
                     Accept: '*',
                     transports: ['websocket', 'polling', 'flashsocket']} };
}

export const innerHeaders=(singleToken="")=>{
  var id = localStorage.getItem("id");
  var token = getCookie("tokenauth");

  return  { Authorization: (token !=null && token !="" ? 'Bearer ' + token : ''),
            connectionId: id,
            singleToken: singleToken,
            AccessControlAllowOrigin: "*",
            Accept: '*',
            //Upgrade: 'websocket', // Add the Upgrade header
            //Connection: 'Upgrade',
            //'Sec-WebSocket-Version': '13',
            transports: ['websocket', 'polling', 'flashsocket']//
            }  ;
}

export const stringifyIfJSON=(str)=>{
        if (Array.isArray(str) || typeof str === 'object') {
            try {
                JSON.stringify(str);
                return JSON.stringify(str);
            } catch (e) {
                return str.toString();
            }
        } else {
            try {
              JSON.parse(str);
               return JSON.stringify(str);
            } catch (e) {
               return str;
            }
        }
 }

export const logout=(e)=>{
     e.preventDefault();
     sessionStorage.clear();
     localStorage.clear();
     eraseCookie("tokenauth");
     window.location.reload();
}

  export const successIcon=()=>{
        try{
          document.getElementById("successDone").style.display = "block";

          setTimeout(() => {
            document.getElementById("successDone").style.display = "none";
           }, 4000);

        }catch(err){}
      }

   export const showLoad=()=>{
        try{
          document.getElementById("ajaxGif").style.display = "block";
        }catch(err){}
      }

   export const hideLoad=()=>{
        try{
          document.getElementById("ajaxGif").style.display = "none";
        }catch(err){}
      }


   export const showAlert=(text="")=>{
        try{
          document.getElementById("mini-modal-alert").style.display = "block";
          document.getElementsByTagName('body')[0].style.overflowY = "hidden"  ;
          var tNew = text.replace(/(?:\r\n|\r|\n)/g, '<br/>');
          tNew = tNew.replace(/  /g,"&nbsp;&nbsp;");
          document.getElementById("load_content_mini").innerHTML = tNew;
        }catch(err){ alert(err);
        }
   }

  export const handleError=(error="")=>{
      if(String(error).includes("status code 401")){
           alert(error);
        }else{
           alert(error);
        }
      }

  export const getLanguage=(filename = "")=>{
       const extension = filename.split('.').pop();
       if(extension=="js"){
         return 'javascript';
       }else if(extension=="php"){
         return 'php';
       }else if(extension=='java'){
          return 'java';
       }else if(extension=='html'){
          return 'html';
       }else if(extension=='css'){
          return 'css';
       }else if(extension=='XML' || extension=='xml'){
          return 'xml';
        }else{
          return 'php';
       }
    }


    export const getUrlParam =(name)=>{
          name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
          var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
          var results = regex.exec(window.location.hash.substr(1));
       return  results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }

    export const getPageNr =()=>{
      return getUrlParam("page") ==""? 0 : parseInt(getUrlParam("page")) - 1
    }

    export const goTo =(e)=>{
      e.preventDefault();
      window.location.href = e.target.href;
      window.location.reload();
    }

    export const setInput=(objArray )=>{

        for(var key in objArray){
              //alert(key);
               var element = document.getElementsByName(key) ;
               if(element.length>0){
                    var value = typeof objArray[key] !="undefined" ? objArray[key] : "" ;
                    var objElement = element[0].tagName =="META" ? element[1] : element[0];

                     if(typeof objElement !="undefined"){

                     if((objElement.tagName =="TEXTAREA" || objElement.tagName =="INPUT" ||
                        objElement.tagName =="SELECT") && objElement.type != "checkbox") {
                          reactSetVal(objElement, value);

                      } else if(objElement.type =="checkbox"){
                           objElement.checked= value=="yes"? true:false;

                           //reactSetCheckboxRadio(objElement, true);
                     }
                   }
               }

           }
   }

  /**
   In react to set value of input or textarea its not the same as js it need to go with this procedure
  */
  export const reactSetVal=(objElement, val)=>{
    if(typeof objElement !="undefined"){
         let currentVal = objElement.value;
         objElement.value = val;
         let typeInput = objElement.tagName =="TEXTAREA" ? "textarea" :"input";
         let event = new Event(typeInput, {target :objElement, bubbles: true});

         event.simulated = true;
         let tracker = objElement._valueTracker;
         if(tracker){
            tracker.setValue(currentVal);
          }
         objElement.dispatchEvent(event);
     }
   }

   export const reactSetCheckboxRadio=(objElement, eventName)=>{
     let currentVal = objElement.value;

     let event = new Event(eventName, {target :objElement, bubbles: true});

     event.simulated = true;
     let tracker = objElement._valueTracker;
     if(tracker){
         tracker.setValue(currentVal);
      }
     objElement.dispatchEvent(event);
   }

   export const setValueByClass =(className, val)=>{
            if(Array.isArray(val)){
               for(var i=0; i< val.length; i++){
                 if(val[i] !="" && val[i] !=null ){
                  var inputElement = document.getElementsByClassName(className+"_"+val[i]);
                   if(inputElement.length > 0)
                      inputElement[0].checked=true
                   }
                }
            }else{
                var inputElement = document.getElementsByClassName(className);
                if(inputElement.length > 0)
                      reactSetVal(inputElement[0], val);
            }

  }



 export const toJsonObj =(json)=>{
   return isJson(json) ? JSON.parse(json) : {};
 }

 export const toJsonArr =(json)=>{
    return isJson(json) ? JSON.parse(json) : [];
  }

export const random=(length)=>{
      var result           = '';
      var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      var charactersLength = characters.length;
      for ( var i = 0; i < length; i++ ) {
        result += characters.charAt(Math.floor(Math.random() *
                  charactersLength));
     }
     return result;
  }

 export const isNumeric=(value)=> {

    if(value=="")
       return false;

    value = value.replace(",","");
    value = value.replace(".","");

    return /^-?\d+$/.test(value);
}

 export const isJson=(str)=> {

     if(str != null && isNumeric(str.toString()) )
         return false;

     try {
          if(str ==null)
               return false;

         JSON.parse(str);
     } catch (e) {
         return false;
     }
     return true;
 }

  export const fromJson=(json)=> {
       if(isJson(json)){
         return JSON.parse(json);
       }else{
         return {};
       }
  }





   export const showEditFields=(e, mainClass="")=> {
       e.preventDefault();

       var main = document.getElementsByClassName(mainClass);
       var hiddensClasses = main[0].getElementsByClassName("hide_edit");
       var namesClasses = main[0].getElementsByClassName("edit_text");

        for(var i = 0; i< hiddensClasses.length; i++){
            var style = hiddensClasses[i].style.display ;
            style = style =="none" || style =="" || typeof style =="undefined" ? "none":"block";
            hiddensClasses[i].style.display = style =="block" ? "none": "block";

           if(typeof namesClasses[i] !="undefined")
            namesClasses[i].style.display = style ;
        }

    }


    export const objToHtml =(obj)=>{
       return ReactDOMServer.renderToStaticMarkup(
        <>
          {obj}
        </>
       )
     }

     export const showModal =(idModal)=>{
           var modal = document.getElementById(idModal);
                     modal.style.display = "block";

                 var body = document.body;
                 body.classList.add("modal-open");
      }


     export const showDivs =(className="")=>{
        var elements = document.getElementsByClassName(className);
        for(var i=0;i<elements.length;i++){
          elements[i].style.display = "inline";
        }
     }

   export const randId=()=>{
     return "id"+Math.round(new Date().getTime() + (Math.random() * 100000));
   }

    export const serialiseByDiv=( mainClass="")=> {
       var main = document.getElementsByClassName(mainClass);
       var inputs = main[0].getElementsByTagName('input');
       var select = main[0].getElementsByTagName('select');
       var textarea = main[0].getElementsByTagName('textarea');

       var obj = {};
       var accept=["text","hidden","number","password"];
        for(var i = 0; i< inputs.length; i++){
            var type = inputs[i].type ;
            var name = inputs[i].name;

            var split = name.split("-");


            if(type=="checkbox"){
              if(inputs[i].checked){
                if(split.length > 1)
                  var obj = obj

                obj[inputs[i].name] = inputs[i].value;

                }

            }else if(type=="radio"){
               // if(inputs[i].checked)
               //    obj[inputs[i].name] = inputs[i].value;

             }else if(accept.includes(type)){


                obj[inputs[i].name] = inputs[i].value;
            }
        }

        for(var i = 0; i< select.length; i++){
             obj[select[i].name] = select[i].value;
        }

        for(var i = 0; i< textarea.length; i++){
             obj[textarea[i].name] = textarea[i].value;
        }

       return obj;
    }


 export const emptyDataCheck=(data='')=>{
    if (typeof data === "undefined" || data ==""){
        return false;
    }else{
       return true ;
    }
  }

 export const undCheck=(data ='')=>{
      if (typeof data === "undefined"){
          return '';
      }else{
         return data;
      }
  }

 export const wch=(data='',dataDefault='')=>{
    if (typeof data === "undefined"){
        return dataDefault;
    }else{
       return data !=''? data :dataDefault ;
    }
 }

 export const getSelectedRows=(className='checkboxBulk')=>{
       var elementsBulk = document.getElementsByClassName(className);
       var ids=[];
       for(var i=0; i<elementsBulk.length; i++){
        if(elementsBulk[i].checked)
          ids.push(elementsBulk[i].value);
       }
     return ids;
 }


 export const bulkUncheckAll=(cssClass="")=> {
     var bulkElements = document.getElementsByClassName(cssClass);
     for(var i=0;i<bulkElements.length;i++){
       bulkElements[i].checked= false;
     }
 }

 export const  convertUnixPermissionToNumber =(permission="")=>{
          const pNr ={"r":4, "w":2, "x":1, "-":0, "s":0, "S":0}
          var nr=0;
          var dif = permission.length > 9 ? 0:1;
          var converted = [];
          for (var i = 1; i < (permission.length + dif); i++) {
              nr =pNr.hasOwnProperty(permission[(i-dif)]) ? pNr[permission[(i-dif)]] + nr : nr;
              if(i%3==0){
                converted.push(nr);
                nr = 0;
              }
           }

      return converted.join("");
 }

export const showThisPart=(e, idPart="")=> {
   document.getElementById(idPart).style.display=e.target.checked ? "block":"none";
 }

export const hideDiv=(e, idDiv="")=> {
   e.preventDefault();
   document.getElementById(idDiv).style.display= "none";
 }

export const generatePassword=(length = 12)=> {
      var charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@",
          retVal = "";

      for (var i = 0, n = charset.length; i < (length/2); ++i) {
          retVal += charset.charAt(Math.floor(Math.random() * n));
      }
      retVal += "@UY";

      for (var i = 0, n = charset.length; i < (length/2); ++i) {
                retVal += charset.charAt(Math.floor(Math.random() * n));
      }
      return retVal;
}

export const normalizePozition = (mouseX, mouseY, scope, contextMenu) => {
        // ? compute what is the mouse position relative to the container element (scope)

       let {
          left : scopeOffsetX,
          top  : scopeOffsetY,
        } = scope.getBoundingClientRect();

        scopeOffsetX = scopeOffsetX < 0 ? 0 : scopeOffsetX;
        scopeOffsetY = scopeOffsetY < 0 ? 0 : scopeOffsetY;

        const scopeX = mouseX - scopeOffsetX;
        const scopeY = mouseY ;//mouseY - scopeOffsetY

        // ? check if the element will go out of bounds
        const outOfBoundsOnX =
          scopeX + contextMenu.clientWidth > scope.clientWidth;

        const outOfBoundsOnY =
          scopeY + contextMenu.clientHeight > window.innerHeight;

        let normalizedX = mouseX;
        let normalizedY = mouseY;

        // ? normalize on X
        if (outOfBoundsOnX) {
          normalizedX =
            scopeOffsetX + scope.clientWidth - contextMenu.clientWidth;
        }

        // ? normalize on Y
        if (outOfBoundsOnY) {
            normalizedY = window.innerHeight - contextMenu.clientHeight-5;
            //mouseY - contextMenu.clientHeight;
        }

        return { normalizedX, normalizedY };
      };


 export const getParentByTagName=(obj, tagName) =>{
     tagName = tagName.toLowerCase();
     while (obj!= null && obj.tagName!=null && obj.tagName.toLowerCase() !=
     tagName) {
     obj=obj.parentNode;
     }
     return obj;
 }


 export const setCookie=(name, value, days) =>{
     var expires = "";
     if (days) {
         var date = new Date();
         date.setTime(date.getTime() + (days*24*60*60*1000));
         expires = "; expires=" + date.toUTCString();
     }
     document.cookie = name + "=" + (value || "")  + expires + "; path=/";
 }

 export const getCookie=(name) =>{
     var nameEQ = name + "=";
     var ca = document.cookie.split(';');
     for(var i=0;i < ca.length;i++) {
         var c = ca[i];
         while (c.charAt(0)==' ') c = c.substring(1,c.length);
         if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
     }
     return null;
 }

 export const eraseCookie=(name) =>{
     document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
 }
