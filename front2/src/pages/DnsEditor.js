import React from 'react'
import ReactDOM from 'react-dom'
import {Link, useParams} from 'react-router-dom';
import { headers, hideLoad, handleError,  showLoad, showAlert } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';


class DnsEditor extends React.Component {
  initRecords  = {NS:[], A:[], AAAA:[], MX:[], CNAME:[], TXT:[], SRV:[], PTR:[]};
  constructor(props) {
       super(props);
       this.state = { activeRec: "A",
                      records:   this.initRecords,
                      soaRec:    [],
                    }
  }

// initial record file how will look
initialFileContent = `
$TTL 86400
@	IN	SOA	ns1replace.	admin.domainreplace. (
			datereplace01
			3600
			1800
			604800
			86400 )

@	14400	IN	NS	ns1replace.
@	14400	IN	NS	ns2replace.
@	14400	IN	A	ipreplace
ns1	14400	IN	A	ipreplace
ns2	14400	IN	A	ipreplace
*	14400	IN	A	ipreplace
@	14400	IN	MX  10	mail.domainreplace.
mail	14400	IN	A	ipreplace
@	14400	IN  TXT	"v=spf1 ip4:ipreplace -all"
_dmarc	14400	IN	TXT	"v=DMARC1; p=none; rua=mailto:dmarc@domainreplace; ruf=mailto:dmarc@domainreplace; fo=1"
    `;



 recAddName= {l4:{Name:"21", Class:"IN",Type:"REPLACE",Value:""},
              l5:{Name:"@",TTL:"14400",Class:"IN",Type:"REPLACE",Value:""},
              l6:{Name:"@",TTL:"14400",Class:"IN",Type:"REPLACE",Priority:"10",Value:""},
              lSRV:{Name:"",TTL:"10800",Class:"IN",Type:"REPLACE",Priority:"20",Weight:"100",Port:"5060",Value:""}
             };

componentDidUpdate(){}
  componentDidMount(){
    if(this.props.params.domain=="n"){
       let domain = prompt("Please enter Domain name", "");
       if (domain != null) {
         window.location.href =window.BASE_URL+"#/domains/dns/"+domain;
         window.location.reload();
       }
    }else{
      this.getData();
    }
  }

// get content of /etc/bind/domain.db
 getData =()=>{
      showLoad();
         //let data = sessionStorage.getItem("dnsrec_list");
         //this.setupData(data);
      axios.get(window.API_URL+'get-file-content?pathFile=/etc/bind/'+this.props.params.domain+".db",  headers() )
              .then(res => {
                   hideLoad();
                   this.setupData(res.data);
                   sessionStorage.setItem("dnsrec_list", res.data);
                }).catch(error => {
                   handleError(error);
                   hideLoad();
               });
  }
 // helper parse records
  setupData =(data="")=>{
    if(data==null || data=="")
     return null;

     let lines = data.split(/\r?\n|\r|\n/g);
     let soaStart =false;

     let record = this.initRecords;

     Object.keys(this.state.records).forEach(key => {
         this.state.records[key] = [];
     });

      this.setState({records: {}});
      this.state.soaRec=[];

     for (let i = 1; i < lines.length; i++) {
           let splitLine = lines[i].split(/\s+|\t/);
           if(splitLine.includes("SOA") || soaStart ){ // NS Records
               soaStart = true;
                this.state.soaRec.push(splitLine.filter((a) => a));
              if(splitLine.includes(")"))
                soaStart =false;
           }else if(splitLine.includes("NS")){ // NS Records
              record.NS.push(splitLine);
           }else if(splitLine.includes("A")){ // A Records
              record.A.push(splitLine);
           }else if(splitLine.includes("AAAA")){ // AAAA Records
               record.AAAA.push(splitLine);
           }else if(splitLine.includes("MX")){ // MX Records
              record.MX.push(splitLine);
           }else if(splitLine.includes("CNAME")){ // CNAME Records
              record.CNAME.push(splitLine);
           }else if(splitLine.includes("PTR")){ // PTR Records
              record.PTR.push(splitLine);
          }else if(splitLine.includes("TXT")){ // TXT Records
               let index = splitLine.indexOf("TXT");
               let lastPart = splitLine.slice(index+1, splitLine.length).join(' ');
                     lastPart = lastPart.replaceAll('"','');
                     lastPart = lastPart.includes("DKIM") ? lastPart.replaceAll(' ','').replaceAll(';','; ') : lastPart;
               let newArray = splitLine.slice(0, index+1 );
               newArray.push(lastPart);
              record.TXT.push(newArray);
           }else if(splitLine.includes("SRV")){ // SRV Records
              record.SRV.push(splitLine);
           }

          setTimeout(() => this.setState({ records: record, soaRec: this.state.soaRec }), 150);

     }
  }

