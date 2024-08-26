import React from 'react'
import axios from 'axios';
import { headers, hideLoad, showLoad, showAlert, eraseCookie, logout } from './../Helpers.js';

class Header extends React.Component {
 sessionStorageName ="tokenauth";

 constructor(props) {
       super(props);
       this.state = {accounts : [], activeAcc:"", isLogin: true, mobileMenuOpen:false, lastUrl:"#/database-mysql"  }
  }

 componentDidMount(){
        //var hash = window.location.hash.substr(1);
       var classesMenu=document.getElementsByClassName("nav-link");
       var _this = this;
       for(var i=0;i<classesMenu.length;i++){
        classesMenu[i].addEventListener("click", function(){
              var classesMenu2 = document.getElementsByClassName("nav-link");
               for(var j=0;j<classesMenu2.length;j++)
                classesMenu2[j].classList.remove("active");

              this.classList.add("active");

              if(_this.state.mobileMenuOpen && !this.classList.contains("parentItem")){
                 document.getElementById("navbarCollapse").style.display= "none" ;
                _this.setState({mobileMenuOpen: false});
              }

         }, false);
       }

   }


  pushDbUrl =(e)=>{
   e.preventDefault();
    const lastUrl = localStorage.getItem("lastClickedUrl");
    if(lastUrl != null)
    this.setState({lastUrl: lastUrl});
 }

 showSubmenu =(e, id="")=>{
   e.preventDefault();
   document.getElementById(id).style.display= document.getElementById(id).style.display=="block" ? "none":"block";
 }

 hideMenu =(id="")=>{
   document.getElementById(id).style.display=  "none" ;
 }

 coldClick=(e)=>{
   e.preventDefault();
 }

 showMenu=(e)=>{
   e.preventDefault();

   var menu = document.getElementById("navbarCollapse");
   menu.style.display=menu.style.display=="block" ? "none":"block";
   this.setState({mobileMenuOpen:this.state.mobileMenuOpen ? false:true});
 }


  render() {
    return (
    <>
       <header class="positionRelative">
         <nav class="navbar navbar-expand-md navbar-dark navBarTop">
           <div class="container-fluid">
             <a class="navbar-brand" href={window.BASE_URL+"#/"}>SimpleSSH</a>
             <button class="navbar-toggler" onClick={this.showMenu} type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
               <span class="navbar-toggler-icon"></span>
             </button>
             <div class="collapse navbar-collapse topMenu" id="navbarCollapse">
               <ul class="navbar-nav me-auto mb-2 mb-md-0 topMenuNav">

                  <li class="nav-item dropdown hoverShowSubmenu">
                    <a class="nav-link parentItem" href="#" onClick={this.coldClick}  id="navbarDropdown">
                     <span> <i class="bi bi-gear-wide-connected"></i></span> Settings
                    </a>
                    <ul class="dropdown-menu" id="settingsConnections" aria-labelledby="navbarDropdown">
                      <li>
                        <a class="nav-link" href={window.BASE_URL+"#/settings"} >
                            <i class="bi bi-gear-wide-connected"></i> SSH connection
                        </a>
                      </li>
                      <li>
                         <a class="nav-link" href={window.BASE_URL+"#/system-users"} >
                          <i class="bi bi-person-badge"></i> App Users
                         </a>
                      </li>
                        <li>
                         <a class="nav-link" href={window.BASE_URL+"#/system-logs"} >
                          <i class="bi bi-info-lg"></i> Logs
                         </a>
                      </li>
                     </ul>
                  </li>


                  <li class="nav-item">
                   <a class="nav-link" href={window.BASE_URL+"#/installations"}>
                     <span><i class="bi bi-mouse2"></i></span> Installations
                    </a>
                 </li>
                  <li class="nav-item">
                    <a class="nav-link" href={window.BASE_URL+"#/users-ftp"}>
                      <span><i class="bi bi-person"></i></span>  Users/Ftp
                    </a>
                  </li>
                  <li class="nav-item dropdown hoverShowSubmenu">
                     <a class="nav-link" href={window.BASE_URL+"#/domains"} id="navbarDropdown2" onClick={this.coldClick}>
                      <span><i class="bi bi-globe"></i></span>  Domains
                     </a>
                     <ul class="dropdown-menu" id="usersDatabase" aria-labelledby="navbarDropdown2">
                         <li>
                            <a class="nav-link" href={window.BASE_URL+"#/domains"} >
                              <i class="bi bi-hdd-stack"></i> Domains
                            </a>
                         </li>
                         <li>
                           <a class="nav-link" href={window.BASE_URL+"#/domains/dns/n"} >
                             <i class="bi bi-person"></i> DNS Editor
                           </a>
                         </li>
                     </ul>
                  </li>
                  <li class="nav-item">
                   <a class="nav-link" href={window.BASE_URL+"#/fil-manager"}>
                     <span><i class="bi bi-folder"></i></span>  FileManager
                   </a>
                  </li>
                     <li class="nav-item dropdown hoverShowSubmenu">
                      <a class="nav-link parentItem" href="#" id="navbarDropdown" onClick={this.coldClick}>
                       <span><i class="bi bi-hdd-stack"></i></span>  Database
                      </a>
                      <ul class="dropdown-menu" id="usersDatabase" aria-labelledby="navbarDropdown">
                        <li>
                           <a class="nav-link" href={window.BASE_URL+"#/database-mysql"} >
                             <i class="bi bi-hdd-stack"></i> Database
                           </a>
                        </li>
                        <li>
                          <a class="nav-link" href={window.BASE_URL+"#/database-mysql-users"} >
                            <i class="bi bi-person"></i> Database Users
                          </a>
                        </li>
                        <li>
                          <a class="nav-link" href={window.BASE_URL+this.state.lastUrl} >
                           <i class="bi-link"></i> Last click
                          </a>
                        </li>
                       </ul>
                    </li>
                    <li class="nav-item">
                       <a class="nav-link" href={window.BASE_URL+"#/firewall"}>
                         <span><i class="bi bi-bricks"></i></span> Firewall
                       </a>
                   </li>
                   <li class="nav-item">
                      <a class="nav-link" href={window.BASE_URL+"#/services"}>
                        <span><i class="bi bi-plugin"></i></span> Services
                      </a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href={window.BASE_URL+"#/groups"}>
                      <span><i class="bi bi-shield-lock"></i></span> Groups
                    </a>
                  </li>

                    <li class="nav-item">
                        <a class="nav-link" href={window.BASE_URL+"#/emails"}>
                         <span>@</span> Email
                        </a>
                    </li>
                     <li class="nav-item">
                        <a class="nav-link" href="#" onClick={logout}>
                          <span><i class="bi bi-plug"></i></span> Exit
                        </a>
                    </li>

               </ul>

             </div>
           </div>
         </nav>
       </header>
          <a href="#" onClick={this.pushDbUrl} id="trigerUpdateUrlToLastUrl" style={{display:"none"}}></a>
      </>
    );
  }
}

export default Header;