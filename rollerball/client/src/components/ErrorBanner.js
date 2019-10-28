import React, { Component } from 'react';
import { Alert } from 'reactstrap';

/*
 * Renders an error message in the form of a banner.
 */
export default class ErrorBanner extends Component {


    render() {
        return (
            <Alert style={{ backgroundColor: "#e84e20", color: "#FFFFFF"}}>
                { this.props.message }
            </Alert>
        );
    }
}
