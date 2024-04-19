import React from 'react'
import ReactDOM from 'react-dom'
//import AppNavbar from './../layouts/AppNavbar';
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert, generatePassword, getCookie } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';

class Database extends React.Component {
 sessionStorageNameDb = 'list-of-database';
 privileges = ["ALL PRIVILEGES","CREATE","DROP","DELETE","INSERT","SELECT","UPDATE"];
 constructor(props) {
       super(props);
       this.state = {rows : [],
                     text: '',
                     title: ''
                    }
 }

 componentDidMount(){
     try{
       let data = sessionStorage.getItem(this.sessionStorageNameDb);
       if(data!=null && data !="")
       this.setState({rows: JSON.parse(data) });
     }catch(err){}
  }

// getListOfUser or database
 getData =(e, type="")=>{
     e.preventDefault();
     showLoad();
     axios.get(window.API_URL+'get-list-of-database-users?dataType='+type,  headers() )
                .then(res => {
                     this.setState({rows: res.data });
                     sessionStorage.setItem(this.sessionStorageNameDb, JSON.stringify(res.data));
                     hideLoad();
                  }).catch(error => {
                     handleError(error);
                     hideLoad();
                 });
  }

 importDb =(e, name="" )=>{
     e.preventDefault();
     let files = e.target.files;
     if(files.length ==0)
               return null;

     const formData = new FormData();

     formData.append("file", files[0], Date.now()+"."+files[0].name.split(".").pop());
     formData.append("dbname", name);

     showLoad();
     axios.put(window.API_URL+'import-database', formData, headers()).
           then(res => {
             hideLoad();
             alert(res.data);
           }). catch(error => {
             handleError(error);
             hideLoad();
           });
  }



  // add new database
    addNewData =(e)=>{
         e.preventDefault();
         const form = e.currentTarget
         const body = serialize(form, {hash: true, empty: false})
        // alert(JSON.stringify(body));
         showLoad();
         axios.put(window.API_URL+'add-new-database', body, headers()).
               then(res => {
                  this.setState({rows: res.data });
                  sessionStorage.setItem(this.sessionStorageNameDb, JSON.stringify(res.data));
                  hideLoad();
                }). catch(error => {
                  handleError(error);
                  hideLoad();
               });
    }

//remove user
 removeData = (e, name="")=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove the: "+name+" ?"))
   return null;

    showLoad();
    axios.delete(window.API_URL+'remove-database?name='+name,  headers() )
         .then(res => {
              this.setState({rows: res.data });
              sessionStorage.setItem(this.sessionStorageNameDb, JSON.stringify(res.data));
              hideLoad();
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
                <small>Data Base Name</small> <br/>
                <input type="text" name="name" class="form-control" required={true} placeholder="Data Base Name"/>
             </div>
             <div class="col-md-2">
                <small>User name</small><br/>
                <input type="text" name="user" class="form-control" required={true} placeholder="User name"/>
             </div>
             <div class="col-md-3">
               <small>User password</small> <br/>
                <input type="text" name="password" class="form-control" required={true} title="Password"
                        defaultValue={generatePassword(16)} placeholder="User Password"/>
              </div>
              <div class="col-md-2">
                 <small>Host</small> <br/>
                 <input type="text" name="host" class="form-control" required={true} defaultValue="localhost" placeholder="Host"/>
              </div>
             <div class="col-md-3"><br/>
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
                <a href="#" class="btn btn btn-success btn_small" onClick={e=>this.getData(e,"database")}>
                 <i class="bi bi-arrow-clockwise"></i> Show Database list
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
                <th scope="col">User</th>
                <th scope="col">Export</th>
                <th scope="col">Import</th>
                <th scope="col">Action</th>
              </tr>
            </thead>
            <tbody>
            {this.state.rows.map(row=>
              <tr>
                <td>
                 <a href={window.BASE_URL+"#/database-mysql/"+row.name} >
                   <i class="bi bi-hdd-stack"></i> {row.name}
                </a>
                </td>
                 <td>
                    {row.host} &nbsp;
                    <a href={window.BASE_URL+"#/database-mysql-users"} class="editPencil">
                       <i class="bi bi-pencil-square"></i>
                    </a>
                </td>
                <td>
                    {row.user} &nbsp;
                    <a href={window.BASE_URL+"#/database-mysql-users"} class="editPencil">
                       <i class="bi bi-pencil-square"></i>
                    </a>
                </td>
                <td>
                   <a href={window.API_URL+"export-database?name="+row.name+"&id="+localStorage.getItem("id")+
                                                                            "&tok="+getCookie("tokenauth")} >
                      <i class="bi bi-box-arrow-right"></i> Export
                   </a>
                </td>
                <td>
                    <input type="file" name="import" style={{display:"none"}}
                        id={"import_"+row.name} onChange={e=>this.importDb(e, row.name)} />
                    <label for={"import_"+row.name} class="labelBtn"> <i class="bi bi-box-arrow-in-left"></i> Import </label>

                </td>
                <td>
                   <a href="#" onClick={e=>this.removeData(e, row.name )}>
                      <i class="bi bi-trash3"></i> Delete
                   </a>
                </td>
              </tr>
             )}
            </tbody>
          </table>
             <div class="col-md-12">
                <p class="text_align_center">
                   <a href="#" class="btn btn btn-success btn_small" onClick={e=>this.getData(e,"database")}>
                      <i class="bi bi-arrow-clockwise"></i> Get Data
                   </a>
                </p>
            </div>
        </div>
       </div>
    );
  }
}

export default Database;