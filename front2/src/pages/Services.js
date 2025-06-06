import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';

class Firewall extends React.Component {
 sessionStorageName = 'list-of-services';
 constructor(props) {
       super(props);
       this.state = {rows : [],
                     tmpRows : [],
                     editsRows:[],
                     }
 }

 componentDidMount(){
     try{
       let data = sessionStorage.getItem(this.sessionStorageName);
       if(data!=null && data !="")
       this.setState({rows: JSON.parse(data), tmpRows:JSON.parse(data) });
     }catch(err){}
  }

// getListOfUser
 getData =(e)=>{
     e.preventDefault();
     showLoad();
     axios.get(window.API_URL+'get-list-of-services',  headers() )
                .then(res => {
                     this.setState({rows: res.data, tmpRows:res.data });
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
     axios.put(window.API_URL+'add-new-service',body, headers()).
           then(res => {
               this.setState({rows: res.data, tmpRows:res.data });
               sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
               hideLoad();
               alert("Added!");
           }). catch(error => {
               handleError(error);
               hideLoad();
           });
   }

//action
 actionBtn = (e, name="", action="")=>{
   e.preventDefault();
   if(action=="remove" && !window.confirm("Are you sure you want to "+action+" the "+name+" ?"))
   return null;

    showLoad();
    axios.get(window.API_URL+'services-action?name='+name+"&actionBtn="+action,  headers() )
         .then(res => {
              hideLoad();
              var searchVal = document.getElementById("searchInput").value.toUpperCase();

              this.setState({rows: (res.data !=null && searchVal.trim() != "" ? res.data.filter(v=>v.name.toUpperCase().includes(searchVal) ||
                                                v.description.toUpperCase().includes(searchVal)) : res.data),
                            tmpRows:res.data });
              sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
           }).catch(error => {
              handleError(error);
              hideLoad();
          });
 }

 statusService = (e, name="" )=>{
   e.preventDefault();
    showLoad();
    axios.get(window.API_URL+'service-show-status?name='+name,  headers() )
         .then(res => {
              hideLoad();
              showAlert(res.data);
          }).catch(error => {
              handleError(error);
              hideLoad();
          });
 }

 search =(e)=>{
   const val = e.target.value.toUpperCase();
   const data = this.state.tmpRows.filter(v=>v.name.toUpperCase().includes(val) ||
                                             v.description.toUpperCase().includes(val));
   this.setState({rows:data});
 }

  yourServices =(e)=>{
    e.preventDefault();
    const data = this.state.tmpRows.filter(v=>v.allow=="yes");
    this.setState({rows:data});
  }


 editFile=(e, fileName="")=>{
     e.preventDefault();
      showLoad();
    axios.get(window.API_URL+'get-file-content?pathFile=/etc/systemd/system/'+fileName,  headers() )
         .then(res => {
              hideLoad();
               let obj ={path: "/etc/systemd/system/",
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

  render() {
    return (
       <>

         <form onSubmit={this.addNewOne} action="#" method="POST">
            <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
                 <div class="col-md-2">
                   <small>Name (Without Space)</small><br/>
                   <input type="text" name="name" class="form-control" required={true} placeholder="Name"/>
                 </div>
                  <div class="col-md-2">
                   <small>Description</small><br/>
                   <input type="text" name="description" class="form-control" required={true} placeholder="Description"/>
                 </div>
                 <div class="col-md-6">
                    <small>Execute Code</small> <br/>
                    <input type="text" name="runcode" class="form-control" defaultValue="/usr/bin/java -jar /path/to/your.jar --spring.config.location=/path/to/application.properties" required={true} placeholder="Code"/>
                  </div>
                 <div class="col-md-2"><br/>
                   <button class="btn btn-primary btn_small" type ="submit" >Add New</button>
                 </div>

           </div>
         </form>
           <div class="height20px"></div>

           <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
             <div class="col-md-3">
               <a href="#" class="btn btn btn-success btn_small" onClick={this.getData}>
                <i class="bi bi-arrow-clockwise"></i> Show services
               </a>
              </div>
              <div class="col-md-3">
                <input type="text" placeholder="Enter the name" id="searchInput" class="form-control" onChange={this.search}/>
              </div>
             <div class="col-md-6 text_align_right">
               <a href="#" class="btn btn-info btn_small" onClick={this.yourServices}>Show your services</a>
            </div>
           </div>

           <div class="clear"></div>
       <div class="row">
         <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col" style={{width:"40px"}}> </th>
                <th scope="col">Name</th>
                <th scope="col">Status</th>
                <th scope="col">Description</th>
                <th scope="col" class="text_align_center" style={{width:"140px"}}>Action</th>
             </tr>
            </thead>
            <tbody>
            {this.state.rows.map(row=>
             <tr>
                 <td>
                    <a href="#" onClick={e=>this.statusService(e, row.name)} title="Show Status">
                       <i class="bi bi-question-circle"></i>
                    </a>
                 </td>
                 <td> {row.name} </td>
                 <td> {row.status} </td>
                 <td> {row.description} </td>
                  <td class="text_align_center">
                    <a href="#" onClick={e=>this.actionBtn(e, row.name,"restart")} title="Restart service">
                        <i class="bi bi-arrow-clockwise"></i>
                    </a>&nbsp;
                    <a href="#" onClick={e=>this.actionBtn(e, row.name,"enable")} title="Start service">
                      <i class="bi bi-play-circle"></i>
                    </a>&nbsp;
                     <a href="#" onClick={e=>this.actionBtn(e, row.name,"disable")} title="Stop service">
                        <i class="bi bi-stop-circle"></i>
                     </a>
                     {row.allow=="yes" ?<>&nbsp;
                      <a href="#" onClick={e=>this.editFile(e, row.name)} title="Edit Service">
                         <i class="bi bi-pencil-square"></i>
                      </a>&nbsp;
                      <a href="#" onClick={e=>this.actionBtn(e, row.name,"remove")} title="Remove service">
                         <i class="bi bi-x-circle-fill"></i>
                      </a>
                      </>:<></>}
                  </td>
               </tr>
             )}
            </tbody>
          </table>
            <div class="col-md-12">
                <p class="text_align_center">
                   <a href="#" class="btn btn btn-success btn_small" onClick={this.getData}>
                      <i class="bi bi-arrow-clockwise"></i> Show services
                   </a>
                </p>
            </div>
         </div>

      </>
    );
  }
}

export default Firewall;