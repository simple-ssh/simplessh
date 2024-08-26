import React, { Component } from 'react';
import { Route, Routes,  HashRouter , Redirect} from 'react-router-dom';
import axios from 'axios';
import { headers, hideLoad, showLoad, showAlert, getCookie } from './Helpers.js';
import Login from './layouts/Login.js';
import Header from './layouts/Header';
import Footer from './layouts/Footer';

/***Routers**/
import Home from './pages/Home';
import Installations from './pages/Installations';
import UsersFtp from './pages/UsersFtp';
import Domains from './pages/Domains';
import FileManager from './pages/FileManager';
import Database from './pages/Database';
import DatabaseUsers from './pages/DatabaseUsers';
import DatabaseTables from './pages/DatabaseTables';
import DatabaseTablesStructure from './pages/DatabaseTablesStructure';
import DatabaseTablesData from './pages/DatabaseTablesData';
import Firewall from './pages/Firewall';
import Services from './pages/Services';
import Settings from './pages/Settings';
import Emails from './pages/Emails';
import SystemUsers from './pages/SystemUsers';
import Logs from './pages/Logs';
import Groups from './pages/Groups';
import SubGroups from './pages/SubGroups';
import DnsEditor from './pages/DnsEditor';
import PopupCodeEditor from './pages/PopupCodeEditor';
/*** end Routers**/

class DefaultTemplate extends React.Component {

constructor(props) {
       super(props);
       this.state = { accounts : [],
                      activeAcc:"",
                      isLogin: (getCookie("tokenauth") =="" || getCookie("tokenauth") == null ? true : false),
                      terminalCount:0,
                   }
  }

   componentDidMount(){
         const idStorage = localStorage.getItem("id");

         if(idStorage !="" && idStorage !=null)
           this.setState({activeAcc: idStorage});

           this.getData();
    }

   updateDataByClick =(e)=>{
      e.preventDefault();
      this.getData();
   }
 // getList Of User
  getData =()=>{
    axios.get(window.API_URL+'get-header-list-of-accounts',  headers() )
         .then(res => {
              this.setState({accounts: res.data, isLogin: false });
              const idStorage = localStorage.getItem("id");
              if(((idStorage =="" || idStorage ==null) && res.data.length>0) || res.data.length==1){
                localStorage.setItem("id", res.data[0]["id"]);
                this.setState({activeAcc: res.data[0]["id"]});
              }


          }).catch(error => {
                if (error.response && (error.response.status===401 || error.response.status===403)) {
                    this.setState({isLogin: true});
                 } else {
                    this.setState({isLogin: false});
                }

          });
   }


   setAcc=(e)=>{
     sessionStorage.clear();
     localStorage.setItem("id", e.target.value);
     this.setState({activeAcc: e.target.value});
     window.location.reload();
    }

    getLogs=(e)=>{
      e.preventDefault();
      axios.get(window.API_URL+'get-logs?limit=250',  headers() )
           .then(res => {
              showAlert(res.data);
           }).catch(error => { });
    }


render() {
  return (
        <>
        <div id="ajaxGif"></div>
        <a href="#" id="updateAcc" onClick={this.updateDataByClick}></a>
        <div id="successDone"><i class="bi bi-check-circle"></i></div>

        {this.state.isLogin ? <Login/> :<>

             <a href="#" class="logsBtnBottom" title="Logs" onClick={this.getLogs}> <i class="bi bi-info-lg"></i> Logs</a>

             {this.state.accounts.length>0 ?
              <select class="topAccountSelect" onChange={this.setAcc}>
               {this.state.accounts.map(acc=>
                 <option value={acc.id} selected={this.state.activeAcc ==acc.id}>{acc.sshHost+"@"+acc.sshLog}</option>
                )}
              </select>:<></>}

         <Header/>
             <HashRouter>
               <main class="flex-shrink-0">
                 <div class="container-fluid">
                   <div class="row containerMinHeight">
                     <div class="col-md-12" style={{padding:"0 20px"}}>
                       <Routes>
                        <Route path='/' element={<Home/>} />
                        <Route exact path='/installations' element={<Installations/>} />
                        <Route exact path='/users-ftp' element={<UsersFtp/>} />
                        <Route exact path='/domains' element={<Domains/>} />
                        <Route exact path='/domains/dns/:domain' element={<DnsEditor/>} />
                        <Route exact path='/fil-manager' element={<FileManager/>} />
                        <Route exact path='/database-mysql' element={<Database/>} />
                        <Route exact path='/database-mysql/:dbname' element={<DatabaseTables/>} />
                        <Route exact path='/database-mysql-table-structure/:dbname/:tbname' element={<DatabaseTablesStructure/>} />
                        <Route exact path='/database-mysql-table-data/:dbname/:tbname' element={<DatabaseTablesData/>} />
                        <Route exact path='/database-mysql-users' element={<DatabaseUsers/>} />
                        <Route exact path='/firewall' element={<Firewall/>} />
                        <Route exact path='/services' element={<Services/>} />
                        <Route exact path='/settings' element={<Settings/>} />
                        <Route exact path='/emails' element={<Emails/>} />
                        <Route exact path='/system-users' element={<SystemUsers/>} />
                        <Route exact path='/system-logs' element={<Logs/>} />
                        <Route exact path='/groups' element={<Groups/>} />
                        <Route path='/groups/:groupName' element={<SubGroups/>} />
                        </Routes>
                     </div>
                   </div>
                   <PopupCodeEditor/>
                 </div>
               </main>
              </HashRouter>


              <Footer/>
          </>}
        </>
    );
  }
}

export default DefaultTemplate;