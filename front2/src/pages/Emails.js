import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, generatePassword, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';


class Emails extends React.Component {
 sessionStorageName = 'list-of-emails';
 constructor(props) {
       super(props);
       this.state = {rows : [],
                     postfixServer:"",
                     accounts: [],
                     accountType:"",
                     showAdd: "none",
                     sslBox:"none",
                     sslType:"0",
                     serverHost:"",
                      }
 }

   componentDidMount(){
     try{
       let data = sessionStorage.getItem(this.sessionStorageName);
       if(data!=null && data !="")
       this.setState({rows: JSON.parse(data) });
     }catch(err){}
   }

   // getListOfUser
   getData =(e)=>{
     e.preventDefault();
     showLoad();
     axios.get(window.API_URL+'get-list-of-emails',  headers() )
                .then(res => {
                     if(res.data !==""){
                     this.setState({rows: res.data });
                     sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
                     }

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
     axios.put(window.API_URL+'add-new-email-account', body, headers()).
           then(res => {
               this.setState({rows: res.data });
               sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
               hideLoad();
               alert("Added!");
           }). catch(error => {
               handleError(error);
               hideLoad();
           });
   }


 addSSL=(e)=>{
  e.preventDefault();
  const form = e.currentTarget
  const body = serialize(form, {hash: true, empty: true})

   if(body['typeSSL'] =="0"){
     this.installSSL(body['domain']);
     return null;
   }

  showLoad();
  axios.put(window.API_URL+'setup-ssl-to-postfix', body, headers()).
        then(res => {
             hideLoad();
             alert(res.data.response);

             if(res.data.response.includes("is not installed"))
                this.installSSL(res.data.domain);

        }). catch(error => {
            handleError(error);
            hideLoad();
        });
}

 installSSL =(name="" )=>{
     const appName = name;
     let email = window.prompt("Enter your email, could be any of your email, to install SSL on: "+name, "");
     if (email == null || email =="") {
       if(email =="")
          alert( "The email should not be empty");
        return null;
     }

      const body = {name: name, email: email };
      showLoad();
      axios.put(window.API_URL+'install-ssl', body, headers()).
          then(res => {
             showAlert(res.data.response);
             hideLoad();
          }). catch(error => {
             handleError(error);
             hideLoad();
          });
 }


  //remove user
  removeOne = (e, email="" )=>{
   e.preventDefault();

   if(!window.confirm("Are you sure you want to remove the "+email+" ?"))
   return null;

    showLoad();
    axios.delete(window.API_URL+'remove-email?email='+email , headers() )
         .then(res => {
              hideLoad();
              this.setState({rows: res.data });
              sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));

              alert("Removed!");
          }).catch(error => {
              handleError(error);
              hideLoad();
          });
  }

 addBtn =(e, action="")=>{
  e.preventDefault();
  this.setState({sslBox: "none", showAdd:"block" });
 }

