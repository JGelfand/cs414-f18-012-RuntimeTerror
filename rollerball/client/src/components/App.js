import React, {Component} from 'react';
import Application from './Application/Application'

import 'bootstrap/dist/css/bootstrap.css';

export default class App extends Component {
  constructor (props){
    super(props);

    this.pages = [
      { title: 'Login', page: 'login'}

    ];

    this.state = {
      current_page: this.pages[0].page
    };

    this.setAppPage = this.setAppPage.bind(this);
  }


  render() {
    switch(this.state["current_page"]){
        case "register":
            return(
                <div>

                </div>
            );
        case "login":
            return(
                <div className="csu-branding">
                    <Application page={this.state.current_page}/>
                </div>
            );

    }
  }


  setAppPage (page) {
    this.setState({current_page: page})
  }

}