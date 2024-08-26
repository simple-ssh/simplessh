import React, { useState, useContext } from "react";

/**
 this part will show and check all bulk elements
**/
 export const SetBulk =(props)=>{
  return (<input type="checkbox" id="checkall" onClick={e=>bulkClick(e,props.cssClass)}/>);
}

 const bulkClick=(e, cssClass)=> {
    var bulkElements = document.getElementsByClassName(cssClass);
    for(var i=0;i<bulkElements.length;i++){
      bulkElements[i].checked= e.target.checked;
    }
}

export function Modal(props){

  return(
        <div class="modal fade in" tabindex="-1" role="dialog" id={props.idModal} >
          <div class="modal-dialog modalWindow modal-lg-editor" role="document">
            <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" onClick={()=>{document.getElementById(props.idModal).style.display="none";}} class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                    <h4 class="modal-title modal_load_title"> </h4>
                  </div>
                  <div class="modal_load_content" >
                    {props.htmlContent}
                  </div>
                 <div class="clear"></div>
              </div>
          </div>
        </div>
    );

}