 info=(e, userName="")=>{
    e.preventDefault();
     if(userName=="")
      return null;
    let server = userName.split("@");
        server  = server.length ==2 ? server[1] :"";
    var txt =
          "<h4>IMAP info</h4>"+
          "Hostname: "+server+"<br/>"+
          "User: "+userName+"<br/>"+
          "Password: ***** (your user password)<br/>"+
          "Port: 143<br/>"+
          "Security: STARTTLS<br/>"+
          "Auth method: Normal Password<br/><br/>"+

          "<h4>SMTP info</h4>"+
          "Hostname: "+server+"<br/>"+
          "User: "+userName+"<br/>"+
          "Password: ***** (your user password)<br/>"+
          "Port: 587<br/>"+
          "Security: STARTTLS<br/>"+
          "Auth method: Normal Password<br/><br/>"+

        "<h4>This is the config for smtp for Roundcube Web mail <br/>(add this lines to the end of file: /config/config.inc.php)</h4>"+
        "<code>"+
         "$config['smtp_server'] = 'tls://"+server+"';<br/>"+
         "$config['default_host'] = 'ssl://"+server+"';<br/>"+
         "$config['default_port'] = 993;<br/>"+
         // TCP port used for IMAP connections
                  "$config['smtp_port'] = 587;<br/>"+
                  "$config['smtp_user'] = '%u';<br/>"+
                  "$config['smtp_pass'] = '%p';<br/>"+
                  "$config['smtp_auth_type'] = 'PLAIN';<br/>"+
                  "$config['smtp_conn_options'] = array(<br/>"+
                     "&nbsp;&nbsp;'ssl'         => array(<br/>"+
                     "&nbsp;&nbsp;'verify_peer'      => false,<br/>"+
                     "&nbsp;&nbsp;'verify_peer_name' => false,<br/>"+
                  "&nbsp;),<br/>"+
                ");</code><br/>";

    showAlert(txt);
 }

infoTxtRecord=(e, dom="")=>{
    e.preventDefault();
    let domainSplit = dom.split("@");
    let domain = domainSplit[1];
    let preIP = "";

    try{
        let select = document.getElementsByClassName("topAccountSelect")[0];
        var text= select.options[select.selectedIndex].text;
        if(!text.includes("amaz")){
           let split = text.split("@");
           preIP = split?.[0] || "" ;
        }
     }catch(err){}

    let ip4Server = window.prompt("Enter the IP v4 for your server", preIP);
    if (ip4Server == null ) return null;
    if (ip4Server == "" ) { alert("The IP should be valid and not empty"); return null;}

    var txt = " <h3>TXT Records </h3>" +
               "<p>For each domain of your emails, you need to add this TXT records</p>" +
               "                   <table class=\"table table-striped table-hover\">" +
               "                               <thead>" +
               "                                 <tr>" +
               "                                   <th scope=\"col\">Name</th>" +
               "                                   <th scope=\"col\">TTL</th>" +
               "                                   <th scope=\"col\">Type</th>" +
               "                                   <th scope=\"col\">Value</th>" +
               "                                 </tr>" +
               "                               </thead>" +
               "                               <tbody>" +
               "                                 <tr>" +
               "                                     <td scope=\"col\">@</td>" +
               "                                     <td scope=\"col\">14400</td>" +
               "                                     <td scope=\"col\">TXT</td>" +
               "                                     <td scope=\"col\">v=spf1 a mx ip4:"+ip4Server+" ~all</td>" +
               "                                 </tr>" +
               "                                 <tr>" +
               "                                     <td scope=\"col\">_dmarc</td>" +
               "                                     <td scope=\"col\">14400</td>" +
               "                                     <td scope=\"col\">TXT</td>" +
               "                                     <td scope=\"col\">v=DMARC1; p=quarantine; rua=mailto:dmarc@"+domain+"; ruf=mailto:dmarc@"+domain+"</td>" +
               "                                 </tr>" +
               "                                 <tr>" +
               "                                     <td scope=\"col\">mail._domainkey</td>" +
               "                                     <td scope=\"col\">14400</td>" +
               "                                     <td scope=\"col\">TXT</td>" +
               "                                     <td scope=\"col\" style=\"word-wrap: break-word;overflow-wrap: break-word;word-break: break-all;\">v=DKIM1; k=rsa; t=y; p=dkimKey</td>" +
               "                                 </tr>" +
               "                           </tbody>" +
               "                       </table>";


              showLoad();
               axios.get(window.API_URL+'get-dkim-info',  headers() )
                    .then(res => {

                         let info = res.data;

                         if(info.response=="ok"){
                           showAlert(txt.replace("dkimKey", info.key));
                         }else{
                           alert(info.response);}

                      hideLoad();
                      }).catch(error => {
                         handleError(error);
                         hideLoad();
                     });

 }

testDNS=(e, dom="")=>{
    e.preventDefault();
    let domainSplit = dom.split("@");
    let domain = domainSplit[1];
    let preIP = "";
    try{
        let select = document.getElementsByClassName("topAccountSelect")[0];
        var text= select.options[select.selectedIndex].text;
        if(!text.includes("amaz")){
           let split = text.split("@");
           preIP = split?.[0] || "" ;
        }
     }catch(err){}

    let ip4Server = window.prompt("Enter the IP v4 for your server", preIP);
    if (ip4Server == null ) return null;
    if (ip4Server == "" ) { alert("The IP should be valid and not empty"); return null;}

    var txt = " <h3>DNS Records </h3>" +
               "<p>Domain: "+domain+" ; IP: "+ip4Server+"</p>" +
               "                   <table class=\"table table-striped table-hover infoTable\">" +
               "                               <thead>" +
               "                                 <tr>" +
               "                                   <th scope=\"col\"></th>" +
               "                                   <th scope=\"col\">Name</th>" +
               "                                   <th scope=\"col\">Value</th>" +
               "                                 </tr>" +
               "                               </thead>" +
               "                               <tbody>";


              showLoad();
               axios.get(window.API_URL+'get-dns-info?domain='+domain+'&ip='+ip4Server,  headers() )
                    .then(res => {
                         let obj = res.data;
                         for (const key in obj) {
                          if (Object.prototype.hasOwnProperty.call(obj, key) && !key.includes("ok")) {
                            const value = obj[key];
                            console.log(`${key}: ${value}`);

                             txt = txt+"<tr>" +
                                       " <td scope=\"col\"><i class=\"bi "+(Object.prototype.hasOwnProperty.call(obj, key+"ok") ? obj[key+"ok"]:"bi-dash-circle") +"\"></i></td>" +
                                       " <td scope=\"col\" style=\"text-transform: uppercase;\">"+key+"</td>" +
                                       " <td scope=\"col\" style=\"word-wrap: break-word;overflow-wrap: break-word;word-break: break-all;\">"+value+"</td>" +
                                       "</tr>";

                          }
                         }

                        txt = txt+" </tbody>" +
                                  "</table>";
                       showAlert(txt);
                       hideLoad();
                      }).catch(error => {
                         handleError(error);
                         hideLoad();
                     });

 }
  accType=(e)=>{
   this.setState({accountType:e.target.value});
  }

