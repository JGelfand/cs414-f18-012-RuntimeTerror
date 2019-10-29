import React, {Component} from 'react';
import {Button, Container, Form, Input, Row, Col } from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";



export default class ListNotifications extends Component {
    constructor(props) {
        super(props);

        this.displayEachNotification = this.displayEachNotification.bind(this);



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
            let currType = this.props.ListNotifications[i].type;
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
                    {this.getTypeButton(currType)}
                </Row>);

        }
        return rows;

    }

    getTypeButton(currType){
        if(currType === "alert"){
            return (<Col><Button>Alert Button</Button></Col>);
        }
    }
}