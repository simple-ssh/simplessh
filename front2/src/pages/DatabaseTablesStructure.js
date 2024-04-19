import React from 'react'
import ReactDOM from 'react-dom'
//import AppNavbar from './../layouts/AppNavbar';
import { Link, useParams } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert, generatePassword } from './../Helpers.js';
import { optionType,optionCollation  } from './../layouts/SelectType.js';
import axios from 'axios';
import serialize from 'form-serialize';

class DatabaseTablesStructure extends React.Component {
 sessionStorageNameDb = 'list-of-database-tables';
 defObj= {fname:"",fnameFrom:"", ftype:"VARCHAR", flength:"", fcollation:"", fnull:"", fdefault:"", fprimary:"", fautoincrement:""};
 constructor(props) {
       super(props);
       this.state = {rows : [],
                     text: '',
                     title: '',
                     newTable:"none",
                     fieldsList:[this.defObj],
                     editAdd:"add",
                    }
 }

 componentDidMount(){
   this.getData(this.props.params.dbname, this.props.params.tbname);
 }

getDataInit =(e)=>{
 e.preventDefault();
 this.getData(this.props.params.dbname, this.props.params.tbname);
}

// getListOfUser or database
 getData =( database, table="")=>{
     showLoad();
     axios.get(window.API_URL+'get-list-of-database-table-structure?database='+database+"&table="+table,  headers() )
                .then(res => {
                      this.setState({rows: res.data });
                      hideLoad();
                  }).catch(error => {
                     handleError(error);
                     hideLoad();
                 });
  }


  // add new database
   addNewData =(e)=>{
     e.preventDefault();

     showLoad();
    //console.log(JSON.stringify(this.state.fieldsList));
     axios.put(window.API_URL+'add-edit-database-table-column?table='+this.props.params.tbname+
                                                         '&database='+this.props.params.dbname+
                                                         "&typeEditAdd="+this.state.editAdd,
               this.state.fieldsList, headers()).
           then(res => {
               this.setState({rows: res.data.rows });

               if(res.data.response !="")
                 alert(res.data.response);

                hideLoad();

                const updatedFieldsList = this.state.fieldsList.map(item => ({
                  ...item, // Spread the item object to retain other properties
                  fnameFrom: '', // Update the name property to fnameFrom
                }));

                //console.log(JSON.stringify(updatedFieldsList));
                // Set the updatedFieldsList back to the component state
                this.setState({ fieldsList: updatedFieldsList });

            }). catch(error => {
              handleError(error);
              hideLoad();
           });
   }


//remove
 removeData = (e, name="")=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove the: "+name+" ?"))
   return null;

