import React, {Component} from 'react';
import {Button, Container, Form, Input, ListGroup, ListGroupItem, Row, Col} from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";

export default class MatchPage extends Component{
    constructor(props){
        super(props);

        this.getBoard = this.getBoard.bind(this);

        this.getBoard();

        this.state={
            matchInfo: null

        }

    }

    render(){
        return [
        <Row>
            {JSON.stringify(this.state.matchInfo)}
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

    getBoard(){

        const body ={
            matchID: this.props.matchID,
            token: this.props.token
        };

        sendServerRequestWithBody("matches" , body, this.props.serverPort).then(response =>{
                if(response.body === null)
                    console.log("No match found");
                else
                    this.setState({matchInfo: response.body});

        })

    }
}