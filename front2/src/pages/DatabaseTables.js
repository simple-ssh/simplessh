import React from 'react'
import ReactDOM from 'react-dom'
//import AppNavbar from './../layouts/AppNavbar';
import { Link, useParams } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert, getSelectedRows, bulkUncheckAll,
         generatePassword } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';
import { optionType,optionCollation  } from './../layouts/SelectType.js';
import {SetBulk} from './../UtilsComponents.js';

class DatabaseTables extends React.Component {
 sessionStorageNameDb = 'list-of-database-tables';
 defObj= {fname:"", ftype:"INT", flength:"10", fcollation:"", fnull:"ye", fdefault:"", fprimary:"", fautoincrement:""};
 constructor(props) {
       super(props);
       this.state = {rows : [],
                     text: '',
                     title: '',
                     newTable:"none",
                     makeSql:"none",
                     fieldsList:[this.defObj],
                    }
 }

 componentDidMount(){
    this.getData(this.props.params.dbname);
  }

// getListOfUser or database
 getData =( database)=>{
     showLoad();
     axios.get(window.API_URL+'get-list-of-database-tables?database='+database,  headers() )
                .then(res => {
                      this.setState({rows: res.data });
                      hideLoad();
                  }).catch(error => {
                     handleError(error);
                     hideLoad();
                 });
  }


  // add new table
   addNewData =(e)=>{
     e.preventDefault();
     var tbName= document.getElementById("tableNameAdd").value;
     showLoad();

     axios.put(window.API_URL+'add-new-database-table?table='+tbName+'&database='+this.props.params.dbname,
               this.state.fieldsList, headers()).
           then(res => {
               this.setState({rows: res.data.rows });

              alert(res.data.response !=""? res.data.response:
                    "Database has ben added, check on the list bellow, if is not than there is an error");

               hideLoad();
            }). catch(error => {
              handleError(error);
              hideLoad();
           });
   }

//remove user
 removeData = (e, name="")=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove the "+(name ==""? "selected items":"")+" ?"))
   return null;

   var ids=getSelectedRows("checkboxBulk");
   if(name =="" && ids.length==0)
      return null;

   var dbName = this.props.params.dbname;

   let tables = name !="" ? dbName+'.'+name : ( ids.length>1 ? (dbName+'.'+ids.join(", "+dbName+'.')): dbName+'.'+ids[0]);
   const body = {tables:tables, database:dbName};

    showLoad();
    axios.post(window.API_URL+'remove-database-table', body, headers() )
         .then(res => {
              hideLoad();
              this.setState({rows: res.data.rows });
              if(res.data.response !="")
                 alert(res.data.response);

            }).catch(error => {
              handleError(error);
              hideLoad();
          });
   bulkUncheckAll("checkboxBulk");
 }

// empty table
 emptyTable = (e, name="")=>{
    e.preventDefault();
    if(!window.confirm("Are you sure you want to empty the table "+name+" ?"))
    return null;

    var dbName = this.props.params.dbname;
    const body = {table:name, database:dbName};

     showLoad();
     axios.post(window.API_URL+'empty-database-table', body, headers() )
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
   this.setState({ newTable: this.state.newTable =="none"? "block" : "none", makeSql:"none" });
 }

closeNewTable=(e)=>{
   e.preventDefault();
   this.setState({ newTable: "none" });
 }

