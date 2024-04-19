import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';


class Firewall extends React.Component {
 sessionStorageName = 'list-of-firewall-rules';
 constructor(props) {
       super(props);
       this.state = {rows : [] }
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
     axios.get(window.API_URL+'get-list-of-firewall-rules',  headers() )
                .then(res => {
                     this.setState({rows: res.data });
                     sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
                     hideLoad();
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

     showLoad();
     axios.put(window.API_URL+'add-new-firewall-rule?name='+body.name+"&dowhat="+body.dowhat,{}, headers()).
           then(res => {
               this.setState({rows: res.data });
               sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
               hideLoad();
               alert("Added!");
           }). catch(error => {
               handleError(error);
               hideLoad();
           });
   }

//remove user
 removeOne = (e, id="", name="")=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove the "+name+" ?"))
   return null;

    showLoad();
    axios.delete(window.API_URL+'remove-firewall-rule?name='+name.trim()+'&id_ufw='+id,  headers() )
         .then(res => {
              hideLoad();
              this.setState({rows: res.data });
              sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));

              alert("Removed!");
          }).catch(error => {
              handleError(error);
              hideLoad();
          });
 }

 handlePortChange = (event) => {
      document.getElementById("portInp").value = event.target.value;
  }

 actionBtn =(e, action="")=>{
  e.preventDefault();
  showLoad();
  axios.get(window.API_URL+'enable-disable-firewall?actionBtn='+action,  headers() )
             .then(res => {
                   hideLoad();
                   this.setState({rows: res.data });
                   sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
               }).catch(error => {
                  handleError(error);
                  hideLoad();
              });
 }

  render() {
    return (
       <>

         <form onSubmit={this.addNewOne} action="#" method="POST">
            <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
                 <div class="col-md-2">
                    <select name="platform" class="form-control" onChange={this.handlePortChange}>
                      <option value="">Select</option>
                      <option value="22/tcp">22 - SSH</option>
                      <option value="22/tcp,80/tcp,443/tcp,Nginx HTTP,2525/tcp,25/tcp,587/tcp,110/tcp,995/tcp,143/tcp,993/tcp,465/tcp,Bind9">All Utils Ports For Web And Email Server</option>
                      <option value="80/tcp">80 - HTTP</option>
                      <option value="443/tcp">443 - HTTPS</option>
                      <option value="Nginx HTTP">Nginx HTTP</option>
                      <option value="40000:40100/tcp">40000:40100/tcp</option>
                      <option value="Bind9">Bind9</option>
                      <option value="2525/tcp">2525/tcp</option>

                      <option value="8080/tcp">8080 - Alternative HTTP</option>
                      <option value="8443/tcp">8443 - Alternative HTTPS</option>
                      <option value="8000/tcp">8000 - Alternative HTTP (common for development)</option>
                      <option value="25/tcp,587/tcp,110/tcp,995/tcp,143/tcp,993/tcp,465/tcp,Bind9">All mail Port</option>
                      <option value="25/tcp">25 - SMTP</option>
                      <option value="587/tcp">587 - SMTPS</option>
                      <option value="110/tcp">110 - POP3</option>
                      <option value="995/tcp">995 - POP3S</option>
                      <option value="143/tcp">143 - IMAP</option>
                      <option value="993/tcp">993 - IMAPS</option>
                      <option value="465/tcp">465 - SMTPS (deprecated, encrypted)</option>
                   </select>
                 </div>
                 <div class="col-md-3">
                   <input type="text" name="name" id="portInp" class="form-control" required={true} placeholder="443/tcp"/>
                 </div>
                    <div class="col-md-2">
                      <select name="platform" name="dowhat" class="form-control" >
                         <option value="allow">ALLOW</option>
                         <option value="deny">DENY</option>
                      </select>
                    </div>
                 <div class="col-md-2">
                   <button class="btn btn-primary btn_small" type ="submit" >Add New Rule</button>
                 </div>

           </div>
         </form>
           <div class="height20px"></div>

           <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
             <div class="col-md-3">
               <a href="#" class="btn btn btn-success btn_small" onClick={this.getData}>
                <i class="bi bi-arrow-clockwise"></i> Show rules
               </a>
             </div>
             <div class="col-md-9 text_align_right">
               <a href="#" class="btn btn-info btn_small" onClick={e=>this.actionBtn(e,"enable")}>Enable Firewall</a> &nbsp;&nbsp;
               <a href="#" class="btn btn-secondary btn_small" onClick={e=>this.actionBtn(e,"disable")}>Disable Firewall</a>
             </div>
           </div>

           <div class="clear"></div>
       <div class="row">
         <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col" style={{width:"30px"}}>id</th>
                <th scope="col">Name</th>
                <th scope="col" class="text_align_center"></th>
                <th scope="col" class="text_align_center">Remove</th>
             </tr>
            </thead>
            <tbody>
            {this.state.rows.map(row=>
             <tr>
                 <td> {row.id} </td>
                 <td> {row.name} </td>
                 <td> {row.type} </td>
                  <td class="text_align_center">
                    <a href="#" onClick={e=>this.removeOne(e, row.id, (row.name.includes("where") ? row.type.trim().replace(/^.*?- /, "") : row.name))}>
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
                      <i class="bi bi-arrow-clockwise"></i> Show rules
                   </a>
                </p>
            </div>
         </div>

      </>
    );
  }
}

export default Firewall;