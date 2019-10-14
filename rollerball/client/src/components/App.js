import React, {Component} from 'react';

import 'bootstrap/dist/css/bootstrap.css';
import Register from './Register/Register';
import Login from './Login/Login';
import HomePage from './HomePage/HomePage';
import {getOriginalServerPort, sendServerRequest} from '../api/restfulAPI';


export default class App extends Component {
  constructor (props){
    super(props);

    this.pages = [
        {title: 'Rollerball Registration', page: 'register'},
        {title: 'Rollerball Login', page: 'login'},
        {title: 'Rollerball Home Page', page: 'homepage'}
    ];

    this.state = {
      current_page: this.pages[0].page,
        username: '',
        password: '',
        confirmedPassword: '',
        email: '',
        serverPort: getOriginalServerPort(),
        authToken: {}
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
                <HomePage

                />
            );
    }
  }


  onChange(statevar, value){
      this.setState({[statevar]: value});
  }


  setAppPage (page) {
    this.setState({current_page: page})
  }

}

