import React, {Component} from 'react';
import {Button, Container, Form, Input, ListGroup, ListGroupItem, Row} from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";

export default class MatchPage extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return [
        <Row>
            {JSON.stringify(this.props.matchInfo)}
        </Row>,
        <Row>
            <Button onClick={()=>this.props.setAppPage("homepage")}>Go to home</Button>
        </Row>];
    }
}