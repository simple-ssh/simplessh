import React, { Component } from 'react';
import ConfigVarContext from './ConfigVarContext'
import axios from "axios";
import {headers} from './../Helpers.js';

class ConfigVar extends Component {

constructor(props) {
       super(props);
       var local = localStorage.getItem('settingsData');
       try{
         local = local != null && local !=""?  JSON.parse(local) : [];
       }catch(err){ local = {};}

       this.state = {rows:local}
}

   componentDidMount(){
          /*
          this.setState({isLoading: true });
          const response = await fetch(window.API_URL+'get-settings-vars');
          const data = await response.json();
          this.setState({rows: data , isLoading: false});*/

          var isAdmin = document.getElementsByClassName("isAdminPanel");
          var langLocalStor = localStorage.getItem("lang");

          var lang = isAdmin.length > 0 ? "admin": langLocalStor !=null ? langLocalStor :"";

          axios.get(window.API_URL+'get-settings-vars?lang='+lang+'&currency=&isadmin='+(isAdmin.length > 0 ? "yes":"")  , headers())
               .then((res) => {
                    this.setState({rows: res.data , isLoading: false});
                    var response =  res.data;

                    response["defaultCurrency"] = isAdmin.length >0 ? response.currency: response.currency;
                    response["defaultLang"] = (isAdmin.length >0 ? response.mainOptions.adminlang :
                                                                   response.mainOptions.languages[0]);

                    response["defaultLangText"] = response.mainOptions.languages[0];



                    localStorage.setItem('settingsData', JSON.stringify(response));
               });


            /*
             var cssis = "";
            Object.entries(data).forEach(([key, value]) =>{
                if(key.includes("_css")){
                   cssis = cssis + value;
                }
              });

             const $style = document.createElement("style");
             document.head.appendChild($style);
               $style.innerHTML = `${cssis}`;*/
            }


         render() {
              return (
                    <ConfigVarContext.Provider value={this.state.rows}  lang="en" money="$">
                       {this.props.children}
                    </ConfigVarContext.Provider>
               );
         }
}

export default ConfigVar;




/*
function alertee(){
   const response =  fetch(window.API_URL+'header');
   const data = response.json();
   return data._header_;
}


 window.headerRespons = [];
export const getData = async () => {
        const response = await fetch(window.API_URL+'header');
                  //.then(response => response.json())
        const data = await response.json();

         window.headerRespons = await data;
    //return  data;
 }
//getData();

async componentDidMount(){
     const response = await fetch(window.API_URL+'header');
                     //.then(response => response.json())
       const data = await response.json();
     window.headerRespons = data;
}


export const _header1 =  (dataArray1) => {
   //Do something with the input
    //const data = await getData();
    return  window.headerRespons[dataArray1];
  //return theInput+"---" ;
};

export const _header2 =  (dataArray1, dataArray2) => {
   //Do something with the input
    //const data = await getData();
    return window.headerRespons[dataArray1][dataArray2] ;
  //return theInput+"---" ;
};

export const _l =  (theInput) => {
   //Do something with the input
    //const data = await getData();

    return theInput;
  //return theInput+"---" ;
};

export const justAnAlert = () => {
   alert('hello');
};
*/