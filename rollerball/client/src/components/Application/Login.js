import React, { Component } from 'react'
import { Container, Row, Col, Pane } from 'reactstrap'
import { Button } from 'reactstrap'
import { Form, Input } from 'reactstrap'


export default class Login extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Row>
                {this.createForm('origin')}
            </Row>
        )
    }

    createForm(stateVar) {
        return (
            <Col xs={12} sm={6} md={4} lg={3}>
                <Pane header={stateVar.charAt(0).toUpperCase() + stateVar.slice(1)}
                      bodyJSX={
                          <Form >
                              {this.createInputField(stateVar, 'latitude')}
                          </Form>
                      }
                />
            </Col>
        );
    }

    createInputField(stateVar) {

        return (
            <Input name={stateVar + "Input"} placeholder="Latitude/Longitude"
                   id={`${stateVar}Coordinates`}
                   style={{width: "100%"}} />
        );

    }
}