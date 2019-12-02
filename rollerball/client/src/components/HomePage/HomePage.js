import React, {Component} from 'react';
import MessageSender from "./MessageSender";
import {Button, Container, Form, Input, ListGroup, ListGroupItem, Row, Col} from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";
import ListNotifications from "./ListNotifications";
import ListMatches from "./ListMatches";



export default class HomePage extends Component {
    constructor(props) {
        super(props);

        this.getTableList = this.getTableList.bind(this);


        for(let key in props){
            console.log("Prop key: "+key+". Value: "+props[key]);
        }
        this.state={
            allNotifications: {},
            allInvites: {},
            showingNotifications: false,
            showingMatches: false,
            allMatches: {},
            deregisterClicks: 0
        }

    }

    render(){
        return(
            <Container>
                <Row>
                    <Col>
                    <h1>RollerBall HomePage</h1>
                    </Col>
                    <Col>
                        <Button onClick={() => this.logout()}>Logout</Button>
                        <Button onClick={() => this.deregisterStep()}>{this.getDeregisterMessage()}</Button>
                    </Col>
                </Row>
                <Row>
                    <Col>
                    <MessageSender token={this.props.token} serverPort={this.props.serverPort}/>
                    </Col>
                </Row>
                <Row>
                    <Button onClick={() => this.getTableList("notifications")}>View Notifications</Button>
                    <Button onClick={() => this.getTableList("ViewCurrentGames")}>View Current Games</Button>
                </Row>
                {this.renderNotifications()}
                {this.renderMatches()}
            </Container>
        );
    }


    getTableList(table){
        if(table === 'notifications' || table === 'ViewCurrentGames') {
            if(table === 'notifications' && this.state.showingNotifications === true) {
                this.setState({showingNotifications: false});
                return;
            }
            if(table === 'ViewCurrentGames' && this.state.showingMatches === true) {
                this.setState({showingMatches: false});
                return;
            }

            const body = {
                token: this.props.token
            };
            sendServerRequestWithBody(table, body, this.props.serverPort).then(
                (response) => {
                    if (!response.body.message) {
                        if(table === "notifications") {
                            this.state.allNotifications = response.body;
                            this.state.showingNotifications = true;
                            this.setState(this.state);
                        }if(table === "ViewCurrentGames"){
                            console.log(response.body);
                            this.state.allMatches = response.body;
                            this.state.showingMatches = true;
                            this.setState(this.state);
                        }
                    } else {
                        console.log("Did not work");
                    }
                }
            );
        }
    }

    renderNotifications(){
        if(this.state.showingNotifications)
            return <ListNotifications ListNotifications={this.state.allNotifications} setAppPage={this.props.setAppPage}
                                      serverPort={this.props.serverPort} token={this.props.token} setAppState={this.props.setAppState}
            />;
        return null;
    }

    renderMatches(){
        if(this.state.showingMatches)
            return <ListMatches ListMatches={this.state.allMatches}
                                setAppState={this.props.setAppState}
                                setAppPage={this.props.setAppPage}
            />;
        return null;
    }

    logout(){
        this.props.setAppPage('login');
    }

    deregisterStep(){
        if(this.state.deregisterClicks < 5){
            this.setState({deregisterClicks: this.state.deregisterClicks+1});
        }
        else{
            sendServerRequestWithBody("deregister", {token:this.props.token}, this.props.serverPort).then(
                ()=>this.logout()
            );
        }
    }

    getDeregisterMessage(){
        let message = "Deregister";
        for(let i=this.state.deregisterClicks;i>0;i--){
            message = "Really "+message+"?";
        }
        return message;
    }
}