     // add new record
     addRecord =(e, clName, typeRec)=>{
         e.preventDefault();

         if(document.getElementById("ident_"+typeRec+"Value").value==""){
            alert("Value must not be empty!");
            return null;
        }

         const inputValues = [];
          // Get all elements with class "inputField"
          const inputFields = document.querySelectorAll('.'+clName);
         // Iterate through input fields and store values in the array
          inputFields.forEach(input => {
            inputValues.push(input.value);
          });
        this.state.records[typeRec].push(inputValues);
        this.setState({records: this.state.records });
      }


//remove record
 removeOne = (e,  typeRec, index)=>{
   e.preventDefault();
   if(!window.confirm("Are you sure you want to remove the record ?"))
   return null;

    let record = Object.assign({}, this.state.records) ;
        record[typeRec].splice(index, 1);

    Object.keys(this.state.records).forEach(key => {
              this.state.records[key] = [];
     });

    this.setState({records: {}});
    setTimeout(() => this.setState({records: record}), 50);
 }
//edit other records exclude SOA
editItem = (e, typeRec, index1, index2)=>{
      let val = e.target.value;
      this.state.records[typeRec][index1].splice(index2, 1, val);
      this.setState({records: this.state.records });
 }
 // edit soa records
 editSoa = (e, index1, index2)=>{
       let val = e.target.value;
       if(this.state.soaRec.length==0){
         this.state.soaRec.push(["","IN","SOA","","","("]);
         this.state.soaRec.push([""]);
         this.state.soaRec.push([""]);
         this.state.soaRec.push([""]);
         this.state.soaRec.push([""]);
         this.state.soaRec.push(["",")"]);
       }

       this.state.soaRec[0].splice(1, 1, "IN");
       this.state.soaRec[0].splice(2, 1, "SOA");

       this.state.soaRec[index1].splice(index2, 1, val);
       this.setState({soaRec: this.state.soaRec });
  }


