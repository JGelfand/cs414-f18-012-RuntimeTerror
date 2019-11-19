import React, {Component} from 'react';
import {Button, Container, Form, FormGroup, Label, Input, Row, Col} from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";

export default class MatchPage extends Component{
    constructor(props){
        super(props);
    }

    render(){
        return(
        <Container>
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
        </Row>,
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
        let move = {token: this.props.token, matchId: this.props.matchId};
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
}