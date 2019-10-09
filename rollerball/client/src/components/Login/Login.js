import React, {Component} from 'react';
import { Container, Row, Col, Button, Form, Input } from 'reactstrap'



export default class Login extends Component {
    constructor(props) {
        super(props);

        this.createInputField = this.createInputField.bind(this);
        this.register = this.register.bind(this);

        this.state = {};
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
                        <Button>Login</Button>
                    </Row>
                </Container>
            );
    }


    register(){
        this.props.updateFieldChange('username', '');
        this.props.updateFieldChange('password', '');
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
            this.props.updateFieldChange(statevar, event.target.value)};

        if(statevar === 'password'){
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