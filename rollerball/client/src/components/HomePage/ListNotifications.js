import React, {Component} from 'react';
import {Button, Container, Form, Input, Row, Col, Table} from "reactstrap";
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
        return(
            <Table>
                <thead>
                <tr>
                    <th>Message</th>
                    <th>Date Sent</th>
                    <th>Sender</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                {this.props.ListNotifications.map(currNotification =>
                    <tr>
                        <th scope="row">{currNotification.message}</th>
                        <td>
                            {currNotification.date['date'].month}
                            {'-'}
                            {currNotification.date['date'].day}
                            {'-'}
                            {currNotification.date['date'].year}
                            {'  at  '}
                            {currNotification.date.time['hour']}
                            {':'}
                            {currNotification.date.time['minute']}
                            {':'}
                            {currNotification.date.time['second']}
                            {':'}
                            {currNotification.date.time['nano']}
                        </td>
                        <td>{this.getSenderUsername(currNotification)}</td>
                        <td>{this.getTypeButton(currNotification)}</td>
                    </tr>
                )}
                </tbody>
            </Table>
        );
    }


    getTypeButton(notification){
        let currType=notification.type;
        if(currType === "alert"){
            return (<Col><Button>Alert Button</Button></Col>);
        }if(currType === "invite"){
            return (<Col><Button onClick={()=>this.sendInviteResponse(notification.id, true)}>Accept</Button><Button onClick={()=>this.sendInviteResponse(notification.id, false)}>Decline</Button></Col>);
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