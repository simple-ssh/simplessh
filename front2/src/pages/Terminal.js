import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';
import PopupCodeEditor from './PopupCodeEditor';

class Terminal extends React.Component {
 lastCommandSession = "lastCommand"
 constructor(props) {
       super(props);
       this.state = {rows : [], response :  "", lastCommand:"" }
 }

 componentDidMount(){
  this.setState({lastCommand: (sessionStorage.getItem(this.lastCommandSession) != null ?
                              sessionStorage.getItem(this.lastCommandSession):"") });

 }

  // run Command
  runCommand =(e)=>{
     e.preventDefault();
     const form = e.currentTarget
     const body = serialize(form, {hash: true, empty: true});

     showLoad();
     axios.put(window.API_URL+'execute-command',body, headers()).
           then(res => {
                var resp = res.data.response;
                resp = resp.replace(/(?:\r\n|\r|\n)/g, '<br/>');
                resp = resp.replace(/  /g,"&nbsp;&nbsp;");

               this.setState({rows: res.data.rows, response:resp });
               hideLoad();

                sessionStorage.setItem(this.lastCommandSession, body.name);
           }). catch(error => {
               handleError(error);
               hideLoad();
           });
   }


  render() {
    return (
       <>

         <form onSubmit={this.runCommand} action="#" method="POST">
               <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
                 <div class="col-md-8">
                   <input type="text" name="name" class="form-control" required={true} defaultValue={this.state.lastCommand} placeholder="Enter the  command, no need to enter &quot;sudo&quot; (example: apt-get -q -y install php  OR apt-get update)"/>
                 </div>
                  <div class="col-md-2">
                   <label> <input type="checkbox" name="mysql" value="yes"/> Mysql Command </label>
                 </div>
                 <div class="col-md-2">
                   <button class="btn btn-primary btn_small" type ="submit" >Run Command</button>
                 </div>
               </div>
         </form>
           <div class="height20px"></div>
           <div class="clear"></div>
           <div class="row">
               <div class="col-md-12" style={{background:"#fff",minHeight:"50vh"}}>
                    <div dangerouslySetInnerHTML={{ __html: this.state.response }} />


                    {this.state.rows.length>0 ?
                      <div class="resizableDiv" style={{maxHeight: "calc(100vh - 120px)"}}>
                           <table class="table table-hover resizableTable">
                             <thead>
                               <tr>
                                  {Object.keys(this.state.rows.length > 0? this.state.rows[0] :{}).map(key =>
                                     <th scope="col">{key}</th>
                                  )}
                               </tr>
                             </thead>
                            <tbody>
                             {this.state.rows.map((row,x)=>
                               <tr>
                                 {Object.keys(row).map(key =>
                                   <td> {row[key].length>100 ? <div class="resizeData">{row[key]}</div>:
                                            <>{row[key]}</>}  </td>
                                 )}
                               </tr>
                              )}
                            </tbody>
                           </table>
                           </div>
                         :
                        <></>
                    }
               </div>
            </div>

      </>
    );
  }
}

export default Terminal;