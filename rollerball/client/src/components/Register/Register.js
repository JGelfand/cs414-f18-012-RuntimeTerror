import React, {Component} from 'react';



export default class App extends Component {
    constructor(props) {
        super(props);

        this.login = this.login.bind(this);

    }

    render(){

        return(
            <div>
                <br></br>
                <p><h1>User Registration</h1></p>
                <p>Email: <input></input></p>
                <p>Username: <input></input></p>
                <p>Password: <input></input></p>
                <p>Confirm Password: <input></input></p>
                <button>Register</button>
                <button onClick={this.login}>Login</button>
            </div>
        );
    }

    login(){
        this.props.setPage('login');
    }


}