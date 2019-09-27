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
            <p>Some Text</p>
            )
    }
  }


  setAppPage (page) {
    this.setState({current_page: page})
  }

}

