import React, {Component} from 'react';
import { Container, Row, Col, Button, Form, Input } from 'reactstrap'
import {sendServerRequestWithBody} from "../../api/restfulAPI";
import ErrorBanner from "../ErrorBanner";



export default class Login extends Component {
    constructor(props) {
        super(props);

        this.createInputField = this.createInputField.bind(this);
        this.register = this.register.bind(this);
        this.sendLoginRequest = this.sendLoginRequest.bind(this);
        this.state = {
            username: "",
            password: "",
            errorMessage: null
        };
    }

    render(){
        return(

            <Container>
                <Row>
                    <p><h1>Rollerball Login</h1></p>
                </Row>
                <Row>
                    {'Username:'} {this.createForm('username')}
                </Row>
                <Row>
                    {'Password:'}{this.createForm('password')}
                </Row>
                <Row>
                    <Button onClick={this.register}>Register Here</Button>
                    <Button onClick={this.sendLoginRequest}>Login</Button>
                </Row>
                <Row>
                    {this.state.errorMessage}
                </Row>
            </Container>
        );
    }


    register(){
        this.props.setAppPage('register');
    }

    createForm(statevar){
        return(
            <Form>
                {this.createInputField(statevar)}
            </Form>
        );
    }

    createInputField(statevar) {
        let updateStateVarOnChange = (event) => {
            this.state[statevar] = event.target.value;
            this.setState(this.state);
        };
        return (
            <Input name={statevar + ""} placeholder={""}
                   id={`${statevar}`}
                   value={this.state[statevar]}
                   onChange={updateStateVarOnChange}
                   style={{width: "100%"}}
                   type={statevar.includes("password") ? "password" : "text"}/>
        );
    }

    sendLoginRequest(){
        const body = {
            username: this.state['username'],
            password: this.state['password']
        };

        sendServerRequestWithBody("login", body, this.props.serverPort).then(
            (response) => {
                if (response.body.success === true) {
                    this.props.updateFieldChange('authToken', response.body['token']);
                    this.props.setAppPage('homepage');
                } else {
                    this.setState({errorMessage:
                            <ErrorBanner message={response.body.errorMessage}/>
                    })
                }
            }
        );
    }
}