  installSSLBox=(e)=>{
    e.preventDefault();
    this.setState({sslBox:this.state.sslBox=="none"?"block":"none", showAdd: "none"});

    if(this.state.sslBox=="none"){
        showLoad();
        axios.get(window.API_URL+'get-server-host',  headers() )
             .then(res => {
                 if(res.data !=="")
                     this.setState({serverHost: res.data });

                hideLoad();
           }).catch(error => {
                  handleError(error);
                  hideLoad();
           });
    }

  }

  sslType=(e)=>{
   this.setState({sslType:e.target.value});
  }


  changePassword =(e, id="" )=>{
         e.preventDefault();

          let password = window.prompt("Enter the new password bellow", "");
          if (password == null || password =="") {
            if(password =="")
               alert( "The field password should not be empty");

             return null;
         }

         const body = {accid: id, password: password };
         showLoad();

         axios.put(window.API_URL+'email-change-password', body, headers()).
               then(res => {
                   hideLoad();
                   alert(res.data);
               }). catch(error => {
                  handleError(error);
                  hideLoad();
               });
    }

changeForward =(e, id="", forwardEm="", email="", domainID="" )=>{
         e.preventDefault();

          let forwardEmail = window.prompt("Enter the forward Email", forwardEm);
          if (forwardEmail == null ) return null;

         showLoad();
         const body = {idForward: id, forward: forwardEmail, domainID: domainID, email:email  };
         axios.put(window.API_URL+'email-change-forward', body, headers()).
               then(res => {
                   hideLoad();
                    this.setState({rows: res.data });
                    sessionStorage.setItem(this.sessionStorageName, JSON.stringify(res.data));
               }). catch(error => {
                  handleError(error);
                  hideLoad();
               });
    }

