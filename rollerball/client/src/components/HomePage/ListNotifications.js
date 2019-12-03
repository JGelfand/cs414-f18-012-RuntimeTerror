import React, {Component} from 'react';
import {Button, Container, Form, Input, Row, Col, Table} from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";



export default class ListNotifications extends Component {
    constructor(props) {
        super(props);

        this.displayEachNotification = this.displayEachNotification.bind(this);
        this.sendInviteResponse = this.sendInviteResponse.bind(this);

        this.state = {};
        this.getNotifications();

    }


    render() {

        return(
            <div>
                {this.displayEachNotification()}
            </div>
        );
    }

    displayEachNotification(){
        if(this.state.ListNotifications) return(
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
                {this.state.ListNotifications.map(currNotification =>
                    <tr style={{"font-weight":currNotification.unread?"bold":"normal"}}>
                        <td>{currNotification.message}</td>
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
        return null;
    }


    getTypeButton(notification){
        let currType=notification.type;
        if(currType === "invite"){
            return (<Col><Button onClick={()=>this.sendInviteResponse(notification.id, true)}>Accept</Button><Button onClick={()=>this.sendInviteResponse(notification.id, false)}>Decline</Button></Col>);
        }if(currType === "message" || currType === "alert"){
            return (<Col><Button onClick={() => this.markAsRead(notification.id, !notification.unread)}>{notification.unread?"Mark As Read":"Delete"}</Button></Col>);
        }
    }

    sendInviteResponse(id, accept){
        sendServerRequestWithBody("inviteAnswer", {token:this.props.token, accept:accept, inviteId:id}, this.props.serverPort).then(
            (response =>{
                if(response.statusCode == 200 && response.body) {
                   this.props.setAppState("matchID", response.body.id);
                    this.props.setAppPage("matchPage");
                }
                else{
                    this.getNotifications();
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

    markAsRead(id, deleteMessage){
        sendServerRequestWithBody("markRead", {token:this.props.token, id: id, delete:deleteMessage}, this.props.serverPort).then(
            (response =>{
                if(response.statusCode == 200 && response.body) {
                    this.getNotifications();
                }
            })
        );
    }

    getNotifications(){
        const body = {
            token: this.props.token,
        };
        sendServerRequestWithBody("notifications", body, this.props.serverPort).then(
            (response =>{
                if(response.statusCode == 200 && response.body) {
                    this.setState({ListNotifications: response.body});
                }
            })
        )
    }

}