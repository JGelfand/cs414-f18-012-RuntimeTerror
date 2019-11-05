import React, {Component} from 'react';
import {Button, Container, Form, Input, Row, Col } from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";



export default class ListNotifications extends Component {
    constructor(props) {
        super(props);

        this.displayEachNotification = this.displayEachNotification.bind(this);
        this.sendInviteResponse = this.sendInviteResponse.bind(this);


        this.state = {}

    }



    render() {

        return(
            <div>
                {this.displayEachNotification()}
            </div>
        );
    }

    displayEachNotification(){
        let rows = [];
        for(let i = 0; i < this.props.ListNotifications.length; i++){
            rows.push(
                <Row>
                    <Col>{this.props.ListNotifications[i].message}</Col>
                    <Col>
                        {this.props.ListNotifications[i].date['date'].month}
                        {'-'}
                        {this.props.ListNotifications[i].date['date'].day}
                        {'-'}
                        {this.props.ListNotifications[i].date['date'].year}
                        {'  at  '}
                        {this.props.ListNotifications[i].date.time['hour']}
                        {':'}
                        {this.props.ListNotifications[i].date.time['minute']}
                        {':'}
                        {this.props.ListNotifications[i].date.time['second']}
                        {':'}
                        {this.props.ListNotifications[i].date.time['nano']}
                    </Col>
                    {this.getSenderUsername(this.props.ListNotifications[i])}
                    {this.getTypeButton(this.props.ListNotifications[i])}
                </Row>);

        }
        return rows;

    }

    getTypeButton(notification){
        let currType=notification.type;
        if(currType === "alert"){
            return (<Col><Button>Alert Button</Button></Col>);
        }if(currType === "invite"){
            return (<Col><Button onClick={()=>this.sendInviteResponse(notification.id, true)}>Accept</Button><Button onClick={()=>this.sendInviteResponse(notification.id, true)}>Decline</Button></Col>);
        }if(currType === "message"){
            return (<Col><Button>Mark As Read</Button></Col>);
        }
    }

    sendInviteResponse(id, accept){
        sendServerRequestWithBody("inviteAnswer", {token:this.props.token, accept:accept, inviteId:id}, this.props.serverPort).then(
            (response =>{
                if(response.statusCode == 200 && response.body) {
                    this.props.setAppState("matchInfo", response.body);
                    this.props.setAppPage("matchPage");
                }
            })
        )
    }

    getSenderUsername(currNotification) {
        if(currNotification.type === "invite" || currNotification.type === "message"){
            return (<Col>{currNotification['senderUsername']}</Col>);
        }else{
            return(<Col>{"admin"}</Col>);
        }
    }
}