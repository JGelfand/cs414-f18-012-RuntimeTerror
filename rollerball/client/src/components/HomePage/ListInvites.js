import React, {Component} from 'react';
import {Button, Container, Form, Input, Row, Col } from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";



export default class ListInvites extends Component {
    constructor(props) {
        super(props);

        this.displayInvites = this.displayInvites.bind(this);


        this.state = {}

    }


    render() {

        return (
            <div>
                {this.displayInvites()}
            </div>
        );
    }


    displayInvites(){

    }
}