import React from 'react'
import ReactDOM from 'react-dom'
import Editor from "@monaco-editor/react";
import {getLanguage, headers, hideLoad, handleError,  showLoad, showAlert, successIcon} from './../Helpers.js';
import axios from 'axios';

class PopupCodeEditor extends React.Component {
 sessionStorageName = "edit_files";
 sessionStorageMinimise = "minimiseEditor";
 constructor(props) {
       super(props);
       this.state = {
                     rows:          [],
                     text:          "",
                     filename:      "",
                     language:      "",
                     path:          "",
                     showAjax:      "none",
                     activeTab:     0,
                     showAjaxAll:   "none",
                     showAjaxReload:   "none",
                     browserMobile: false,
                    }
 }

 updateRows=()=>{
        var objRows=[];
        try{
          let data = localStorage.getItem(this.sessionStorageName);
          if(data!=null && data !=""){
           objRows =JSON.parse(data, (key, value) => { return typeof value === 'number'? value.toString() : value; });
           this.setState({rows:objRows, activeTab:(objRows.length-1)});
          }
        }catch(err){  }



             const ua = navigator.userAgent||navigator.vendor||window.opera;
             //here are 2 if chose witch one work for you
             //if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(ua)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(ua.substr(0,4)))
             if (/Mobile|Android|iP(hone|od)|IEMobile|BlackBerry|Kindle|Silk-Accelerated|(hpw|web)OS|Opera M(obi|ini)/.test(ua))
             {
               this.setState({browserMobile:true});
             }


 }

 componentDidMount(){
   this.updateRows();

        // Check if the event listener has been initialized
        if (!document.keydownEventListenerInitialized) {
          // Add event listener
          document.addEventListener('keydown', e => {

               if (e.ctrlKey && e.key === 's') {
                 // Prevent the Save dialog to open
                 e.preventDefault();
                 // Place your code here
                 this.updateFile(e, this.state.activeTab);
               }
             });

          // Set flag to indicate that the event listener has been initialized
          document.keydownEventListenerInitialized = true;
        }
       /*
          this.setState({text:     this.props.text,
                    filename: this.props.filename,
                    language: getLanguage(this.props.filename),
                    path:     this.props.path,
                });*/
 }

  componentWillReceiveProps(props){
    this.updateRows();
  }



 showEditor=(e)=>{
    e.preventDefault();
    try{
      document.getElementById("mini-modal-editor").style.display = "block";
      document.getElementsByTagName('body')[0].style.overflowY = "hidden"  ;
      document.getElementById("editorBottomLink").style.display = "none";
    }catch(err){}
    localStorage.setItem(this.sessionStorageMinimise,"");
    this.updateRows();
 }


 closeEditor=(e)=>{
     e.preventDefault();
      try{
        document.getElementsByTagName('body')[0].style.overflowY = "visible"  ;
        document.getElementById("mini-modal-editor").style.display = "none";
      }catch(err){}
      this.setState({rows:[]});
      localStorage.setItem(this.sessionStorageName,"");
      localStorage.setItem(this.sessionStorageMinimise,"");
  }



   minimisePopup=(e)=>{
        e.preventDefault();
        try{
          document.getElementsByTagName('body')[0].style.overflowY = "visible"  ;
          document.getElementById("mini-modal-editor").style.display = "none";
          document.getElementById("editorBottomLink").style.display = "block";
        }catch(err){}
        localStorage.setItem(this.sessionStorageMinimise,"yes");
    }

  onChange = (newValue, j) => {
      try{
           var rows = this.state.rows;
               rows[j]["content"] = newValue;
           this.setState({rows: rows});
           localStorage.setItem(this.sessionStorageName, JSON.stringify(rows));
        }catch(err){}
  }



 closeTab=(e, j)=>{
   e.preventDefault();
    try{
         var rows = this.state.rows;
             rows.splice(j, 1);
         this.setState({rows: rows, activeTab: (rows.length-1)});
         localStorage.setItem(this.sessionStorageName, JSON.stringify(rows));
    }catch(err){}
  }

  updateFile =(e, j, type="single")=>{
       e.preventDefault();
       const body = type=="all" ? this.state.rows : [this.state.rows[j]];
       if(type=="all") {
           this.setState({showAjaxAll: 'inline'});
        }else{
           this.setState({showAjax: 'inline'});
          }
         //alert(JSON.stringify(body)); return null;
       axios.put(window.API_URL+'save-file-content', body, headers()).
           then(res => {
               this.setState({showAjax: 'none', showAjaxAll:"none"});
               if(res.data=="ok"){
                  successIcon();
               }else{
                  alert(res.data);
               }

            }). catch(error => {
              handleError(error);
              this.setState({showAjax: 'none', showAjaxAll:"none"});
           });
    }

    activateTab =(e, nr=0)=>{
        e.preventDefault();
        this.setState({activeTab: nr});
    }

    reloadFile =(e, j )=>{
        e.preventDefault();
        const body = this.state.rows[j];

       if(!window.confirm("Are you sure you want to reload "+body.fullPathWithName + " all the changes will be lost"))
                return null;

         this.setState({showAjaxReload: 'inline'});
         axios.get(window.API_URL+'get-file-content?pathFile='+body.fullPathWithName,  headers() )
               .then(res => {
                       this.state.rows[j]['content'] = res.data;
                       this.setState({rows:this.state.rows, showAjaxReload: 'none'});
                       localStorage.setItem("edit_files", JSON.stringify(this.state.rows));
                 }).catch(error => {
                    handleError(error);
                    this.setState({showAjax: 'none' });
                });
    }

