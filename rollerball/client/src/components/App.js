import React, {Component} from 'react';

import 'bootstrap/dist/css/bootstrap.css';
import Register from './Register/Register';
import Login from './Login/Login';


export default class App extends Component {
  constructor (props){
    super(props);

    this.pages = [
        {title: 'Rollerball Registration', page: 'register'},
        {title: 'Rollerball Login', page: 'login'}
    ];

    this.state = {
      current_page: this.pages[0].page,
        username: '',
        password: ''
    };

    this.setAppPage = this.setAppPage.bind(this);
    this.onChange = this.onChange.bind(this);
  }


  render() {
    switch(this.state["current_page"]){
        case "register":
            return(
                <Register
                setPage={this.setAppPage}
                />
            );
        case "login":
            return(
                <Login
                updateFieldChange={this.onChange}
                username={this.state.username}
                password={this.state.password}
                setAppPage={this.setAppPage}
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

