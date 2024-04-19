import React from 'react'

import { Link, useParams } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';


class SubGroups extends React.Component {

  constructor(props) {
       super(props);
       this.state = {rows : [],
                     tmpRows : [],
                    }
 }

 componentDidMount(){
      this.getData();
   }

// getListOfUser
 getData =()=>{
     showLoad();
     axios.get(window.API_URL+'get-list-of-subgroups?name='+this.props.params.groupName,  headers() )
                .then(res => {
                     this.setState({rows: res.data });
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
     //let { groupName } = useParams();
     showLoad();
     axios.put(window.API_URL+'add-new-element-to-subgroup?name='+this.props.params.groupName+'&user='+body.name, {}, headers()).
           then(res => {
               this.setState({rows: res.data });
               hideLoad();
               alert("Added!");
           }). catch(error => {
               handleError(error);
               hideLoad();
           });
   }

//remove user
 removeOne = (e,  user="")=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove the "+user+" ?"))
   return null;
    //let { groupName } = useParams();
    showLoad();
    axios.delete(window.API_URL+'remove-element-from-subgroup?name='+this.props.params.groupName+'&user='+user,  headers() )
         .then(res => {
              hideLoad();
              this.setState({rows: res.data });
              alert("Removed!");
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
                 <div class="col-md-4">
                   <input type="text" name="name" class="form-control" required={true} placeholder="www-data"/>
                 </div>

                 <div class="col-md-2">
                   <button class="btn btn-primary btn_small" type ="submit" >Add New User to: {this.props.params.groupName}</button>
                 </div>

           </div>
         </form>
           <div class="height20px"></div>

       <div class="row">
         <table class="table table-striped table-hover">
            <thead>

             <tr>
                 <th scope="col">
                   <a href={window.BASE_URL+"#/groups"} style={{textDecoration:"none"}}>
                     <i class="bi bi-arrow-left"></i>
                     Back to Groups
                    </a>
                 </th>
                 <th scope="col" class="text_align_center"> </th>
              </tr>
            </thead>
            <tbody>

            <tr>
              <td><b>{this.props.params.groupName}</b></td>
              <td></td>
            </tr>

            {this.state.rows.map(row=>
              <tr>
                  <td> {row.name} </td>
                  <td class="text_align_center">
                    <a href="#" onClick={e=>this.removeOne(e, row.name)}>
                       <i class="bi bi-x-circle-fill"></i>
                    </a>
                  </td>
               </tr>
             )}
            </tbody>
          </table>

         </div>

      </>
    );
  }
 }


export default (props) => ( <SubGroups
                                   {...props}
                                   params={useParams()}
                            />);

