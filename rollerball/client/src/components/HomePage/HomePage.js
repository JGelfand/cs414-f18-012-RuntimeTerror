import React, {Component} from 'react';
import MessageSender from "./MessageSender";
import {Button, Container, Form, Input, ListGroup, ListGroupItem, Row, Col} from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";
import ListNotifications from "./ListNotifications";



export default class HomePage extends Component {
    constructor(props) {
        super(props);

        this.getNotifications = this.getNotifications.bind(this);


        for(let key in props){
            console.log("Prop key: "+key+". Value: "+props[key]);
        }
        this.state={
            allNotifications: {},
            allInvites: {},
            showingNotifications: false
        }

    }

    render(){
        return(
            <Container>
                <Row>
                    <h1>RollerBall HomePage</h1>
                </Row>
                <Row>
                    <Col>
                    <MessageSender token={this.props.token} serverPort={this.props.serverPort}/>
                    </Col>
                    <Col>
                        <Button onClick={() => this.props.setAppPage('login')}>Logout</Button>
                    </Col>
                </Row>
                <Row>
                    <Button onClick={this.getNotifications}>View Notifications</Button>
                </Row>
                {this.renderNotifications()}
            </Container>
        );
    }


    getNotifications(){
        if(!this.state.showingNotifications) {
            const body = {
                token: this.props.token
            };

            sendServerRequestWithBody("notifications", body, this.props.serverPort).then(
                (response) => {
                    if (!response.body.message) {
                        this.state.allNotifications = response.body;
                        this.state.showingNotifications = true;
                        this.setState(this.state);
                    } else {
                        console.log("Did not work");
                    }
                }
            );
        }
        else
            this.setState({showingNotifications:false});
    }

    renderNotifications(){
        if(this.state.showingNotifications)
            return <ListNotifications ListNotifications={this.state.allNotifications} setAppPage={this.props.setAppPage}
                                      serverPort={this.props.serverPort} token={this.props.token} setAppState={this.props.setAppState}
            />;
        return null;
    }

}