 // small helper check if domain is valid
  isDomainName=(str)=>{
     // Updated regular expression for a domain format
     const domainRegex = /^[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    // Test if the string matches the domain format
     return domainRegex.test(str);
  }

// save data to server
 updateDataToServer =(e)=>{
  e.preventDefault();

  let soaRec = this.state.soaRec;
  let allRec = this.state.records;


  let data = "$TTL 14400";
  for (let i = 0; i < soaRec.length; i++) {
  // each subdomain must have point (.) character on the end so here we set it in case don't have it, like: sub.domain.tld.
     for (let j = 0; j < soaRec[i].length; j++) {
        let item = soaRec[i][j];
        if(item.trim().charAt(item.length - 1) !="." && this.isDomainName(item))
            soaRec[i].splice(j, 1, item.trim()+".") ;
     }

      data = data +'\n'+ (i>0? "			" :"") +soaRec[i].join(i==0 ? "	" :" ") ;
      if(i==0){  data = data.replace("	("," ("); }
  }


    for (let key in allRec) {
      for (let i = 0; i < allRec[key].length; i++) {
       // each subdomain must have point (.) character on the end so here we set it in case don't have it, like: sub.domain.tld.
         for (let j = 0; j < allRec[key][i].length; j++) {
            let item = allRec[key][i][j];
             if(item.trim().charAt(item.length - 1) !="." && this.isDomainName(item))
                allRec[key][i].splice(j, 1, item.trim()+".") ;

              if(allRec[key][i].includes("TXT") && !item.includes('"') && (allRec[key][i].length - 1)==j){
                   if(item.length > 150){
                      let interval = Math.round(item.length/3)+1;
                      item = item.replace(new RegExp(`.{1,${interval}}(?=(.{${interval}})+$)`, 'g'), '$&" "');
                   }
                   allRec[key][i].splice(j, 1, '"'+item.trim()+'"') ;
                 }
          }

          data = data +'\n'+ allRec[key][i].join("	");
      }
    }
    data = data +'\n';

    showLoad();
    let body = {path:"/etc/bind",
                content:data.replace(/"/g, '\\"').replace(/\$/g, '\\$'),
                fileName:this.props.params.domain+".db",
                owner:"bind",
                fullPathWithName:"/etc/bind/"+this.props.params.domain+".db",
                permission:"644",
                domain:this.props.params.domain};

   axios.put(window.API_URL+'setup-dns', body, headers()). // save-file-content
           then(res => {
              hideLoad();
               if(res.data=="ok"){
                   alert("Your records has updated successful! Please restart the bind 9 service");
               }else{
                    alert(res.data);
               }

            }). catch(error => {
               hideLoad();
              handleError(error);
              this.setState({showAjax: 'none', showAjaxAll:"none"});
           });

 }



  // this is for tab, when click on the tab move tro windows
  activeRecBtn =(e, recordType="A")=>{
    e.preventDefault();
    this.setState({activeRec: recordType });
  }
 //set the ip when click in ip box
 setIp =(e)=>{
     try{
        if(e.target.value ==""){
          let select = document.getElementsByClassName("topAccountSelect")[0];
          var text= select.options[select.selectedIndex].text;
          if(!text.includes("amaz")){
           let split = text.split("@");
           document.getElementById("ipAddressGen").value = split?.[0] || "" ;
           }
        }
      }catch (exception){}
  }

// generate new records
  generateDNS =(e )=>{
      e.preventDefault();
      if(!window.confirm("Are you sure you want to set to default records? The records will not be saved on the server until you press button: Update Changes"))
        return null;

      const form = e.currentTarget
      const body = serialize(form, {hash: true, empty: true, queryView:false});
      let content = this.initialFileContent;

      content = content.replaceAll("domainreplace",body['domain']);
      content = content.replaceAll("ipreplace",body['ip']);
      content = content.replaceAll("ns1replace",body['ns1']);
      content = content.replaceAll("ns2replace",body['ns2']);
      var todayDate = new Date().toISOString().slice(0, 10).replace(/-/g, '');
      content = content.replaceAll("datereplace",todayDate);


      this.setupData(content);
  }

   actionService =(e, name="", action="")=>{
      e.preventDefault();
      showLoad();
      axios.get(window.API_URL+'action-service?name='+name+"&actionService="+action,  headers() )
           .then(res => {
              hideLoad();
              alert(res.data=="" || res.data==null ? "Success":  res.data);
           }).catch(error => {
              handleError(error);
              hideLoad();
           });
     }

  render() {
    return (
       <>
         <form onSubmit={this.generateDNS} action="#" method="POST">
            <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
                 <div class="col-md-2">
                    <input type="text" name="domain" defaultValue={this.props.params.domain} class="form-control" required={true}
                           placeholder={"Domain:"+this.props.params.domain} />
                 </div>
                 <div class="col-md-2">
                    <input type="text" name="ip" defaultValue={""} id="ipAddressGen" class="form-control" required={true}
                           placeholder="IP address:000.000.000.000" onClick={this.setIp}/>
                 </div>
                 <div class="col-md-2">
                     <input type="text" name="ns1" defaultValue ={"ns1."+this.props.params.domain} class="form-control" required={true}
                            placeholder={"NS1: ns1."+this.props.params.domain} />
                 </div>
                 <div class="col-md-2">
                     <input type="text" name="ns2" defaultValue ={"ns2."+this.props.params.domain} class="form-control" required={true}
                            placeholder={"NS2: ns2."+this.props.params.domain}/>
                 </div>
                 <div class="col-md-2">
                     <input type="text" name="ns3"  class="form-control"
                            placeholder={"NS3: ns3."+this.props.params.domain}/>
                 </div>
                  <div class="col-md-2">
                     <input type="text" name="ns4"  class="form-control"
                            placeholder={"NS4: ns4."+this.props.params.domain}/>
                 </div>

                 <div class="col-md-12 text_align_center">
                   <button class="btn btn-primary btn_small" type ="submit" >Add New Rule</button>
                 </div>
            </div>
         </form>
           <div class="height20px"></div>

           <div class="row shadow-sm bg-body rounded paddingBottomTopForm">
             <div class="col-md-3">
               <a href="#" class="btn btn btn-success btn_small" onClick={this.updateDataToServer}>
                  Update Changes
               </a>
             </div>
             <div class="col-md-9 text_align_right">
               <a href="#" class="btn btn-secondary btn_small" onClick={e=>this.actionService(e,"bind9","status")}>Bind9 status</a> &nbsp;&nbsp;
               <a href="#" class="btn btn-info btn_small" onClick={e=>this.actionService(e,"bind9","restart")}>Restart Bind9</a>
             </div>
           </div>

           <div class="clear"></div>

 <div class="row">
 <div class="minHeightPage">
  <ul class="nav nav-tabs">
    {Object.keys(this.state.records).map(key =>
         <li class="nav-item">
            <a class={key==this.state.activeRec? "nav-link active" :"nav-link"} aria-current="page" href="#" onClick={e=>this.activeRecBtn(e, key)}> {key} Records</a>
          </li>
     )}
         <li class="nav-item">
          <a class={"SOA"==this.state.activeRec? "nav-link active" :"nav-link"} aria-current="page" href="#" onClick={e=>this.activeRecBtn(e, "SOA")}> SOA Records</a>
        </li>
  </ul>

<div class="tabBlocks" id="nav-tabContent">
      {Object.keys(this.state.records).map(key =>
        <div class={key==this.state.activeRec? "tabBlock active" :"tabBlock"}>
          <h5>{key} Records {key=="PTR"? "(Name: last octet of your server IP address, example ip: 171.122.4.220, Name will be:220)":""}</h5>

          <table class="table table-striped table-hover">
                <thead>
                       <tr>
                        {Object.entries(this.recAddName[(key=="PTR" ? "l4": (key=="MX" ? "l6":(key=="SRV"? "lSRV":"l5")))]).map(([k, item])=>
                            <td>
                             {k}:<br/>
                            <input readOnly={item==key || item=="REPLACE" || item=="IN"  ? true : false}
                                        defaultValue={ item=="REPLACE"  ? item.replace("REPLACE",key) : item}  class={"dnsEditInput recType"+key}
                                        id={"ident_"+key+k}/>
                            </td>
                          )}
                           <td class="text_align_center">
                              <a href="#" onClick={e=>this.addRecord(e, "recType"+key, key)}><i class="bi bi-plus-circle-fill"></i></a>
                           </td>
                        </tr>
                </thead>
               <tbody>
                      {this.state.records[key].map((row,index1)=>
                        <tr>
                          {row.map((item,index2)=>
                             <td> <input readOnly={item==key || item=="IN"  ? true : false}
                                         value={item} class="dnsEditInput"
                                         onChange={e=>this.editItem(e, key, index1, index2)}/> </td>
                          )}
                          <td class="text_align_center">
                              <a href="#" onClick={e=>this.removeOne(e, key, index1 )}>
                                 <i class="bi bi-x-circle-fill"></i>
                              </a>
                           </td>
                        </tr>
                      )}
            </tbody>
          </table>
          </div>
         )}

        <div class={"SOA"==this.state.activeRec? "tabBlock active" :"tabBlock"}>
          <h5>SOA Records</h5>
          <table class="table table-striped table-hover">
             <tr>
                 <td>Owner name -@</td> <td> <input value={this.state.soaRec?.[0]?.[0] || ""} onChange={e=>this.editSoa(e,0,0)} class="dnsEditInput"/> </td>
              </tr>
              <tr>
                 <td>Class</td> <td> {this.state.soaRec?.[0]?.[1] || "IN"}  </td>
              </tr>
              <tr>
                 <td>Type</td> <td>  {this.state.soaRec?.[0]?.[2] || "SOA"} </td>
              </tr>
              <tr>
                 <td>Primary NameServer</td> <td> <input value={this.state.soaRec?.[0]?.[3] || ""} onChange={e=>this.editSoa(e,0,3)} class="dnsEditInput"/> </td>
              </tr>
               <tr>
                  <td>Responsible Person/Domain <br/>(email@example.com OR root.example.com)</td>
                  <td> <input value={this.state.soaRec?.[0]?.[4] || ""} onChange={e=>this.editSoa(e,0,4)} class="dnsEditInput"/> </td>
              </tr>
              <tr>
                  <td>Serial(yyyymmddss)</td> <td> <input value={this.state.soaRec?.[1]?.[0] || ""} onChange={e=>this.editSoa(e,1,0)} class="dnsEditInput"/> </td>
              </tr>
              <tr>
                  <td>Retry</td> <td> <input value={this.state.soaRec?.[2]?.[0] || ""} onChange={e=>this.editSoa(e,2,0)} class="dnsEditInput"/> </td>
              </tr>
              <tr>
                  <td>Refresh</td> <td> <input value={this.state.soaRec?.[3]?.[0] || ""} onChange={e=>this.editSoa(e,3,0)} class="dnsEditInput"/> </td>
              </tr>
              <tr>
                  <td>Expire</td> <td> <input value={this.state.soaRec?.[4]?.[0] || ""} onChange={e=>this.editSoa(e,4,0)} class="dnsEditInput"/> </td>
              </tr>
              <tr>
                  <td>TTL</td> <td> <input value={this.state.soaRec?.[5]?.[0] || ""} onChange={e=>this.editSoa(e,5,0)} class="dnsEditInput"/> </td>
              </tr>
           </table>
         </div>
      </div>
            <div class="col-md-12 text_align_center">
                <a href="#" class="btn btn-secondary" onClick={e=>{e.preventDefault(); window.location.reload();}}>
                               Cancel Changes
                </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
               <a href="#" class="btn btn-success" onClick={this.updateDataToServer}>
                   Update Changes
               </a>
             </div>
         </div>
       </div>
      </>
    );
  }
}

export default (props) => (
                          <DnsEditor
                              {...props}
                              params={useParams()}
                          />
                      ); 