import React from 'react'
import ReactDOM from 'react-dom'
import { Link } from 'react-router-dom';
import { headers, innerHeaders, hideLoad, handleError,  showLoad, showAlert, random } from './../Helpers.js';
import axios from 'axios';
import serialize from 'form-serialize';
import PopupCodeEditor from './PopupCodeEditor';
import Stomp from 'stompjs';


class TerminalBody extends React.Component {
  sessionCommands = 'list-of-command';
  unicID = random(5);
  singleToken = random(40);
  constructor(props) {
       super(props);
       this.state = {rows : [],
                     response :  "",
                     messages: [],
                     stompClient: null,
                     request:{},
                     suggestions:[],
                     commandEntered:'',
                     showSuggestion:'none',
                     suggestionPosition:'topSuggestion',
                     suggestionNr:-1,
                     stopShowingSuggestion: false,

                    }

       this.divRef = React.createRef();
 }

 componentWillUnmount() {
     if (this.state.stompClient) {
         this.state.stompClient.disconnect();
      }
 }

 componentDidMount(){

     this.useEffect();

     // load suggestions
     try{
         let data = sessionStorage.getItem(this.sessionCommands);
       if(data!=null && data !=""){
         let objRows =JSON.parse(data);
         this.setState({suggestions: objRows });
       }
      }catch(err){}
 }

getSessionSuggestion =()=>{
    try{
       let data = sessionStorage.getItem(this.sessionCommands);
       if(data!=null && data !=""){
            return JSON.parse(data);
        }
      }catch(err){ }
        return [];
}

 useEffect=() => {
      // initiate stomp
     const stompClient = Stomp.client(window.TERMINAL_URL);
     stompClient.debug = () => {};
     // connect to socket
     stompClient.connect(innerHeaders(this.singleToken), (message) => {
       this.setState({stompClient:stompClient});
         /*
           any message came from backend will be seted here, when we send a message and receive back message will be push here
           ws.onmessage = (evt) => {
                     const message = evt.data;
                     this.setState(prevState => ({
                       messages: [...prevState.messages, message]
                     }));
                   };
        */

        const subscription =  stompClient.subscribe('/receive-data/terminal/'+this.singleToken, message => {
           this.setState(prevState => ({  messages: [...prevState.messages, message.body ] }));
           this.divRef.current.scrollTop = this.divRef.current.scrollHeight+20;
        });

         // Check if the subscription is active
        /*if (subscription && subscription.id) {
            console.log('Subscription successful. Subscription ID:', subscription.id);
        } else {
            console.error('Subscription failed');
        }*/

     }, (error) => {
       // Handle connection errors
       console.error('WebSocket connection error:', error);
     });

    }

// send command  innerHeaders()
 handleSubmit = (event) => {
    event.preventDefault();
    let text = event.target.value.trim();

    let suggestions = this.getSessionSuggestion();
    if(!suggestions.includes(text)){
       suggestions.push(event.target.value);
       const uniqueSuggestions = [...new Set(suggestions.filter(item => item.replace(/\n/g, '').trim()))];//suggestions.filter((item, index) => suggestions.indexOf(item) === index);
       sessionStorage.setItem(this.sessionCommands, JSON.stringify(uniqueSuggestions));
     }

    if(this.state.stompClient == null){
      this.setState(prevState => ({  messages: [...prevState.messages,  "The chanel is closed, refresh the page, or check the ssh connection data." ] }));
      this.divRef.current.scrollTop = this.divRef.current.scrollHeight+20;
    }

    if(text!="" && this.state.stompClient != null){
      this.state.stompClient.send('/send-data/terminal', innerHeaders(this.singleToken), JSON.stringify({'command': text}));
    }
     this.setState({commandEntered :'', stopShowingSuggestion:false});
  }

 handleChange = (event) => {
    let text = event.target.value;
    if(this.state.showSuggestion == 'none' && !this.state.stopShowingSuggestion){
     this.setState({showSuggestion:'block', suggestionPosition:(this.state.messages.length < 5 ? 'bottomSuggestion':'topSuggestion') });
    }

    let sorted = this.getSessionSuggestion().reverse().filter(item => item.includes(text));
    this.setState({commandEntered : text, suggestions: sorted, suggestionNr:-1,
                   showSuggestion: (sorted.length==0 ? 'none': (!this.state.stopShowingSuggestion ? 'block': 'none')) });
 }

  handleKeyDown = (event) => {
    //event.preventDefault();
    let suggestionsLength = this.state.suggestions.length;

    if (event.key === 'Enter' && !event.shiftKey) {

      if(this.state.suggestionNr >-1){ // here is that mean that user want to select one of the suggestion
          this.setState({ commandEntered:this.state.suggestions[this.state.suggestionNr].trim(),
                          suggestionNr:-1,
                          stopShowingSuggestion: true,
                          showSuggestion:'none' });
        }else{
         this.handleSubmit(event);
         this.setState({ showSuggestion:'none', suggestionNr:-1, stopShowingSuggestion:false});
       }


    }else if (event.key === 'ArrowUp') {
      this.setState({ suggestionNr: this.state.suggestionNr<=0 ? suggestionsLength-1 : this.state.suggestionNr-1});
    } else if (event.key === 'ArrowDown') {
       this.setState({ suggestionNr: this.state.suggestionNr >= suggestionsLength-1 ? 0 : this.state.suggestionNr+1});
     } else if (event.key === 'ArrowLeft' || event.key === 'ArrowRight') {
       this.setState({showSuggestion:'none', stopShowingSuggestion:true});
     }
  }

  pushSuggestion = (e, suggestion="") => {
       e.preventDefault();
       this.setState({commandEntered : suggestion.trim(), showSuggestion:'none', suggestionNr:-1});
       document.getElementById('terminalInputId'+this.unicID).focus();
    }

  render() {
    return (
         <div class="terminal-body" ref={this.divRef}>
               {this.state.messages.map((message, index) => (
                <p>{message}</p>
              ))}
           <div class="terminalEnter">
            <div class={"terminalSuggestions "+this.state.suggestionPosition} style={{display:this.state.showSuggestion}}>
               {this.state.suggestions.map((suggestion, index) => (
                    <a href="#" class={(index==this.state.suggestionNr ? "selectedSuggestion":"")} onClick={e=>this.pushSuggestion(e, suggestion)}>{suggestion}</a>
                ))}
            </div>
              <textarea id={"terminalInputId"+this.unicID} class="terminalInput"
                       value={this.state.commandEntered}
                       placeholder="Type you command here"
                       onChange={this.handleChange}
                       onKeyDown={this.handleKeyDown} />
           </div>
         </div>
    );
  }
}

export default TerminalBody;