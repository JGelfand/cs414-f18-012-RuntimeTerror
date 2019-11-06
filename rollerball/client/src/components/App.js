import React, {Component} from 'react';

import 'bootstrap/dist/css/bootstrap.css';
import Register from './Register/Register';
import Login from './Login/Login';
import HomePage from './HomePage/HomePage';
import {getOriginalServerPort, sendServerRequest} from '../api/restfulAPI';
import MatchPage from "./MatchPage/MatchPage";


export default class App extends Component {
  constructor (props){
    super(props);

    this.pages = [
        {title: 'Rollerball Registration', page: 'register'},
        {title: 'Rollerball Login', page: 'login'},
        {title: 'Rollerball Home Page', page: 'homepage'},
        {title: 'Notification Page', page: 'notification'}
    ];

    this.state = {
      current_page: this.pages[0].page,
        username: '',
        password: '',
        confirmedPassword: '',
        email: '',
        serverPort: getOriginalServerPort(),
        authToken: {},
        matchInfo: null
    };

    this.setAppPage = this.setAppPage.bind(this);
    this.onChange = this.onChange.bind(this);
  }


  render() {
    switch(this.state["current_page"]){
        case "register":
            return(
                <Register
                setAppPage={this.setAppPage}
                updateFieldChange={this.onChange}
                password={this.state.password}
                confirmedPassword={this.state.confirmedPassword}
                serverPort={this.state.serverPort}
                email={this.state.email}
                username={this.state.username}
                />
            );
        case "login":
            return(
                <Login setAppPage={this.setAppPage}
                       serverPort={this.state.serverPort}
                       updateFieldChange={this.onChange}
                />
            );
        case "homepage":
            return(
                <HomePage setAppPage={this.setAppPage}
                          token={this.state.authToken}
                          setAppState={this.onChange}
                />
            );
        case "matchPage":
            return (
                <MatchPage token={this.state.authToken}
                            setAppPage={this.setAppPage}
                            matchInfo={this.state.matchInfo}/>
            )
    }
  }



  onChange(statevar, value){
      console.log("called onChange with "+statevar+", "+value);
      this.setState({[statevar]: value});
  }


  setAppPage (page) {
    this.setState({current_page: page});
  }

}

