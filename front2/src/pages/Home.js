import React from 'react'
import ReactDOM from 'react-dom'
//import AppNavbar from './../layouts/AppNavbar';
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert, eraseCookie, logout } from './../Helpers.js';


class Home extends React.Component {

 constructor(props) {
       super(props);
       this.state = {apiresponse : [], text: '', title: '' }
 }

 componentDidMount(){
  }

  hideMenu =(id="")=>{ }

  render() {
    return (
         <div class="row centerHomeLinks">
           <div class="col-md-3 col-6">
              <a class="nav-link" href={window.BASE_URL+"#/fil-manager"}>
               <i class="bi bi-folder"></i> <br/> FileManager
             </a>
           </div>
           <div class="col-md-3 col-6">
                <a class="nav-link" href={window.BASE_URL+"#/database-mysql"}
                                                           onClick={e=>this.hideMenu("usersDatabase")}>
                  <i class="bi bi-hdd-stack"></i> <br/> Database
               </a>
           </div>
           <div class="col-md-3 col-6">
             <a class="nav-link" href={window.BASE_URL+"#/database-mysql-users"}
                                                onClick={e=>this.hideMenu("usersDatabase")}>
               <i class="bi bi-person"></i> <br/> Database Users
             </a>
          </div>

           <div class="col-md-3 col-6">
              <a class="nav-link" href={window.BASE_URL+"#/emails"}>
                <span>@</span> <br/> Email
             </a>
          </div>

          <div class="clear"></div>

           <div class="col-md-3 col-6">
             <a class="nav-link" href={window.BASE_URL+"#/settings"} >
                <i class="bi bi-gear-wide-connected"></i> <br/> SSH connection
             </a>
           </div>
           <div class="col-md-3 col-6">
             <a class="nav-link" href={window.BASE_URL+"#/system-users"} >
               <i class="bi bi-person-badge"></i> <br/> App Users
             </a>
           </div>
           <div class="col-md-3 col-6">
             <a class="nav-link" href={window.BASE_URL+"#/installations"}>
               <i class="bi bi-mouse2"></i>  <br/> Installations
             </a>
           </div>

           <div class="clear"></div>

           <div class="col-md-3 col-6">
            <a class="nav-link" href={window.BASE_URL+"#/users-ftp"}>
              <i class="bi bi-person"></i> <br/> Ubuntu Users/Ftp
            </a>
          </div>
           <div class="col-md-3 col-6">
            <a class="nav-link" href={window.BASE_URL+"#/domains"}>
              <i class="bi bi-globe"></i> <br/> Domains
            </a>
          </div>


           <div class="col-md-3 col-6">
               <a class="nav-link" href={window.BASE_URL+"#/firewall"}>
                 <i class="bi bi-bricks"></i> <br/> Firewall
               </a>
          </div>
           <div class="col-md-3 col-6">
            <a class="nav-link" href={window.BASE_URL+"#/services"}>
             <i class="bi bi-plugin"></i> <br/> Services
            </a>
          </div>

           <div class="col-md-3 col-6">
               <a class="nav-link" href="#" onClick={logout}>
                  <i class="bi bi-plug"></i> <br/> Exit
              </a>
          </div>


        </div>
    );
  }
}

export default Home;