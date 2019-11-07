import React, {Component} from 'react';
import {Button, Container, Form, Input, ListGroup, ListGroupItem, Row, Col} from "reactstrap";
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
            <Col>
            <Button onClick={()=>this.props.setAppPage("homepage")}>Go to home</Button>
            </Col>
            <Col>
                <Button onClick={() => this.props.setAppPage("login")}>Logout</Button>
            </Col>
        </Row>];
    }
}