    showLoad();
    axios.delete(window.API_URL+'remove-database-table-column?table='+this.props.params.tbname+
                              '&database='+this.props.params.dbname+"&column="+name,  headers() )
         .then(res => {
              hideLoad();
              this.setState({rows: res.data.rows });
              if(res.data.response !="")
                 alert(res.data.response);

            }).catch(error => {
              handleError(error);
              hideLoad();
          });
 }

 addField=(e)=>{
   e.preventDefault();
    const obj = [ ...this.state.fieldsList];
    obj.push(this.defObj);
    this.setState({ fieldsList:obj });
 }
 
 removeField=(e, nr=0)=>{
   e.preventDefault();
   const obj = [ ...this.state.fieldsList];
       obj.splice(nr, 1);
    this.setState({ fieldsList:obj  });
 }

 updValue=(e, index,  type="inp")=>{
    let listObj = [ ...this.state.fieldsList];
    let tempObj = { ...listObj[index]};

   if(type=="inp" || type=="select"){
     tempObj[e.target.name] = e.target.value;
   }else if(type=="checkbox"){
     tempObj[e.target.name] = e.target.checked ? "yes":"";
   }
    listObj[index] = tempObj;

   this.setState({ fieldsList: listObj });
 }

 newTableBtn=(e)=>{
   e.preventDefault();
   this.setState({ newTable: this.state.newTable =="none"? "block" : "none",
                   fieldsList: [this.defObj],
                   editAdd:"add" });
 }

  // add new database
    edit =(e, x)=>{
      e.preventDefault();
      window.scrollTo(0, 0);
      const line = this.state.rows[x];
       var fType= line.Type;
       var splitLength = fType.split("(");
       var splitSpace = fType.split(" ");

       this.setState({fieldsList: [{fname:     line.Field,
                                    fnameFrom: line.Field,
                                    ftype:     (fType.includes("(") ? splitLength[0]:(fType.includes(" ")? splitSpace[0] : fType) ).toUpperCase(),
                                    flength:   line.Type.includes("(") ? splitLength[1].replace(")",""):"", fcollation:"",
                                    fnull:     line["Null"]=="YES" ? "yes":"", fdefault:line["Default"]=="NULL" ? "": line["Default"],
                                    fprimary:  line.Key, fautoincrement: line.Extra}],
                       newTable:"block",
                       editAdd:"edit",});

    }

  render() {
    return (
       <div>

         <form onSubmit={this.addNewData} id="updateEditForm" action="#" method="POST" style={{display:this.state.newTable}}>
           <div class="row align-items-center shadow-sm bg-body rounded paddingBottomTopForm">

           {this.state.fieldsList.map((row,x)=>
            <div class="blockFields row">
             <div class="col-md-2">
                <small>Field Name </small> <br/>
                <input type="text" name="fname" onChange={e=>this.updValue(e,x)} value={row.fname} class="form-control" required={true} placeholder="Field Name"/>
             </div>
             <div class="col-md-2">
                <small>Type </small><br/>
                <select name="ftype" class="form-control" value={row.ftype} onChange={e=>this.updValue(e,x,"select")} required={true} >
                 {optionType()}
                </select>
             </div>
             <div class="col-md-2">
               <small>Length</small> <br/>
                <input type="text" name="flength" value={row.flength} onChange={e=>this.updValue(e,x)}   class="form-control" title="Length" placeholder="Length"/>
              </div>
              <div class="col-md-2">
                 <small>Collation</small> <br/>
                  <select dir="ltr" name="fcollation" value={row.fcollation} onChange={e=>this.updValue(e,x,"select")} class="form-control">
                       {optionCollation()}
                  </select>
              </div>
             <div class="col-md-2"><br/>
               <label><input type="checkbox" name="fnull" checked={row.fnull !=""? true:false} onChange={e=>this.updValue(e,x,"checkbox")} value="NULL" /> NULL</label>
             </div>
              <div class="col-md-2">
                 <small>Default</small> <br/>
                 <input type="text" name="fdefault" value={row.fdefault} class="form-control"  onChange={e=>this.updValue(e,x)} title="Default" placeholder="Default"/>
              </div>
              <div class="height10px"></div>
             <div class="col-md-2">
                <label><input type="checkbox" name="fautoincrement" checked={row.fautoincrement !=""? true:false} onChange={e=>this.updValue(e,x,"checkbox")} value="NULL" /> Auto increment</label>
             </div>
             <div class="col-md-2">
               <label>  <input type="checkbox" name="fprimary" checked={row.fprimary !=""? true:false} value="primary" onChange={e=>this.updValue(e,x,"checkbox")}/> Primary Key</label>
             </div>
             <div class="clear"></div>
           </div>
           )}
              <div class="height5px"></div>
              <div class="col-md-12">
                <p class="text_align_center">
                  <a href="#" class="btn btn-secondary btn_small" onClick={this.newTableBtn}> Cancel </a>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  <button class="btn btn-primary btn_small" type ="submit" > {this.state.editAdd=="add"?"Add New":"Update"} </button>
                </p>
             </div>

           </div>
         </form>
              <div class="height5px"></div>
              <nav aria-label="breadcrumb">
                 <ol class="breadcrumb databaseBread">
                  <li class="breadcrumb-item">
                      <a href={window.BASE_URL+"#/database-mysql/"} title="Databases" >
                        <i class="bi bi-hdd-stack"></i> Databases
                      </a>
                  </li>
                   <li class="breadcrumb-item">
                     <a href={window.BASE_URL+"#/database-mysql/"+this.props.params.dbname} title={this.props.params.dbname} >
                       {this.props.params.dbname}
                     </a>
                   </li>
                      <li class="breadcrumb-item">
                       <a href="#" onClick={this.getDataInit} >
                        {this.props.params.tbname}
                      </a>
                    </li>
                 </ol>
                  &nbsp;&nbsp;
                  <a href="#" onClick={this.newTableBtn} class="quickLinksFileManager">
                      <i class="bi bi-plus-square"></i> Add Column
                  </a> &nbsp;&nbsp;
                   <a href={window.BASE_URL+"#/database-mysql-table-data/"+this.props.params.dbname+"/"+this.props.params.tbname}  class="quickLinksFileManager">
                     <i class="bi bi-table"></i> View
                   </a>
               </nav>
         <div class="clear"></div>
         <div class="row">
          <table class="table table-striped table-hover">
            <thead>
              <tr>
                {Object.entries(this.state.rows.length>0? this.state.rows[0] :{}).map(([key, value]) =>
                   <th scope="col">{key}</th>
                 )}
                 <th scope="col">Action</th>
              </tr>
            </thead>
            <tbody>
            {this.state.rows.map((row,x)=>
              <tr>
                 {Object.entries(row).map(([key, value]) =>
                     <td> {value} </td>
                 )}
                <td>
                   <a href="#" onClick={e=>this.edit(e, x )}>
                       <i class="bi bi-pencil-square"></i>
                   </a>&nbsp;&nbsp;&nbsp;
                   <a href="#" onClick={e=>this.removeData(e, row.Field )}>
                      <i class="bi bi-trash3"></i>
                   </a>
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
export default (props) => (
                   <DatabaseTablesStructure
                       {...props}
                       params={useParams()}
                   />
               );
