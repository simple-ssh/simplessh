import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';


class Groups extends React.Component {
 sessionStorageName = 'list-of-groups';
 constructor(props) {
       super(props);
       this.state = {rows : [],
                     tmpRows : [],
                    }
 }

 componentDidMount(){
     try{
       let data = sessionStorage.getItem(this.sessionStorageName);
       if(data!=null && data !="")
       this.setState({rows:    JSON.parse(data) ,
                      tmpRows: JSON.parse(data)
                      });
     }catch(err){}
  }

// getListOfUser
 getData =(e)=>{
     e.preventDefault();
     showLoad();
     axios.get(window.API_URL+'get-list-of-groups',  headers() )
                .then(res => {
                     this.setState({rows: res.data,
                                    tmpRows: res.data });
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
     axios.put(window.API_URL+'add-new-element-to-group?name='+body.name,{}, headers()).
           then(res => {
               this.setState({rows: res.data,
                              tmpRows: res.data});
               sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
               hideLoad();
               alert("Added!");
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
    axios.delete(window.API_URL+'remove-element-from-group?name='+name,  headers() )
         .then(res => {
              hideLoad();
              this.setState({rows: res.data,
                             tmpRows: res.data});
              sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));

              alert("Removed!");
          }).catch(error => {
              handleError(error);
              hideLoad();
          });
 }

 search =(e)=>{
    const val = e.target.value.toUpperCase();
    const data = this.state.tmpRows.filter(v=>v.name.toUpperCase().includes(val) );
    this.setState({rows:data});
  }


 render() {
    return (
       <>

         <form onSubmit={this.addNewOne} action="#" method="POST">
            <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
                 <div class="col-md-4">
                   <input type="text" name="name" class="form-control" required={true} placeholder="www-data"/>
                 </div>

                 <div class="col-md-2">
                   <button class="btn btn-primary btn_small" type ="submit" >Add New one</button>
                 </div>

           </div>
         </form>
           <div class="height20px"></div>

           <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
             <div class="col-md-3">
               <a href="#" class="btn btn btn-success btn_small" onClick={this.getData}>
                <i class="bi bi-arrow-clockwise"></i> Show Groups
               </a>
             </div>
             <div class="col-md-6 text_align_right"> </div>
             <div class="col-md-3 text_align_right">
                 <input type="text" placeholder="Enter the name" id="searchInput" class="form-control" onChange={this.search}/>
             </div>
           </div>

           <div class="clear"></div>
       <div class="row">
         <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col">Name</th>
                <th scope="col" class="text_align_center">Remove</th>
             </tr>
            </thead>
            <tbody>
            {this.state.rows.map(row=>
             <tr>
                  <td>
                     <a href="#" href={window.BASE_URL+"#/groups/"+row.name}>
                       {row.name}
                     </a>
                 </td>
                  <td class="text_align_center">
                    <a href="#" onClick={e=>this.removeOne(e, row.name)}>
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
                      <i class="bi bi-arrow-clockwise"></i> Show Groups
                   </a>
                </p>
            </div>
         </div>

      </>
    );
  }
 }

export default Groups;