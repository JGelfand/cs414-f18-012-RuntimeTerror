import React, {Component} from 'react';
import {Button, Container, Form, Input, Row } from "reactstrap";
import MessageSender from "./MessageSender";



export default class HomePage extends Component {
    constructor(props) {
        super(props);

    }

    render(){
        return(
            <Container>
                <Row>
                <h1><p>YOU HAVE LOGGED IN. NICE!</p></h1>
                </Row>
                <Row>
                <MessageSender token={this.props.token} serverPort={this.props.serverPort}/>
                </Row>
            </Container>
        );

    }

}