import React from 'react'
import ReactDOM from 'react-dom'
//import AppNavbar from './../layouts/AppNavbar';
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';

class UsersFtp extends React.Component {

 constructor(props) {
       super(props);
       this.state = {rows : [],
                     text: '',
                     title: ''
                    }
 }

 componentDidMount(){
     try{
       let data = sessionStorage.getItem('list-of-users');
       if(data!=null && data !="")
       this.setState({rows: JSON.parse(data) });
     }catch(err){}
  }

// getListOfUser
 getData =(e)=>{
     e.preventDefault();
     showLoad();
     axios.get(window.API_URL+'get-list-of-users',  headers() )
                .then(res => {
                     this.setState({rows: res.data });
                     sessionStorage.setItem('list-of-users', JSON.stringify(res.data));
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
                  sessionStorage.setItem('list-of-users', JSON.stringify(res.data));
                  hideLoad();
               }). catch(error => {
                  handleError(error);
                  hideLoad();
               });
    }

// make user sudoer
 addRemoveFromSudo =(e, uname="", type="" )=>{
     e.preventDefault();

    if(!window.confirm("Are you sure you want to "+type+" the user: "+uname+(type=="add"?" to":" from")+" sudo  ?"))
    return null;

     showLoad();
     axios.get(window.API_URL+'add-remove-user-from-sudo?name='+uname+'&type='+type,  headers() )
                .then(res => {
                     hideLoad();
                     showAlert(res.data);
                 }).catch(error => {
                     handleError(error);
                     hideLoad();
                 });
  }

  // add new user
    addUSer =(e)=>{
         e.preventDefault();
         const form = e.currentTarget
         const body = serialize(form, {hash: true, empty: false})

         showLoad();
         axios.put(window.API_URL+'add-new-user', body, headers()).
               then(res => {
                   this.setState({rows: res.data });
                   sessionStorage.setItem('list-of-users', JSON.stringify(res.data));
                   hideLoad();
                   alert("User added!");
               }). catch(error => {
                   handleError(error);
                   hideLoad();
               });
    }

//remove user
 removeUser = (e, uname="")=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove user: "+uname+" ?"))
   return null;

    showLoad();
    axios.delete(window.API_URL+'remove-user?name='+uname,  headers() )
         .then(res => {
              this.setState({rows: res.data });
              sessionStorage.setItem('list-of-users', JSON.stringify(res.data));
              hideLoad();
              alert("User Removed");
          }).catch(error => {
              handleError(error);
              hideLoad();
          });
 }

  render() {
    return (
       <div>
        <form onSubmit={this.addUSer} action="#" method="POST">
           <div class="row align-items-center shadow-sm bg-body rounded paddingBottomTopForm">
             <div class="col-md-3">
                 <input type="text" name="name" class="form-control" required={true} placeholder="User Name"/>
             </div>
             <div class="col-md-3">
                <input type="text" name="password" class="form-control" required={true} placeholder="User Password"/>
              </div>
              <div class="col-md-3">
                 <input type="text" name="path" class="form-control"  placeholder="Path, live empty by default"/>
              </div>
             <div class="col-md-3">
               <button class="btn btn-primary btn_small" type ="submit" >Add New User</button>
             </div>

           </div>
         </form>
           <div class="clear"></div>
         <div class="row">
          <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col">User Name</th>
                <th scope="col">Path</th>
                <th scope="col"></th>
                <th scope="col"></th>
                <th scope="col">Password</th>
                <th scope="col">Delete</th>
             </tr>
            </thead>
            <tbody>
            {this.state.rows.map(row=>
              <tr>
                <td><i class="bi bi-person"></i> {row.name}</td>
                <td>
                    {row.path} &nbsp;
                    {row.name !="root"?
                    <a href="#" onClick={e=>this.changePath(e, row.name, row.path)} class="editPencil">
                       <i class="bi bi-pencil-square"></i>
                    </a>:<></>}
                </td>
                <td>
                {row.name !="root"?
                   <a href="#" onClick={e=>this.addRemoveFromSudo(e, row.name, "add")}>Add to sudo</a>:<></>}
                </td>
                <td>
                {row.name !="root"?
                   <a href="#" onClick={e=>this.addRemoveFromSudo(e, row.name, "remove")}>Remove from sudo</a>:<></>}
                </td>
                <td>
                  ******* &nbsp;
                   <a href="#" onClick={e=>this.changePassword(e, row.name)} class="editPencil">
                      <i class="bi bi-pencil-square"></i>
                   </a>

                </td>
                 <td>
                 {row.name !="root"?
                   <a href="#" onClick={e=>this.removeUser(e, row.name)}> <i class="bi bi-x-circle-fill"></i> </a>
                 :<></>}
                 </td>
              </tr>
             )}
            </tbody>
          </table>
          <div class="col-md-12">
              <p class="text_align_center">
                 <a href="#" class="btn btn btn-success btn_small" onClick={this.getData}>
                 <i class="bi bi-arrow-clockwise"></i> Get Users List
                </a>
              </p>
         </div>
        </div>
       </div>
    );
  }
}

export default UsersFtp;