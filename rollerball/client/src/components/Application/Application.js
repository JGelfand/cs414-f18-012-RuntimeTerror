import React, {Component} from 'react';
import {Container} from 'reactstrap';
import Login from './Login';


export default class Application extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className='application-width'>
                {this.createApplicationPage('login')}
            </div>
        );


    }


    createApplicationPage(pageToRender) {
        switch (pageToRender) {
            case 'login':
                return <Login/>
        }
    }


}