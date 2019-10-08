import React, {Component} from 'react';
import {Button, Container, Form, Input, Row } from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";



export default class Register extends Component {
    constructor(props) {
        super(props);


        this.createInputField = this.createInputField.bind(this);
        this.goToLogin = this.goToLogin.bind(this);
        this.registerToJson = this.registerToJson.bind(this);
        this.sendRegisterRequest = this.sendRegisterRequest.bind(this);

        this.state={

        }

    }

    render(){

        return(

            <Container>
                <Row>
                    <p><h1>Rollerball Registration</h1></p>
                </Row>
                <Row>
                    {'Email Address: '}{this.createForm('email')}
                </Row>
                <Row>
                    {'Username:'}{this.createForm('username')}
                </Row>
                <Row>
                    {'Password:'}{this.createForm('password')}
                </Row>
                <Row>
                    {'Confirm Password:'}{this.createForm('confirmedPassword')}
                </Row>
                <Row>
                    <Button onClick={this.sendRegisterRequest}>Register!</Button>
                    <Button onClick={this.goToLogin} id="Popover1" type="button">Login</Button>
                </Row>
            </Container>
        );
    }


    goToLogin(){
        this.props.updateFieldChange('password', '');
        this.props.updateFieldChange('username', '');
        this.props.updateFieldChange('confirmedPassword', '');
        this.props.updateFieldChange('email', '');
        this.props.setAppPage('login');
    }


    registerToJson(){

        return {
            'username': this.props.username,
            'email': this.props.email,
            'password': this.props.password
        };
    }


    sendRegisterRequest(){
        const registerRequest = this.registerToJson();
        if(this.props.password !== this.props.confirmedPassword){
            return;
        }
        sendServerRequestWithBody('register', registerRequest, this.props.serverPort).then((response) => {

            });

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
            this.props.updateFieldChange(statevar, event.target.value)};

        if(statevar === 'password' || statevar === 'confirmedPassword'){
            return (
                <Input name={statevar + ""} placeholder={""}
                       id={`${statevar}`}
                       value={this.props[statevar]}
                       onChange={updateStateVarOnChange}
                       style={{width: "100%"}}
                       type={"password"}/>
            );
        }


        return (
            <Input name={statevar + ""} placeholder={""}
                   id={`${statevar}`}
                   value={this.props[statevar]}
                   onChange={updateStateVarOnChange}
                   style={{width: "100%"}} />
        );

    }


}