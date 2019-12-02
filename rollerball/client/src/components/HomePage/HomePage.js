import React, {Component} from 'react';
import MessageSender from "./MessageSender";
import {Button, Container, Form, Input, ListGroup, ListGroupItem, Row, Col} from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";
import ListNotifications from "./ListNotifications";
import ListMatches from "./ListMatches";



export default class HomePage extends Component {
    constructor(props) {
        super(props);

        this.getNotificationsList = this.getNotificationsList.bind(this);
        this.getGamesList = this.getGamesList.bind(this);


        for(let key in props){
            console.log("Prop key: "+key+". Value: "+props[key]);
        }
        this.state={
            allNotifications: {},
            allInvites: {},
            showingNotifications: false,
            showingMatches: false,
            showingCompletedGames: false,
            allMatches: {},
            completedMatches: {}
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
                        <Button onClick={() => this.props.setAppPage('login')}>Logout</Button>
                    </Col>
                </Row>
                <Row>
                    <Col>
                    <MessageSender token={this.props.token} serverPort={this.props.serverPort}/>
                    </Col>
                </Row>
                <Row>
                    <Button onClick={() => this.getNotificationsList("notifications")}>View Notifications</Button>
                    <Button onClick={() => this.getGamesList("ViewCurrentGames")}>View Current Games</Button>
                    <Button onClick={() => this.getGamesList("CompletedGames")}>View Completed Games</Button>
                </Row>
                {this.renderNotifications()}
                {this.renderMatches()}
                {this.renderCompletedMatches()}
            </Container>
        );
    }


    getNotificationsList(table) {
        if (table === 'notifications' && this.state.showingNotifications === true) {
            this.setState({showingNotifications: false});
        } else {
            const body = {
                token: this.props.token,
            };
            this.sendRequest("notifications", body, "notifications");
        }
    }


    getGamesList(table){
        if(table === 'ViewCurrentGames' || table === 'CompletedGames') {
            if(table === 'ViewCurrentGames' && this.state.showingMatches === true) {
                this.setState({showingMatches: false});
                return;
            }else if(table === "ViewCurrentGames"){
                let body = {
                    token: this.props.token,
                    finishedGames: false,
                    userID: this.props.token.id
                };
                this.sendRequest("ViewCurrentGames", body, "ViewCurrentGames");
            }
            if(table === 'CompletedGames' && this.state.showingCompletedGames === true) {
                this.setState({showingCompletedGames: false});
            }else if(table === "CompletedGames"){
                let body = {
                    token: this.props.token,
                    finishedGames: true,
                    userID: this.props.token.id
                };
                this.sendRequest("ViewCurrentGames", body, "CompletedGames");
            }
        }
    }


    sendRequest(table, body, updateTable){
        sendServerRequestWithBody(table, body, this.props.serverPort).then(
            (response) => {
                if (!response.body.message) {
                    if(updateTable === "CompletedGames") {
                        this.state.completedMatches = response.body;
                        this.state.showingCompletedGames = true;
                        this.setState(this.state);
                    }if(updateTable === "ViewCurrentGames"){
                        this.state.allMatches = response.body;
                        this.state.showingMatches = true;
                        this.setState(this.state);
                    }if(updateTable === 'notifications'){
                        this.state.allNotifications = response.body;
                        this.state.showingNotifications = true;
                        this.setState(this.state);
                    }
                } else {
                    console.log("Did not work");
                }
            }
        );
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
                                gameType={"CurrentGames"}
            />;
        return null;
    }

    renderCompletedMatches(){
        if(this.state.showingCompletedGames)
            return <ListMatches ListMatches={this.state.completedMatches}
                                setAppState={this.props.setAppState}
                                setAppPage={this.props.setAppPage}
                                gameType={"CompletedGames"}
            />;
        return null;
    }

}