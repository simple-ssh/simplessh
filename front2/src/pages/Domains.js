import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';
import PopupCodeEditor from './PopupCodeEditor';

class Domains extends React.Component {
 sessionStorageName = 'list-of-domains';
 constructor(props) {
       super(props);
       this.state = {rows : [],
                     text: '',
                     filename: '',
                     title: '',
                     showEdit:false,
                     path:'/etc/nginx/conf.d/',
                     editsRows:[],
                    }
 }

 componentDidMount(){
     try{
       let data = sessionStorage.getItem(this.sessionStorageName);
       if(data!=null && data !="")
       this.setState({rows: JSON.parse(data) });
     }catch(err){}
  }

// getListOfUser
 getData =(e)=>{
     e.preventDefault();
     showLoad();
     axios.get(window.API_URL+'get-list-of-domains',  headers() )
                .then(res => {
                     this.setState({rows: res.data });
                     sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
                     hideLoad();
                  }).catch(error => {
                     handleError(error);
                     hideLoad();
                 });
  }


// change password
  changePassword =(e, name="" )=>{
       e.preventDefault();
       const appName = name;

        let password = window.prompt("Enter the new password bellow", "");
        if (password == null || password =="") {
          if(password =="")
             alert( "The field password should not be empty");

           return null;
       }

       const body = {name: name, password: password };
       showLoad();

       axios.put(window.API_URL+'user-change-password', body, headers()).
             then(res => {
                 hideLoad();
                 alert(res.data);
             }). catch(error => {
                handleError(error);
                hideLoad();
             });
  }


  // change password
    changePath =(e, name="", oldPath="" )=>{
         e.preventDefault();
         const appName = name;

          let path = window.prompt("Enter the new path", oldPath);
          if (path == null || path =="") {
            if(path =="")
               alert( "The field path should not be empty");
             return null;
         }

           const body = {name: name, path: path };
           showLoad();
           axios.put(window.API_URL+'user-change-path', body, headers()).
               then(res => {
                  this.setState({rows: res.data });
                  sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
                  hideLoad();
               }). catch(error => {
                  handleError(error);
                  hideLoad();
               });
    }

// make user sudoer
 onOff =(e, name="", type="" )=>{
     e.preventDefault();

    if(!window.confirm("Are you sure you want to "+(type=="on"?"activate":" suspend")+
                       " the domain: "+name+" ?"))
    return null;

     showLoad();
     axios.get(window.API_URL+'suspend-activate-domain?name='+name+'&type='+type,  headers() )
                .then(res => {
                     hideLoad();
                     this.setState({rows: res.data });
                     sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
                 }).catch(error => {
                     handleError(error);
                     hideLoad();
                 });
  }

  // add new one
    addNewOne =(e)=>{
         e.preventDefault();
         const form = e.currentTarget
         const body = serialize(form, {hash: true, empty: true})
         if(body["ns1"] !="" && body["ip"]==""){
           alert("The field IP address is required");
           document.getElementById("ipAddress").focus();
           return null;
         }


         showLoad();
         axios.put(window.API_URL+'add-new-domain', body, headers()).
               then(res => {
                   this.setState({rows: res.data });
                   sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
                   hideLoad();
                   alert("Domain added!");
               }). catch(error => {
                   handleError(error);
                   hideLoad();
               });
    }

//remove user
 removeOne = (e,  name="")=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove the "+name+" ?"))
   return null;

