import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert, setInput } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';


class Settings extends React.Component {

 constructor(props) {
       super(props);
       this.state = {rows : [], activeAcc:"" }
 }

 componentDidMount(){
      this.getData();
  }

// getList Of User
 getData =()=>{
    showLoad();
     axios.get(window.API_URL+'get-list-of-accounts',  headers() )
                .then(res => {
                     this.setState({rows: res.data });
                     hideLoad();
                  }).catch(error => {
                     //handleError(error);
                     hideLoad();
                 });
  }


  // add new one
  addNewOne =(e)=>{
     e.preventDefault();
     const form = e.currentTarget
     const body = serialize(form, {hash: true, empty: true})
     body["id"]=this.state.activeAcc;
     //alert(JSON.stringify(body));

     showLoad();
     axios.put(window.API_URL+'add-update-settings-account',body, headers()).
           then(res => {
               hideLoad();
               this.setState({rows: res.data });

               if(this.state.activeAcc=="")
               document.getElementById("addNewAccount").reset();
             var updateAccounts = document.getElementById("updateAcc");
             if(updateAccounts)
               updateAccounts.click();

            }). catch(error => {
               handleError(error);
               hideLoad();

           });
   }

//remove user
 removeOne = (e)=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove this account?"))
   return null;

    showLoad();
    axios.delete(window.API_URL+'remove-setting-account?id='+this.state.activeAcc,  headers() )
         .then(res => {
              hideLoad();
              this.setState({rows: res.data, activeAcc:"" });
              alert("Removed!");
              var updateAccounts = document.getElementById("updateAcc");
               if(updateAccounts)
                 updateAccounts.click();
          }).catch(error => {
              handleError(error);
              hideLoad();
          });
 }


newAccount=(e)=>{
 e.preventDefault();
  document.getElementById("addNewAccount").reset();
  this.setState({activeAcc: "" });
}

editAcc=(e, i, id="")=>{
   e.preventDefault();
   const acc= this.state.rows[i];
   setInput(acc);
   this.setState({activeAcc:id });
}

checkBoxFast=(e)=>{
    var val = e.target.value;
     if(val =="root")
      document.getElementById("makeFastGo").checked = true;
}


  render() {
    return (
       <>
         <div class="height15px"></div>
         <div class="row">
            <div class="col-md-3">
              <a href="#" class="btn btn btn-primary btn_small" onClick={this.newAccount} style={{width:"100%",textAlign:"left"}}>
                <i class="bi bi-person-plus-fill"></i> Add new Account
             </a>
             <div class="height5px"></div>
             {this.state.rows.map((row,i)=>
               <>
                 <a href="#" class={"btn btn btn_small "+(this.state.activeAcc==row.id ? "btn-secondary": "btn-success")}
                              onClick={e=>this.editAcc(e, i, row.id)} style={{width:"100%",textAlign:"left"}}>
                   <i class="bi bi-person"></i> {row.sshHost}@{row.sshLog}
                 </a>
                 <div class="height5px"></div>
               </>
             )}

           </div>
           <div class="col-md-5 addAccountData">

                <form onSubmit={this.addNewOne} action="#" method="POST" id="addNewAccount">
                   <p>
                     <small>Platform</small>
                     <select name="platform" class="form-control">
                       <option value="Ubuntu">Ubuntu</option>
                     </select>
                   </p>
                   <p>
                     <small>Host</small>
                     <input type="text" name="sshHost" class="form-control" required={true} placeholder="192.168.1.1"/>
                   </p>
                   <p>
                    <small>SSH user</small>
                     <input type="text" name="sshLog" class="form-control" onBlur={this.checkBoxFast} required={true} placeholder="root"/>
                   </p>
                     <p>
                       <small>SSH password</small>
                        <input type="password" name="sshPass" class="form-control"   placeholder="****"/>
                     </p>
                     <p>
                        <small>SSH <b>.pem</b> (open your **.pem file in text and copy and paste <b>all content</b> here)</small> <br/>
                          <small><b>if you see **** than the data was saved and it is ok no need to enter again</b></small>
                        <textarea name="sshPem" class="form-control" style={{minHeight:"200px" }}
                                                 placeholder="-----BEGIN RSA PRIVATE KEY-----&#10;****&#10;**** &#10;....&#10;-----END RSA PRIVATE KEY-----" />
                      </p>
                     <p>
                      <small>Mysql user name</small>
                      <input type="text" name="mysqlLog" class="form-control" placeholder="root"/>
                    </p>
                    <p>
                      <small>Mysql password</small>
                      <input type="password" name="mysqlPass" class="form-control" placeholder="****"/>
                    </p>
                     <div class="height5px"></div>
                    <p>
                     <label> <b>
                      <input type="checkbox" name="fast" id="makeFastGo" value="yes"/>&nbsp;
                       Make fast save file and upload</b>
                     </label>
                       <small>(it will work only if SSH user have root access)</small>

                    </p>
                    <div class="height10px"></div>
                   <p>
                     <button class="btn btn-primary btn_small" type ="submit" >{this.state.activeAcc !="" ? "Update":"Add"} Account</button>
                      {this.state.activeAcc=="" ? <></>:
                          <a href="#" onClick={this.removeOne} style={{float:"right", color:"red"}} title="Delete account">
                            <i class="bi bi-x"></i>  Remove
                          </a>
                      }
                   </p>
                </form>

           </div>
         </div>

      </>
    );
  }
}

export default Settings;