// run sql
 showSql =(e)=>{
   e.preventDefault();
   this.setState({ makeSql: this.state.makeSql =="none"? "block" : "none" , newTable:"none" });
 }

  runSql =(e)=>{
    e.preventDefault();
     var value = document.getElementById("sqlTextare").value;
     if(value =="")
       return null;
        //replace ' -> ` and escape: ` ->  \`
        if(value.includes("CREATE TABLE")){
          value = value.replaceAll("'","`");
          value = value.replaceAll('`','\\\`');
        }

      showLoad();
     axios.put(window.API_URL+'execute-query-tables' ,
              {query: value, database: this.props.params.dbname }  , headers()).
           then(res => {
               hideLoad();
               this.setState({rows: res.data.rows, queryView:true });
               if(res.data.response !="")
                 alert(res.data.response);

            }). catch(error => {
              handleError(error);
              hideLoad();
           });
  }


  render() {
    return (
       <div>
          <form onSubmit={this.addNewData} action="#" method="POST" style={{display:this.state.newTable}}>
           <div class="row align-items-center shadow-sm bg-body rounded paddingBottomTopForm">
             <div class="col-md-12">
                 <small>Table Name</small> <br/>
                   <input type="text" name="tname" id="tableNameAdd" class="form-control" required={true} placeholder="Table Name"/>
             </div>

             <div class="clear"></div>
           {this.state.fieldsList.map((el,x)=>
            <div class="blockFields row">
             <div class="col-md-2">
                <small>Field Name</small> <br/>
                <input type="text" name="fname" onChange={e=>this.updValue(e,x)} class="form-control" required={true} placeholder="Field Name"/>
             </div>
             <div class="col-md-2">
                <small>Type</small><br/>
                <select name="ftype" class="form-control" onChange={e=>this.updValue(e,x,"select")} required={true} >
                      {optionType()}
                </select>
             </div>
             <div class="col-md-2">
               <small>Length</small> <br/>
                <input type="text" name="flength" onChange={e=>this.updValue(e,x)} defaultValue={10} class="form-control" title="Length" placeholder="Length"/>
              </div>
              <div class="col-md-2">
                 <small>Collation</small> <br/>
                  <select dir="ltr" name="fcollation" onChange={e=>this.updValue(e,x,"select")} class="form-control">
                    {optionCollation()}
                  </select>
              </div>
             <div class="col-md-2"><br/>
               <label><input type="checkbox" name="fnull" defaultChecked={true} onChange={e=>this.updValue(e,x,"checkbox")} value="NULL" /> NULL</label>
             </div>
              <div class="col-md-2">
                 <small>Default</small> <br/>
                 <input type="text" name="fdefault" class="form-control" onChange={e=>this.updValue(e,x)} title="Default" placeholder="Default"/>
              </div>
              <div class="height10px"></div>
             <div class="col-md-2">
                <label><input type="checkbox" name="fautoincrement" onChange={e=>this.updValue(e,x,"checkbox")} value="NULL" /> Auto increment</label>
             </div>
             <div class="col-md-2">
               <label>  <input type="radio" name="fprimary"  value="primary" onChange={e=>this.updValue(e,x,"checkbox")}/> Primary Key</label>
             </div>
             <div class="col-md-8 text_align_right">
                <a href="#" onClick={e=>this.removeField(e,x)}><i class="bi bi-x-octagon"></i></a>
             </div>
             <div class="clear"></div>
           </div>
           )}
           <div class="height10px"></div>
              <div class="col-md-12">
                 <p class="text_align_center">
                   <a href="#" onClick={this.addField}> <i class="bi bi-plus-square"></i> Add new field</a>
                 </p>
              </div>
             <div class="col-md-12">
                <p class="text_align_center">
                 <a class="btn btn-secondary btn_small" href="#" onClick={this.closeNewTable}>Cancel</a>
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                 <button class="btn btn-primary btn_small" type="submit">Add New Table</button>
                </p>
             </div>

           </div>
         </form>
             <div style={{display:this.state.makeSql}}>
               <div class="height5px"></div>
               <div class="col-md-12">
                  <textarea type="text" name="sql" id="sqlTextare" class="form-control" style={{minHeight:"150px"}}
                            placeholder="CREATE TABLE Persons (&#10;id varchar(255),&#10;firstName varchar(255),&#10;PRIMARY KEY (id)&#10;) ENGINE=InnoDB DEFAULT CHARSET=utf8" required={true}></textarea>
                </div>
                 <div class="height5px"></div>
                <div class="col-md-12">
                  <p class="text_align_center">
                    <button class="btn btn-primary btn_small"  onClick={this.runSql}> Run </button>
                  </p>
               </div>
             </div>

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
                 </ol>

                 &nbsp; <a href="#" class="quickLinksFileManager" onClick={this.newTableBtn}>
                             <i class="bi bi-plus-square"></i> Add new Table
                       </a>
                &nbsp; <a href="#" class="quickLinksFileManager redColor" onClick={e=>this.removeData(e,"")}>
                          <i class="bi bi-trash3"></i> Remove
                       </a>
                &nbsp; <a href="#" onClick={this.showSql} class="quickLinksFileManager">
                          <i class="bi bi-code-slash"></i> Run Sql
                       </a>
              </nav>
         <div class="clear"></div>
         <div class="row">
          <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col" style={{width:"25px"}}>
                  <SetBulk cssClass="checkboxBulk"/>
                </th>
                <th scope="col">Name</th>
                <th scope="col">Size</th>
                <th scope="col">Structure</th>
                <th scope="col">Empty</th>
                <th scope="col">Remove</th>
              </tr>
            </thead>
            <tbody>
            {this.state.rows.map(row=>
              <tr>
                <td> <input class="checkboxBulk" type="checkbox" value={row.name}/></td>
                <td>
                 <a href={window.BASE_URL+"#/database-mysql-table-data/"+this.props.params.dbname+"/"+row.name} >
                   <i class="bi bi-table"></i> {row.name}
                </a>
                </td>
                <td>{row.size} MB</td>
                <td>
                   <a href={window.BASE_URL+"#/database-mysql-table-structure/"+this.props.params.dbname+"/"+row.name}>
                      <i class="bi bi-layout-text-sidebar-reverse"></i> Structure
                   </a>
                 </td>
                 <td>
                    <a href="#" onClick={e=>this.emptyTable(e, row.name)}>
                      <i class="bi bi-calendar-x"></i> Empty Table
                    </a>
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

        </div>
       </div>
    );
  }
}

export default (props) => (
                   <DatabaseTables
                       {...props}
                       params={useParams()}
                   />
               );