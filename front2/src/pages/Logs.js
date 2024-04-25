import React from 'react'
import ReactDOM from 'react-dom'

import axios from 'axios';
import { headers, hideLoad, handleError,  showLoad, showAlert, eraseCookie, logout } from './../Helpers.js';


class Home extends React.Component {

 constructor(props) {
       super(props);
       this.state = {logs:"", limit:150 }
 }

 componentDidMount(){
     showLoad();
     this.getLogs(150);
  }


  handleLogs =(e)=>{
    e.preventDefault();
    const limit = this.state.limit + 150;
    this.setState({limit:limit});
    this.getLogs(limit);
  }

  getLogs =(limit)=>{
      showLoad();
      axios.get(window.API_URL+'get-logs?limit='+limit,  headers() )
         .then(res => {

            var logs = res.data.replace(/(?:\r\n|\r|\n)/g, '<br/>');
                logs = logs.replace(/  /g,"&nbsp;&nbsp;");

            this.setState({logs: logs});
            hideLoad();
         }).catch(error => {
            hideLoad();
         });
  }

   clearLogs =(e)=>{
      e.preventDefault();

      if(!window.confirm("Are you sure you want to empty log file?"))
             return null;

      showLoad();
     axios.get(window.API_URL+'empty-logs',  headers() )
               .then(res => {
                    this.setState({logs: ""});
                     alert("Done!");
                     hideLoad();
               }).catch(error => {
                  hideLoad();
               });
  }

  render() {
    return (
         <div class="row">
          <div class="col-md-12" style={{minHeight:"80vh"}}>
               <a onClick={this.clearLogs} href="#" class="btn btn-primary btn_small">Clear log</a>
               <hr/>
               <div dangerouslySetInnerHTML={{ __html: this.state.logs }} />
               <p class="textCenter">
                 <a href="#" class="btn btn-primary btn_small" onClick={this.handleLogs}>Get More</a>
               </p>
          </div>
         </div>
    );
  }
}

export default Home;