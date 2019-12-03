import React, {Component} from 'react';
import {Button, Container, Form, FormGroup, Label, Input, Row, Col} from "reactstrap";
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
        return(
        <Container>
        {this.renderBasicInfo()}
        <div>
            {this.state.matchInfo? this.state.matchInfo["board"].split("\n").map((line, i) => {
                return <Row font-family={"monospace"}>{line}</Row>; //from here:https://www.freecodecamp.org/forum/t/newline-in-react-string-solved/68484/4
            }):null}
        </div>
        <Row>
            <Col>
                <Button onClick={()=>this.getBoard()}>Refresh</Button>
            </Col>
            <Col>
            <Button onClick={()=>this.props.setAppPage("homepage")}>Go to home</Button>
            </Col>
            <Col>
                <Button onClick={() => this.props.setAppPage("login")}>Logout</Button>
            </Col>
        </Row>
        <Row>
            {this.renderMoveForm()}
        </Row>
        <Row>
            {this.state.errorMessage}
        </Row>
        </Container>);
    }

    renderMoveForm(){
        return (
            <Form onSubmit={(event)=>this.sendMove(event)}>
                <FormGroup row>
                    <Label for="fromBox" sm={2}>From</Label>
                    <Col sm={10}>
                        <Input type="text" name="from" id="fromBox" placeholder="format: [a-z][1-7]" />
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Label for="fromBox" sm={2}>To</Label>
                    <Col sm={10}>
                        <Input type="text" name="to" id="toBox" placeholder="format: [a-z][1-7]" />
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Input type={"submit"} value={"Make Move"}/>
                </FormGroup>
            </Form>
        )
    }

    sendMove(moveFormEvent){
        moveFormEvent.preventDefault();
        let move = {token: this.props.token, matchId: this.props.matchID};
        move.to = event.target.elements.to.value;
        move.from = event.target.elements.from.value;
        sendServerRequestWithBody("move", move, this.props.serverPort).then((response)=>
        {
            if(response.body.success){
                this.setState({errorMessage:null});
                this.getBoard();
            }
            else{
                this.setState({errorMessage:response.body.message})
            }
        })
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

    renderBasicInfo(){
        if(this.state.matchInfo)
            return(
                <Row>
                    You are {this.props.token.id == this.state.matchInfo.whiteId? "White": "Black"}. It is {this.state.matchInfo.turn? "White":"Black"}'s turn.
                </Row>
            );
        else
            return null;
    }
}