   setupPostfix =(e)=>{
            e.preventDefault();

            if(!window.confirm("Are you sure you want to configure the postfix/dovecot/spf/dmarc/dkim this will rewrite your existing configurations!"))
                   return null;

            let domain = window.prompt("Enter the domain for your server", "");
            if (domain == null ) return null;
            if (domain == "" ) { alert("The domain should be valid and not empty"); return null;}

            const body = {domain: domain  };
            showLoad();

            axios.put(window.API_URL+'setup-email-server', body, headers()).
                  then(res => {
                      hideLoad();
                       alert(res.data);
                  }). catch(error => {
                     handleError(error);
                     hideLoad();
                  });
       }

    regenerateDkimKey =(e)=>{
       e.preventDefault();
       if(!window.confirm("Are you sure you want to regenerate DKIM key? After that you have to change the DNS TXT record with a new key for all the domains"))
       return null;

       showLoad();
       axios.get(window.API_URL+'regenerate-dkim-key', headers()).
             then(res => {
                  hideLoad();
                  alert(res.data);
             }). catch(error => {
                handleError(error);
                hideLoad();
             });
    }



  render() {
    return (
       <>
         <form onSubmit={this.addNewOne} action="#" method="POST" style={{display: this.state.showAdd}}>
            <div class="row shadow-sm bg-body rounded paddingBottomTopForm" >
                 <div class="col-md-2">
                   <small>Enter the email</small>
                   <input type="email" name="email" class="form-control" required={true} placeholder="email@yourdomain.com"/>
                 </div>
                 <div class="col-md-2">
                    <small>Enter the password</small>
                    <input type="text" name="password" class="form-control" required={true} placeholder="******" defaultValue={generatePassword(16)}/>
                  </div>
                 <div class="col-md-3">
                    <small>Account type</small>
                    <select name="accountType" class="form-control" onClick={this.accType} required={true} >
                       <option value="1">Just email</option>
                       <option value="2">Forward Email to</option>
                    </select>
                 </div>
                 {this.state.accountType !="" ?
                   <>
                     {this.state.accountType =="2" ?
                      <div class="col-md-2" >
                        <small>Enter forward email </small>
                        <input type="email" name="forward" class="form-control" required={true} placeholder="acount@gmail.com"/>
                      </div>
                      : <></>
                      }
                   </>:<></>}

                 <div class="col-md-2"> <br/>
                   <button class="btn btn-primary btn_small" type ="submit" >Add New Email</button>
                 </div>

           </div>
         </form>

         <form onSubmit={this.addSSL} action="#" method="POST" style={{display: this.state.sslBox}}>
             <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
                      <div class="col-md-2">
                         <small><bold>Enter Your Domain *</bold></small>
                         <input type="text" name="domain" class="form-control" required={true} defaultValue={this.state.serverHost} placeholder="your-domain.com"/>
                      </div>
                      <div class="col-md-2">
                        <small>Select SSL type</small>
                        <select name="typeSSL" class="form-control" onClick={this.sslType} required={true} >
                          <option value="0">Install Let’s Encrypt SSL to domain</option>
                          <option value="1">Add Let’s Encrypt SSL to mail server</option>
                          <option value="2">Add Other SSL to mail server</option>
                          <option value="3">Return back how it was</option>
                        </select>
                       </div>

                      {this.state.sslType =="2" ?
                        <>
                         <div class="col-md-3">
                            <small><bold>Cert path *</bold></small>
                            <input type="text" name="cert" class="form-control" required={true} placeholder="/etc/ssl/certs/ssl-cert-snakeoil.pem"/>
                          </div>
                          <div class="col-md-3">
                            <small><bold>Key path *</bold></small>
                            <input type="text" name="key" class="form-control" required={true} placeholder="/etc/ssl/private/ssl-cert-snakeoil.key"/>
                          </div>
                          <div class="col-md-3">
                            <small>CApath path</small>
                            <input type="text" name="capath" class="form-control" placeholder="/etc/ssl/certs"/>
                         </div>
                        </>
                      :<></>}


                  <div class="col-md-2">
                    <br/>
                    <button class="btn btn-primary btn_small" type ="submit" >Setup</button>
                  </div>

            </div>
          </form>



           <div class="height20px"></div>

           <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
             <div class="col-md-6">
               <a href="#" class="btn btn btn-success btn_small" onClick={this.getData}>
                 <i class="bi bi-arrow-clockwise"></i> Show Emails
               </a>&nbsp;&nbsp;

               <a href="#" class="btn btn-info btn_small" onClick={this.addBtn}>
                 <i class="bi bi-plus-circle"></i> Add new email
               </a>
             </div>
              <div class="col-md-6 text_align_right">
                   <a href="#" class="btn btn-info btn_small" onClick={this.installSSLBox}>
                      <i class="bi bi-plus-circle"></i> Install SSL
                   </a>
                   &nbsp;&nbsp;&nbsp;&nbsp;
                  <a href="#" class="btn btn-info btn_small" onClick={this.regenerateDkimKey}>
                     <i class="bi bi-arrow-repeat"></i> Regenerate DKIM Key
                  </a>
                  &nbsp;&nbsp;&nbsp;&nbsp;
                   <a href="#" class="btn btn-warning btn_small" onClick={this.setupPostfix}>
                      <i class="bi bi-sliders"></i> Configure Mail server
                   </a>

              </div>
           </div>

           <div class="clear"></div>
       <div class="row">
         <table class="table table-striped table-hover">
            <thead>
              <tr>
                <th scope="col" style={{width:"40px"}}>Info</th>
                <th scope="col" style={{width:"40px"}}>ID</th>
                <th scope="col">Email</th>
                <th scope="col">Password</th>
                <th scope="col" class="text_align_center">Account/Forward</th>
                <th scope="col" class="text_align_center">TXT Records</th>
                <th scope="col" class="text_align_center">Remove</th>
             </tr>
            </thead>
            <tbody>
            {this.state.rows.map(row=>
             <tr>
                 <td> <a href="#" onClick={e=>this.info(e,row.email)}><i class="bi bi-info-circle"></i></a></td>
                 <td> {row.id} </td>
                 <td> {row.email} </td>
                 <td>
                     ******* &nbsp;
                     <a href="#" onClick={e=>this.changePassword(e, row.id)} class="editPencil">
                        <i class="bi bi-pencil-square"></i>
                     </a>
                 </td>
                 <td class="text_align_center">
                      { row.destination != "NULL" ? row.destination: ""}
                       <a href="#" onClick={e=>this.changeForward(e, (row.idDestination != "NULL" ? row.idDestination: "") , (row.destination != "NULL" ? row.destination: ""), row.email, row.domainID)} class="editPencil">
                          <i class="bi bi-pencil-square"></i>
                       </a>
                   </td>
                   <td class="text_align_center">
                      <a href="#" class="btn btn-secondary btn_small" onClick={e=>this.infoTxtRecord(e, row.email)}>
                        <i class="bi bi-info-circle"></i> Get TXT records
                      </a>&nbsp;|&nbsp;

                       <a href="#" class="btn btn-info btn_small" onClick={e=>this.testDNS(e, row.email)}>
                              <i class="bi bi-arrow-repeat"></i> Test DNS
                       </a>
                  </td>
                  <td class="text_align_center">
                    <a href="#" onClick={e=>this.removeOne(e, row.email)}>
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
                      <i class="bi bi-arrow-clockwise"></i> Show Emails
                   </a>
                </p>
            </div>
         </div>

      </>
    );
  }
}

export default Emails;