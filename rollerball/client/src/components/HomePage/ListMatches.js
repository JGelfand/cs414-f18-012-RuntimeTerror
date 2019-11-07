import React, {Component} from 'react';
import {Button, Container, Form, Input, Row, Col } from "reactstrap";
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
        let rows = [];
        rows.push(
            <Row>
                <Col>
                    {"Match ID"}
                </Col>
                <Col>
                    {"Current Turn"}
                </Col>
                {"Action"}
            </Row>
        );
        for(let i = 0; i < this.props.ListMatches.length; i++){
            rows.push(
                <Row>
                    <Col>
                        {this.props.ListMatches.id}
                    </Col>
                    <Col>

                    </Col>
                </Row>

            );

        }
        return rows;


    }
}