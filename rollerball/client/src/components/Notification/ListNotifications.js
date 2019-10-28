import React, {Component} from 'react';
import {Button, Container, Form, Input, Row } from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";



export default class ListNotifications extends Component {
    constructor(props) {
        super(props);


        this.state = {}

    }



    render() {


        return(
            <div>
                <Row>
                    <Col xs="6" sm="3">{this.props.ListNotifications.message}</Col>
                    <Col xs="6" sm="3">{this.props.ListNotifications.date}</Col>
                    <Col xs="6" sm="3">{this.props.ListNotifications.unread}</Col>
                    <Col xs="6" sm="3"><Button>Do Something</Button></Col>

                </Row>
            </div>
        );
    }
}