     newFile =(e, j)=>{
            e.preventDefault();
            let body = this.state.rows[j];

            let fileName = window.prompt("Enter the the name of the file "+body.path, "");
            if (fileName == null || fileName =="") {
              if(fileName =="")
                alert( "The field password should not be empty");

              return null;
            }

            let newFile={
                          path              : body.path,
                          content           : "",
                          fileName          : fileName,
                          owner             : body.owner,
                          fullPathWithName  : body.fullPathWithName +"/"+fileName,
                          permission        : body.permission,
                       };

            this.state.rows.push(newFile);
            this.setState({rows:this.state.rows, activeTab: parseInt(this.state.activeTab)+1 });
            localStorage.setItem("edit_files", JSON.stringify(this.state.rows));

            try{
                  let data = sessionStorage.getItem("list-of-files");
                  if(data!=null && data !=""){
                    var objRows =JSON.parse(data);
                    let newFileToList = {    data: body.permission+"; "+body.owner+";",
                                             group: body.owner,
                                             name: fileName,
                                             permission: body.permission,
                                             size: "11kb",
                                             type: "2"
                                          };
                    objRows.push(newFileToList);
                    sessionStorage.setItem("list-of-files", JSON.stringify(objRows));
                    document.getElementById("trigerUpdateList").click();
                 }
            }catch(err){}
     }

  render() {
     return (
        <>
          <div class="reveal-modal-bg modal-window" id="mini-modal-editor" >
              <div class="reveal-modal"  >
                <div class="modal-body modal-body-sub" style={{position: "relative"}}>
                  <button type="button" class="close_alert minimisePopup" onClick={this.minimisePopup} data-modal="#mini-modal" aria-hidden="true"> - </button>
                  <button type="button" class="close_alert close_modalinner" onClick={this.closeEditor} data-modal="#mini-modal" aria-hidden="true"> &times;</button>
                   <div>
                    <ul class="nav nav-tabs smallTabs">
                        {this.state.rows.map((row,i)=>
                          <li class="nav-item positionRelative navTabEditor">
                            <a class={"nav-link"+ (this.state.activeTab ==i ? " active":"")} aria-current="page" href="#" onClick={e=>this.activateTab(e,i)}>{row.fileName}</a>
                            <a href="#" onClick={e=>this.closeTab(e,i)} class="closeTabA"><i class="bi bi-x-square-fill"></i></a>
                          </li>
                        )}
                     </ul>
                     <div class="clear"></div>
                    {this.state.rows.map((row,j)=>
                      <div class="editTabs" style={{display:(this.state.activeTab ==j ? "block":"none")}}>
                         <div class="headerEditor">
                                <i style={{marginLeft:"17px"}}><small>{row.path}</small></i>
                                <a href="#" onClick={e=>this.reloadFile(e, j)} class="btn btn-link btn_small_small_small" style={{marginLeft:"0px"}}>
                                   <i class="bi bi-arrow-clockwise" style={{display:(this.state.showAjaxReload=="none" ? "inline":"none")}}></i>
                                   <img style={{width:"20px", display:this.state.showAjaxReload}} src={window.BASE_URL+"assets/img/ajax-loader.gif"}/>
                                </a>
                                <a href="#" onClick={e=>this.newFile(e, j)} class="btn btn-link btn_small_small_small" style={{marginLeft:"0px"}}>
                                   <i class="bi bi-file-earmark-plus"></i>
                               </a>
                              {this.state.rows.length>1?
                              <a href="#" onClick={e=>this.updateFile(e, j,"all")} class="btn btn-info btn_small_small_small"  style={{float:"right"}}>
                                 Update All <img style={{width:"20px", display:this.state.showAjaxAll}} src={window.BASE_URL+"assets/img/ajax-loader.gif"}/>
                              </a>: <></>}

                              <a href="#" onClick={e=>this.updateFile(e, j)} class="btn btn-info btn_small_small_small" style={{float:"right"}}>
                                 Save Changes <img style={{width:"20px", display:this.state.showAjax}} src={window.BASE_URL+"assets/img/ajax-loader.gif"}/>
                              </a>


                          </div>
                          <div class="clear"></div>

                         {this.state.browserMobile ?
                             <textarea wrap="off" style={{width:"100%", height:"88vh"}} value={row.content || ""} onChange={e=>this.onChange(e.target.value,j)}/>
                           :
                             <Editor
                                  height="88vh"
                                  language={getLanguage(row.fileName)}
                                  defaultValue=""
                                  value={row.content}
                                  onChange={e=>this.onChange(e,j)}
                              />
                            }

                       </div>
                       )}
                       <div class="height5px"></div>

                   </div>
                </div>
              </div>
           </div>
           <a href="#" id="showPopupEditor" onClick={this.showEditor}> </a>
           <a href="#" class="editorMinimiseLink" id="editorBottomLink" onClick={this.showEditor}
                   style={{display:localStorage.getItem(this.sessionStorageMinimise)=="yes"?  "block":"none"}}>
               <i class="bi bi-pencil-square"></i> Show Editor
           </a>

         </>
       );
     }
   }

export default PopupCodeEditor;