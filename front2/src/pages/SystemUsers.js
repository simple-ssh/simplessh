import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';

class SystemUsers extends React.Component {

 constructor(props) {
       super(props);
       this.state = {rows : {},
                     text: '',
                     title: ''
                    }
 }

 componentDidMount(){
     this.getData();
  }

// getListOfUser
 getData =()=>{
      showLoad();
     axios.get(window.API_URL+'get-system-users',  headers() )
                .then(res => {
                     this.setState({rows: res.data });
                     hideLoad();
                  }).catch(error => {
                     handleError(error);
                     hideLoad();
                 });
  }

// getListOfUser
 changeTokenAccess =(e)=>{
     e.preventDefault();
     showLoad();
     axios.get(window.API_URL+'change-token-access',  headers() )
                .then(res => {
                     hideLoad();
                     alert(res.data);
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

       const body = {username: name, password: password };
       showLoad();

       axios.put(window.API_URL+'add-change-system-users', body, headers()).
             then(res => {
                 hideLoad();
                 alert("Password Changed!");
             }). catch(error => {
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
         axios.put(window.API_URL+'add-change-system-users', body, headers()).
               then(res => {
                   this.setState({rows: res.data });
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
    axios.delete(window.API_URL+'remove-system-user?username='+uname,  headers() )
         .then(res => {
              this.setState({rows: res.data });
              hideLoad();
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
                 <input type="text" name="username" class="form-control" required={true} placeholder="User Name"/>
             </div>
             <div class="col-md-3">
                <input type="text" name="password" class="form-control" required={true} placeholder="User Password"/>
              </div>
             <div class="col-md-3">
               <button class="btn btn-primary btn_small" type ="submit" >Add New User</button>
             </div>
            <div class="col-md-3 text_align_right" >
               <a href="#" class="btn btn-primary btn_small" onClick={this.changeTokenAccess}>Change Token Access</a>
             </div>
           </div>
         </form>
           <div class="clear"></div>
         <div class="row">
          <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col">User Name</th>
                <th scope="col">Password</th>
                <th scope="col">Delete</th>
             </tr>
            </thead>
            <tbody>
            {Object.keys(this.state.rows).map(row=>
              <tr>
                <td><i class="bi bi-person"></i> {row}</td>
                <td>
                  ******* &nbsp;
                   <a href="#" onClick={e=>this.changePassword(e, row)} class="editPencil">
                      <i class="bi bi-pencil-square"></i>
                   </a>

                </td>
                 <td style={{width:"50px"}}>
                 {Object.keys(this.state.rows).length> 1?
                   <a href="#" onClick={e=>this.removeUser(e, row)}> <i class="bi bi-x-circle-fill"></i> </a>
                 :<></>}
                 </td>
              </tr>
             )}
            </tbody>
          </table>

        </div>
       </div>
    );
  }
}

export default SystemUsers;