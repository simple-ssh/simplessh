import React from 'react'
import ReactDOM from 'react-dom'
//import AppNavbar from './../layouts/AppNavbar';
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';

class Installations extends React.Component {
 list = [{name: "Nginx",checkStatus:"", origin:"nginx", required: true, version: "Last", description:"Web server, turn any system in Web server"},
         {name: "Zip",checkStatus:"", origin:"zip tar", required: true, version: "Last", description:"ZIP is an archive file format that supports lossless data compression"},
         {name: "Bind9",checkStatus:"", origin:"bind9 bind9utils bind9-doc dnsutils", required: false, version: "Last", description:"Allow you to control the DNS zones"},
         {name: "Mysql",checkStatus:"", origin:"mysql-server", required: true, version: "Last", description:"Mysql Database"},
         {name: "Dnsutils",checkStatus:"", origin:"dnsutils", required: false, version: "Last", description:"Collection of utilities for querying Domain Name System (DNS), like dig, nslookup, host, nsupdate, dnssec-keygen "},
         {name: "Certbot",checkStatus:"", origin:"certbot python3-certbot-nginx", required: false, version: "Last", description:"Allow you to install SSL"},
         {name: "JDK",checkStatus:"", origin:"openjdk-8-jdk", required: false, version: "", description:"Java JDK"},
         {name: "PHP",checkStatus:"", origin:"php8.3-fpm php8.3-common php8.3-mysql php8.3-xml php8.3-xmlrpc php8.3-curl php8.3-gd php8.3-imagick php8.3-cli php8.3-dev php8.3-imap php8.3-mbstring php8.3-opcache php8.3-soap php8.3-zip php8.3-intl",
          required: false, version: "8.3", description:"Programming language, please run this before install php: sudo apt-add-repository ppa:ondrej/php"},
         {name: "VSFTPD",checkStatus:"", origin:"vsftpd", required: false, version: "Last", description:"FTP server"},
         {name: "Postfix,  Dovecot, DKIM, SPF DMARC",checkStatus:"", origin:"postfix postfix-mysql dovecot-core dovecot-imapd dovecot-pop3d dovecot-lmtpd dovecot-mysql opendkim opendkim-tools postfix-policyd-spf-python opendmarc postsrsd dnsutils", required: false, version: "Last", description:"Mail server. All in one"},
         {name: "Postfix",checkStatus:"", origin:"postfix postfix-mysql", required: false, version: "Last", description:"Mail server. Only postfix"},
         {name: "Dovecot",checkStatus:"", origin:"dovecot-core dovecot-imapd dovecot-pop3d dovecot-lmtpd dovecot-mysql", required: false, version: "Last",
          description:"Mail server. Only Dovecot, after reinstall if no /etc/dovecot/conf files than run this: sudo UCF_FORCE_CONFFMISS=1 apt-get --reinstall install dovecot-core dovecot-imapd dovecot-pop3d dovecot-lmtpd dovecot-mysql"},
         {name: "DKIM, SPF DMARC",checkStatus:"", origin:"opendkim opendkim-tools postfix-policyd-spf-python opendmarc postsrsd", required: false, version: "Last", description:"Mail server, DKIM(opendkim opendkim-tools), SPF(postfix-policyd-spf-python), DMARC(opendmarc)"},
         {name: "Mailutils",checkStatus:"", origin:"mailutils", required: false, version: "Last", description:"Send mail more info here: https://mailutils.org/ "},
         {name: "fail2ban",checkStatus:"", origin:"fail2ban", required: false, version: "Last", description:"For malware attacks, more details here: https://linuxize.com/post/install-configure-fail2ban-on-ubuntu-20-04/"},
         {name: "Iptables-persistent",checkStatus:"", origin:"iptables-persistent", required: false, version: "Last", description:"Block the ip to all ports, more details:  https://snapshooter.com/blog/how-to-block-ip-accessing-your-linux-server-with-iptables-and-ufw-firewall "},
  ];
    // dovecot-core dovecot-pop3d dovecot-imapd
 constructor(props) {
       super(props);
       this.state = {apiresponse : [],
                     text: '',
                     title: ''
                    }
 }

