import React from 'react'
import ReactDOM from 'react-dom'
//import AppNavbar from './../layouts/AppNavbar';
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert, generatePassword } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';

class DatabaseUsers extends React.Component {
  privileges = ["ALL PRIVILEGES","CREATE","DROP","DELETE","INSERT","SELECT","UPDATE"];
  sessionStorageName = 'list-of-database-mysql-user';
  constructor(props) {
       super(props);
       this.state = {rows : [], dbList:[] };
 }

 componentDidMount(){
     try{
       let data = sessionStorage.getItem(this.sessionStorageName);
       if(data!=null && data !="")
       this.setState({rows: JSON.parse(data) });
     }catch(err){}

     axios.get(window.API_URL+'get-list-of-mysql-database',  headers() )
          .then(res => {
              this.setState({dbList: res.data });
          }).catch(error => {  });
  }

// getListOfUser or database
 getData =(e, type="")=>{
     e.preventDefault();
     showLoad();
     axios.get(window.API_URL+'get-list-of-database-users?dataType='+type,  headers() )
                .then(res => {
                     this.setState({rows: res.data });
                     sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
                     hideLoad();
                  }).catch(error => {
                     handleError(error);
                     hideLoad();
                 });
  }

   // add new user and assign to database
    addNewData =(e)=>{
         e.preventDefault();
         const form = e.currentTarget
         const body = serialize(form, {hash: true, empty: false})
         showLoad();
         axios.put(window.API_URL+'add-new-database-user', body, headers()).
               then(res => {
                  this.setState({rows: res.data });
                  sessionStorage.setItem(this.sessionStorageNameDb, JSON.stringify(res.data));
                  hideLoad();
                }). catch(error => {
                  handleError(error);
                  hideLoad();
               });
    }

// change password
  changePassword =(e, name="", host="" )=>{
       e.preventDefault();
       const appName = name;

        let password = window.prompt("Enter the new password bellow", generatePassword(16));
        if (password == null || password =="") {
          if(password =="")
             alert( "The field password should not be empty");

           return null;
       }

       const body = {name: name, password: password, host:host };
       showLoad();

       axios.put(window.API_URL+'user-mysqldb-change-password', body, headers()).
             then(res => {
                 hideLoad();
                 alert(res.data);
             }). catch(error => {
                handleError(error);
                hideLoad();
             });
  }


//remove user
 removeData = (e, uname="", hostdb="")=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove user: "+uname+" ?"))
   return null;

    showLoad();
    axios.delete(window.API_URL+'remove-database-user?name='+uname+'&hostdb='+hostdb,  headers() )
         .then(res => {
              this.setState({rows: res.data });
              sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
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
        <form onSubmit={this.addNewData} action="#" method="POST">
            <div class="row align-items-center shadow-sm bg-body rounded paddingBottomTopForm">
              <div class="col-md-2">
                <small>Database</small><br/>
                <select class="form-control" name="name" required={true}>
                  <option value="">Select Database</option>
                      {this.state.dbList.map(row=>
                        <option value={row.name}>{row.name}</option>
                      )}
                </select>
              </div>
              <div class="col-md-2">
                  <small>User Name</small> <br/>
                 <input type="text" name="user" class="form-control" required={true} placeholder="User name"/>
              </div>
              <div class="col-md-3">
                 <small>User password</small> <br/>
                 <input type="text" name="password" class="form-control" required={true}  title="Password"
                      defaultValue={generatePassword(15)} placeholder="User Password"/>
               </div>
               <div class="col-md-2">
                  <small>Host</small> <br/>
                  <input type="text" name="host" class="form-control" required={true} defaultValue="localhost" placeholder="Host"/>
               </div>
              <div class="col-md-3">
                <br/>
                <button class="btn btn-primary btn_small" type ="submit" >Add New Database</button>
              </div>
              <div class="col-md-12">
                {this.privileges.map(row=>
                  <><label> <input type="checkbox" name="privileges[]" value={row} /> {row} </label>&nbsp;&nbsp;</>
                )}
              </div>

            </div>
          </form>
         <div class="height20px"></div>

            <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
              <div class="col-md-6">

                <a href="#" class="btn btn btn-success btn_small" onClick={e=>this.getData(e,"users")}>
                 <i class="bi bi-arrow-clockwise"></i> Show User list
                </a>
              </div>
              <div class="col-md-6 text_align_right">
              </div>
            </div>


           <div class="clear"></div>
         <div class="row">
          <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col">Name</th>
                <th scope="col">Host</th>
                <th scope="col">Password</th>
                <th scope="col">Delete</th>

             </tr>
            </thead>
            <tbody>
            {this.state.rows.map(row=>
              <tr>
                <td><i class="bi bi-person"></i> {row.name}</td>
                <td> {row.host}</td>
                <td>
                    ***** &nbsp;
                    <a href="#" onClick={e=>this.changePassword(e, row.name, row.host)} class="editPencil">
                       <i class="bi bi-pencil-square"></i>
                    </a>
                </td>
                <td>
                    <a href="#" onClick={e=>this.removeData(e, row.name, row.host)}>
                        <i class="bi bi-trash3"></i> Delete
                    </a>
                </td>

              </tr>
             )}
            </tbody>
          </table>
             <div class="col-md-12">
                <p class="text_align_center">
                   <a href="#" class="btn btn btn-success btn_small" onClick={e=>this.getData(e,"users")}>
                      <i class="bi bi-arrow-clockwise"></i> Get Data
                   </a>
                </p>
            </div>
        </div>
       </div>
    );
  }
}

export default DatabaseUsers;