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
                {this.displayEachNotification()}
            </div>
        );
    }

    displayEachNotification(){
        let rows = [];
        for(let i = 0; i < this.props.ListNotifications.length; i++){
            rows.push(<Row>{this.props.ListNotifications[i].message}</Row>);
        }
        console.log("Trying to display "+rows.length+" rows.");
        return rows;

    }
}