  componentDidMount(){ }

// when click on the button install
  installApp =(e, name="", id="")=>{
       e.preventDefault();
       const appName = document.getElementById(id) ? document.getElementById(id).value : name;

       let additional = "";

       // if its post fix than we need to ask for a domain name of the mail server
       if(name.includes("postfix")){
         additional = window.prompt("Enter the main domain : domain.com or mail.domain.com", "");

         if(additional == null || additional =="") {
           if(additional =="")
             alert( "Need a domain to continue");
             return null;
         }

         if(additional.includes(":")){
                 const split = additional.split(":");
                 if(split.length <= 1){
                       alert( "Enter a valid domain");
                      return null;
                 }
                 additional = split[1];
          }
          additional = additional.replaceAll("/","").replace("www.","");
       }

       const body = {name: appName, additional:additional };
       showLoad();

       axios.put(window.API_URL+'install-app',
                   body,
                   headers()).
             then(res => {
                 hideLoad();
                 showAlert(res.data);
             }). catch(error => {
                handleError(error);
                hideLoad();
             });
  }

// when click on the button check status
 checkStatus =(e, name="", id="")=>{
     e.preventDefault();
     showLoad();
    const appName = document.getElementById(id) ? document.getElementById(id).value : name;

     axios.get(window.API_URL+'check-app-status?name='+appName,  headers() )
                .then(res => {
                   this.setState({rows: res.data });
                     hideLoad();
                     showAlert(res.data);
                 }).catch(error => {
                     handleError(error);
                     hideLoad();
                 });
  }

// when click on the button uninstall
 uninstallApp =(e, name="", id="")=>{
       e.preventDefault();
       const appName = document.getElementById(id) ? document.getElementById(id).value : name;

       if(!window.confirm("Are you sure you want to uninstall the:  "+appName+" ?"))
          return null;

       showLoad();
       axios.get(window.API_URL+'uninstall-app?name='+appName,  headers() )
              .then(res => {
                 this.setState({rows: res.data });
                   hideLoad();
                   showAlert(res.data);
               }).catch(error => {
                   handleError(error);
                   hideLoad();
               });
    }

    // when click on the button install
      runSSH =(e, id="")=>{
           e.preventDefault();
           const cmdCode = document.getElementById(id).value;

           const body = {name: cmdCode, mysql:"" };
           showLoad();

           axios.put(window.API_URL+'execute-command',
                       body,
                       headers()).
                 then(res => {
                     hideLoad();
                     var resp = res.data.response;
                         resp = resp.replace(/(?:\r\n|\r|\n)/g, '<br/>');
                         resp = resp.replace(/  /g,"&nbsp;&nbsp;");
                     showAlert(resp);
                 }). catch(error => {
                    handleError(error);
                    hideLoad();
                 });
      }

  render() {
    return (
       <div>
           <div class="row align-items-center shadow-sm bg-body rounded paddingBottomTopForm">
             <div class="col-md-10">
               <input type="text" class="form-control" id="ssh_box_cmd" placeholder="apt-get -q -y install vsftpd"/>
             </div>
             <div class="col-md-2">
               <a href="#" class="btn btn-primary btn_small"  onClick={e=>this.runSSH(e, "ssh_box_cmd" )}>Run</a>
             </div>
           </div>
           <div class="clear"></div>
          <div class="row">
          <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col">Name</th>
                <th scope="col">Required</th>
                <th scope="col">Version</th>
                <th scope="col">Status</th>
                <th scope="col">Install</th>
                <th scope="col">Uninstall</th>
                <th scope="col">Description</th>
              </tr>
            </thead>
            <tbody>
            {this.list.map(row=>
              <tr>
                <td>{row.name}</td>
                <td>{row.required ? "Yes":"Optional"}</td>
                <td>

                   {row.name == "JDK" ?
                       (<> <select id={"version_"+row.name} >
                             {[...Array(21)].map((x, i) =>
                               <option value={"openjdk-"+(i+8)+"-jdk"} >{row.name+" "+(i+8)}</option>
                             )}
                             </select>
                            </>) : (<>{row.version}</>)}
                </td>
                <td>
                  <a href="#" onClick={e=>this.checkStatus(e, (row.checkStatus !=""? row.checkStatus : row.origin), "version_"+row.name)}>Check</a>
                </td>
                <td>
                  <a href="#" onClick={e=>this.installApp(e, row.origin, "version_"+row.name)}>Install</a>
                </td>
                <td><a href="#" onClick={e=>this.uninstallApp(e, row.origin, "version_"+row.name)}>Uninstall</a></td>
                <td>{row.description}</td>
              </tr>
             )}
            </tbody>
          </table>
        </div>
       </div>
    );
  }
}

export default Installations;