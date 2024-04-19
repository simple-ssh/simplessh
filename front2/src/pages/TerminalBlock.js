import React from 'react'
import TerminalBody from './TerminalBody';

class TerminalBlock extends React.Component {

  constructor(props) {
      super(props);
      const { idTerminal} = this.props;

      // alert(idTerminal);

      const positions = JSON.parse(sessionStorage.getItem('terminalsPositions')) || [];
      this.state = {
        isDragging: false,
        //position: { x: 0, y: Math.floor(Math.random() * (80 - 15 + 1)) + 15 },
        position: positions[idTerminal] || { x: 0, y: Math.floor(Math.random() * (80 - 15 + 1)) + 15 },
        startPosition: { x: 0, y: Math.floor(Math.random() * (80 - 15 + 1)) + 15 },
        mode:"small",
        minimize:false,
      };
    }

    handleMouseDown = (event) => {
    const className = event.target.className;
        if(className=="terminal-header"){
              this.setState({
                isDragging: true,
                startPosition: {
                  x: event.clientX - this.state.position.x,
                  y: event.clientY - this.state.position.y
                }
              });
          }
    };

    handleMouseMove = (event) => {
      if (this.state.isDragging) {
        this.setState({
          position: {
            x: event.clientX - this.state.startPosition.x,
            y: event.clientY - this.state.startPosition.y
          }
        });
      }
    };

    handleMouseUp = () => {
      this.setState({ isDragging: false });
    };

    handleMode = (e, type='small') => {
       e.preventDefault();
       this.setState({ mode: this.state.mode=="full" ? "small":"full" });
    };

    handleMinimize = (e) => {
       e.preventDefault();
      this.setState({minimize: this.state.minimize ? false: true });
    };

    handleClose = (e) => {
        e.preventDefault();
        const { idTerminal, onRemove } = this.props;
        onRemove(idTerminal);

        // Remove terminal position from sessionStorage
        const positions = JSON.parse(sessionStorage.getItem('terminalsPositions')) || {};
        delete positions[idTerminal];
        sessionStorage.setItem('terminalsPositions', JSON.stringify(positions));
    };


    componentDidMount() {
        window.addEventListener('beforeunload', this.saveTerminalsPositions);
    }

    componentWillUnmount() {
        window.removeEventListener('beforeunload', this.saveTerminalsPositions);
    }

    saveTerminalsPositions = () => {
        const { idTerminal } = this.props;
        const positions = JSON.parse(sessionStorage.getItem('terminalsPositions')) || {};
        positions[idTerminal] = this.state.position;
        sessionStorage.setItem('terminalsPositions', JSON.stringify(positions));
    };

    render() {
      const { position, idTerminal } = this.state;

      return (
      <>
       <div class={"terminal-window"+(this.state.mode=="full"? " fullTerminal":"")}
                        style={{ display:(this.state.minimize ? "none":"inline"), left: (position.x<0 ? 0 : position.x), top: (position.y<0? 0 : position.y) }}
                        onMouseDown={this.handleMouseDown}
                        onMouseMove={this.handleMouseMove}
                        onMouseUp={this.handleMouseUp} >
           <div class="terminal-header">
             <a href="#" class="terminalClose" title="Close" onClick={this.handleClose}></a>
             <a href="#" class="terminalMinimize" title="Minimize" onClick={this.handleMinimize}></a>
             <a href="#" class="terminalZoom" title="Zoom" onClick={e=>this.handleMode(e,"full")}></a>
           </div>
          <TerminalBody/>

       </div>

        <a class="terminalMinimizeBtn" href="#" style={{ display:(this.state.minimize ? "block":"none") }} onClick={this.handleMinimize}><i class="bi bi-terminal"></i></a>
       </>
      );
    }
}

export default TerminalBlock;