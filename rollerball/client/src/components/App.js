import React, {Component} from 'react';

import 'bootstrap/dist/css/bootstrap.css';

export default class App extends Component {
  constructor (props){
    super(props);
    this.state = {
      current_page: "register"
    };

    this.setAppPage = this.setAppPage.bind(this);
  }


  render() {
    switch(this.state["current_page"]){
        case "register":
            return(
                <div>
                  <p><h1>User Registration</h1></p>
                  <p>Email: <input></input></p>
                  <p>Username: <input></input></p>
                  <p>Password: <input></input></p>
                  <p>Confirm Password: <input></input></p>
                  <button>Register</button>
                </div>
            )
    }
  }


  setAppPage (page) {
    this.setState({current_page: page})
  }

}

