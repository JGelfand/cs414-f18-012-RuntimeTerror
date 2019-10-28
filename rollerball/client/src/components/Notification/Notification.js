import React, {Component} from 'react';
import {Button, Container, Form, Input, Row,  ListGroup, ListGroupItem, Col } from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";
import ListNotifications from "./ListNotifications";



export default class Notification extends Component {
    constructor(props) {
        super(props);


        this.state = {

            allNotifications: {}
        }

    }



    render() {

        return (
            <Container>
                <Row>
                    <h1>Current Messages</h1>
                </Row>
                <Row>
                    <Col>
                        {this.getNotifications}
                    </Col>
                </Row>
            </Container>
        );
    }



    getNotifications(){

        const body = {
            'id': this.props.token.id,
            'notifications': this.state.allNotifications
        };

        sendServerRequestWithBody("notifications", body, this.props.serverPort).then(
            (response) => {
                if (response.body.success === true) {
                    this.displayNotifications();
                }
            }
        );


    }


    displayNotifications(){

        if(this.state.allNotifications.length > 0) {
            return (
                <ListGroup>
                    {
                        this.state.allNotifications.map(allNotifications =>
                            <ListGroupItem>
                                <ListNotifications itineraryFindListItem={allNotifications}
                                                       />
                            </ListGroupItem>
                        )
                    }
                </ListGroup>
            )
        }


    }
}