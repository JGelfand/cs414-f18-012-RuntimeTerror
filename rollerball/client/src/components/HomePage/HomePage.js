import React, {Component} from 'react';
import {Button, Container, Form, Input, Row } from "reactstrap";



export default class HomePage extends Component {
    constructor(props) {
        super(props);

        this.setPage = this.setPage.bind(this);

    }

    render(){

        return(
            <Container>
                <Row>
                    <h1>RollerBall HomePage</h1>
                </Row>
                <Row>
                    <Button onClick={this.setPage}>View Notifications</Button>
                </Row>
            </Container>
        );

    }

    setPage(){
        this.props.setAppPage('notification');
    }

}