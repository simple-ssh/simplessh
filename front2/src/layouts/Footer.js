import React from 'react'
import TerminalBlock from '../pages/TerminalBlock';

class Footer extends React.Component {

 constructor(props) {
       super(props);
       this.state = { terminalCount:0, terminals:JSON.parse(sessionStorage.getItem('terminals')) || []}
  }

 componentDidMount(){
 }

 closeAlert=(e)=>{
    e.preventDefault();
     try{
       document.getElementsByTagName('body')[0].style.overflowY = "visible"  ;
       document.getElementById("mini-modal-alert").style.display = "none";
       document.getElementById("load_content_mini").innerHTML = "";

     }catch(err){}
 }

renderTerminal = () => {
  const { terminalCount, terminals } = this.state;

  const handleRemoveTerminal = (idTerminal) => {
    this.setState(prevState => ({
          terminals: prevState.terminals.filter(terminal => terminal.idTerminal !== idTerminal)
        }), () => {
          sessionStorage.setItem('terminals', JSON.stringify(this.state.terminals));
        });
  };

  return terminals.map(terminal => (
    <TerminalBlock
      key={terminal.idTerminal}
      idTerminal={terminal.idTerminal}
      onRemove={() => handleRemoveTerminal(terminal.idTerminal)}
    />
  ));
}


  openNewTerminal = (e) => {
    e.preventDefault();
    const nextId = this.state.terminals.length;
    const newTerminal = { idTerminal: nextId };
    this.setState(prevState => ({
      terminals: [...prevState.terminals, newTerminal]
    }), () => {
      sessionStorage.setItem('terminals', JSON.stringify(this.state.terminals));
    });
  };



  render() {
    return (
     <>
       {this.renderTerminal()}
       <a class="terminalOpen" href="#" onClick={this.openNewTerminal}><i class="bi bi-terminal"></i></a>

       <footer class="footer mt-auto py-3 bg-light">
         <div class="container">
           <p>@
            <a href="https://simplessh.com/">Simplessh.com</a>&nbsp;|&nbsp;
            <a href="https://simplessh.com/pages/privacy-policy.jsp" target="_blank">Privacy</a>&nbsp;|&nbsp;
            <a href="https://simplessh.com/pages/terms-conditions.jsp" target="_blank">Terms & Conditions</a>&nbsp;|&nbsp;
            <a href="https://www.paypal.com/donate/?hosted_button_id=YK3VKUXF89UB4" target="_blank">Support this project by donate</a>&nbsp;
           </p>
         </div>

          <div class="reveal-modal-bg modal-window" id="mini-modal-alert" >
            <div class="reveal-modal"  >
                <div class="modal-body modal-body-sub" style={{position: "relative"}}>
                  <button type="button" class="close_alert close_modalinner" onClick={this.closeAlert} data-modal="#mini-modal" aria-hidden="true"> &times;</button>
                   <div id="load_content_mini"> </div>
                </div>
             </div>
          </div>
          <a href="#" id="closeAlert"> </a>
       </footer>
     </>
    );
  }
}

export default Footer;