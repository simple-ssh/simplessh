import React from 'react'
import axios from 'axios';
import { headers, hideLoad, showLoad, showAlert, setCookie } from './../Helpers.js';
import serialize from 'form-serialize';


class Login extends React.Component {
 sessionStorageName ="tokenauth";
 constructor(props) {
       super(props);
       this.state = {message:"", loading:""}
  }


    // Login
    handleLogin =(e)=>{
     e.preventDefault();
     const form = e.currentTarget
     const body = serialize(form, {hash: true, empty: true})

     this.setState({message: ""});

     showLoad();
     axios.post(window.API_BASE_URL+'/api/signin', body, headers()).
           then(res => {
               //this.setState({rows: res.data });
               //localStorage.setItem(this.sessionStorageName, res.data.accessToken);
               setCookie(this.sessionStorageName, res.data.accessToken, 1);

               hideLoad();
               window.location.reload();
           }). catch(error => {
                if (error.response) {
                  if(error.response.status===401)
                        //alert("not login");
                       this.setState({message: "The User name or password are not correct"});
                   } else {
                       this.setState({message: error});
                   }
               hideLoad();
           });
    }


render() {
    return (
          <div class="loginForm">
             <div class="form-center">
               <form onSubmit={this.handleLogin}>
                  <h3>SimpleSSH</h3>

                <div class="loginInput">
                  <p><small>User name</small>
                    <input name="username" class="form-control" placeholder="User Name" type="text" required={true} />
                  </p>
                 <p><small>Password</small>
                   <input name="password" class="form-control" placeholder="Password"  type="password"  required={true}/>
                 </p>
                 <div class="sign-up">
                      <button className="btn btn-primary btn_small" disabled={this.state.loading} >
                                  {this.state.loading && (
                                    <span className="spinner-border spinner-border-sm"></span>
                                  )}
                                  <span>Login</span>
                       </button>
                 </div>

                  {this.state.message && (
                       <div className="form-group">
                         <div className="alert alert-danger" role="alert">
                           {this.state.message}
                         </div>
                       </div>
                   )}
                  </div>

                </form>
             </div>
          </div>
     );
   }
 }

 export default Login;