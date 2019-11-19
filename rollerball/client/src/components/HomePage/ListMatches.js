import React, {Component} from 'react';
import {Table, Button, Container, Form, Input, Row, Col } from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";



export default class ListMatches extends Component {
    constructor(props) {
        super(props);

        this.displayEachMatch = this.displayEachMatch.bind(this);

        this.state = {}

    }


    render() {

        return (
            <div>
                {this.displayEachMatch()}
            </div>
        );
    }

    displayEachMatch(){
        return(
        <Table>
            <thead>
            <tr>
                <th>Match ID</th>
                <th>Opponent</th>
                <th>Options</th>
            </tr>
            </thead>
            <tbody>
            {this.props.ListMatches.map(currMatch =>
                <tr>
                <th scope="row">{currMatch.id}</th>
                <td> {currMatch['opponentUsername']}</td>
                <td><Button onClick={() => this.displayMatch(currMatch.id)}>Play!</Button></td>
                </tr>
            )}
            </tbody>
        </Table>
        );
    }

    displayMatch(matchID){

        this.props.setAppState("matchID", matchID);
        this.props.setAppPage("matchPage");

    }

}