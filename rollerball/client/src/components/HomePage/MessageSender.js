import React, {Component} from 'react';
import {DropdownToggle, DropdownMenu, DropdownItem, Dropdown, Form, Input, FormGroup, Label, Button } from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";

export default class MessageSender extends Component{
    constructor(props) {
        super(props);
        this.state=this.emptyState();

        this.toggleDropDown = this.toggleDropDown.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
    }

    toggleDropDown(){
        this.setState({dropDownOpen:!this.state.dropDownOpen});
    }

    sendMessage(){
        let body=Object.assign({}, this.state);
        body.token = this.props.token;
        delete body['sendButtonDisabled'];
        delete body['dropDownOpen'];
        this.setState({sendButtonDisabled: true});
        sendServerRequestWithBody("message", body, this.props.serverPort).then((response)=>{
            if(response.body.success){
                this.setState(this.emptyState());
            }
        })
    }

    render() {
        return(
            <Form>
                <FormGroup row>
                <Label sm={3}>Send</Label>
                <Dropdown sm={9} isOpen={this.state.dropDownOpen} toggle={this.toggleDropDown}>
                    <DropdownToggle caret>
                        {this.state.type}
                    </DropdownToggle>
                    <DropdownMenu>
                        <DropdownItem onClick={()=>this.setState({type:"message"})}>message</DropdownItem>
                        <DropdownItem onClick={()=>this.setState({type:"invite"})}>invite</DropdownItem>
                    </DropdownMenu>
                </Dropdown>
                </FormGroup>
                <FormGroup row>
                <Label sm={2}>to</Label>
                <Input sm={10} type={"text"}
                      onChange={event => this.setState({recipient: event.target.value})}
                      value={this.state.recipient}/>
                </FormGroup>
                <FormGroup row>
                <Label>message</Label>
                <Input type={"text"}
                       onChange={event => this.setState({message: event.target.value})}
                       value={this.state.message}/>
                </FormGroup>
                <Button disabled={this.state.sendButtonDisabled} onClick={this.sendMessage}>Send</Button>
            </Form>
        );
    }

    emptyState(){
        return {
            "recipient": "",
            "type": "message",
            "message": "",
            dropDownOpen: false,
            sendButtonDisabled: false
        }
    }

}