    showLoad();
    axios.delete(window.API_URL+'remove-domain?name='+name,  headers() )
         .then(res => {
              this.setState({rows: res.data });
              sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
              hideLoad();
              alert("Domain Removed");
          }).catch(error => {
              handleError(error);
              hideLoad();
          });
 }

 editFile=(e, dName="", path="", fileName="")=>{
      e.preventDefault();
      showLoad();
    axios.get(window.API_URL+'get-file-content?pathFile='+path+fileName,  headers() )
         .then(res => {
              hideLoad();
               let obj ={path:path,
                         content: res.data,
                         fileName: fileName,
                         owner: "root" };
               localStorage.setItem("edit_files", JSON.stringify([obj]));
               this.setState({ editsRows:[obj]});
               document.getElementById("showPopupEditor").click();
          }).catch(error => {
              handleError(error);
              hideLoad();
          });

 }

 installSSL =(e, name="", oldPath="" )=>{
    e.preventDefault();
    const appName = name;

     let email = window.prompt("Enter email, if the ssl was installed use renew", oldPath);
     if (email == null || email =="") {
       if(email =="")
          alert( "The email should not be empty");
        return null;
    }

      const body = {name: name, email: email };
      showLoad();
      axios.put(window.API_URL+'install-ssl', body, headers()).
          then(res => {
             this.setState({rows: res.data.rows });
             sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data.rows));
             showAlert(res.data.response);
             hideLoad();
          }). catch(error => {
             handleError(error);
             hideLoad();
          });
 }

 renewSSL=(e)=>{
     e.preventDefault();
      showLoad();
      axios.get(window.API_URL+'renew-ssl',  headers() )
                 .then(res => {
                      hideLoad();
                      this.setState({rows: res.data.rows });
                      sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data.rows));
                      showAlert(res.data.response);
                  }).catch(error => {
                      handleError(error);
                      hideLoad();
                  });

 }

    actionService =(e, name="", action="")=>{
      e.preventDefault();
      showLoad();
      axios.get(window.API_URL+'action-service?name='+name+"&actionService="+action,  headers() )
                 .then(res => {
                      hideLoad();
                      alert(res.data=="" || res.data==null ? "Success":  res.data);
                   }).catch(error => {
                      handleError(error);
                      hideLoad();
                  });
     }

  render() {
    return (
       <>
         <div class="height20px"></div>
         <form onSubmit={this.addNewOne} action="#" method="POST">
            <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
                 <div class="col-md-2">
                     <input type="text" name="name" class="form-control" required={true} placeholder="Domain Name"/>
                 </div>
                 <div class="col-md-2">
                    <input type="text" name="proxy" class="form-control" placeholder="Proxy"/>
                  </div>
                  <div class="col-md-3">
                     <input type="text" name="path" class="form-control"  required={true} defaultValue="/var/www/" placeholder="Path"/>
                  </div>
                  <div class="col-md-2">
                     <select name="typeDomain" class="form-control">
                       <option value="php">Simple php/html domain</option>
                       <option value="java">Java domain</option>
                     </select>
                  </div>
                 <div class="col-md-2">
                   <button class="btn btn-primary btn_small" type ="submit" >Add New Domain</button>
                 </div>
                 <div class="height5px"></div>
                 <div class="col-md-2">
                   <input type="text" name="ns1" class="form-control" placeholder="Name server 1"/>
                 </div>
                 <div class="col-md-2">
                     <input type="text" name="ns2" class="form-control" placeholder="Name server 2"/>
                  </div>
                  <div class="col-md-2">
                     <input type="text" name="ns3" class="form-control" placeholder="Name server 3"/>
                  </div>
                   <div class="col-md-2">
                      <input type="text" name="ns4" class="form-control" placeholder="Name server 4"/>
                   </div>

           </div>
         </form>
           <div class="height20px"></div>

           <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
             <div class="col-sm-4">
               <a href="#" class="btn btn btn-success btn_small" onClick={this.getData}>
                <i class="bi bi-arrow-clockwise"></i> Show domains list
               </a>
             </div>
             <div class="col-sm-8 text_align_right">
               <a href="#" class="btn btn-secondary btn_small" onClick={e=>this.actionService(e,"nginx","status")}>Nginx status</a> &nbsp;&nbsp;
               <a href="#" class="btn btn-info btn_small" onClick={e=>this.actionService(e,"nginx","restart")}>Restart Nginx</a>
             </div>
           </div>

           <div class="clear"></div>
       <div class="row">
         <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col">Domain Name</th>
                <th scope="col" class="text_align_center">Edit</th>
                <th scope="col" class="text_align_center">Suspend/Activate</th>
                <th scope="col" class="text_align_center">DNS</th>
                <th scope="col" class="text_align_center">Let’s Encrypt SSL </th>
                <th scope="col" class="text_align_center">Remove</th>
             </tr>
            </thead>
            <tbody>
            {this.state.rows.map(row=>
             <tr>
                 <td>
                    <a href={"http://"+row.name} target="_blank" >
                     <i class="bi bi-globe"></i> {row.name}
                    </a>
                 </td>
                 <td class="text_align_center">
                     <a href="#" onClick={e=>this.editFile(e, row.name, "/etc/nginx/conf.d/", row.active)} >
                        <i class="bi bi-pencil-square"></i>
                     </a>
                 </td>
                 <td class="text_align_center">
                   {row.active.includes(".conf") ?
                      <a href="#" onClick={e=>this.onOff(e, row.name, "off")}>
                         <span class="bulbActive"><i class="bi bi-toggle-on bigIconSize"></i></span>
                      </a> :
                      <a href="#" onClick={e=>this.onOff(e, row.name, "on" )}>
                         <i class="bi bi-toggle-off bigIconSize"></i>
                      </a>}
                 </td>
                 <td class="text_align_center">
                    <a href={window.BASE_URL+"#/domains/dns/"+row.name}  >
                        <i class="bi bi-pencil-square"></i> DNS
                    </a>
                 </td>
                 <td class="text_align_center">
                   {row.ssl=="yes" ?
                        <span class="bulbActive"><i class="bi bi-toggle-on bigIconSize"></i></span>
                        :
                       <>
                          <a href="#" onClick={e=>this.installSSL(e, row.name)} title="Activate">
                             <i class="bi bi-toggle-off bigIconSize"></i>
                          </a>
                          &nbsp; <a href="#" onClick={e=>this.renewSSL(e)} title="Renew">
                                    <i class="bi bi-arrow-clockwise bigIconSize"></i>
                                 </a>
                       </>
                      }

                 </td>
                  <td class="text_align_center">
                    <a href="#" onClick={e=>this.removeOne(e, row.active)}>
                       <i class="bi bi-x-circle-fill"></i>
                    </a>
                  </td>
               </tr>
             )}
            </tbody>
          </table>
            <div class="col-md-12">
                <p class="text_align_center">
                   <a href="#" class="btn btn btn-success btn_small" onClick={this.getData}>
                      <i class="bi bi-arrow-clockwise"></i> Get Data
                   </a>
                </p>
            </div>
         </div>

          <PopupCodeEditor editsRows={this.state.editsRows} />
       </>
    );
  }
}

export default Domains;