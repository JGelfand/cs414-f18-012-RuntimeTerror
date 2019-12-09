import React, {Component} from 'react';
import {Button, Container, Form, FormGroup, Label, Input, Row, Col} from "reactstrap";
import {sendServerRequestWithBody} from "../../api/restfulAPI";
import ErrorBanner from "../ErrorBanner";

class Square extends React.Component {
	render() {
	    let squareCSS = {
	        background: '#fff',
            border: '1px solid #999',
            float: 'left',
            fontSize: '50px',
            lineHeight: '34px',
            height: '60px',
            marginRight: '-1px',
            marginTop: '-1px',
            padding: '0',
            textAlign: 'center',
            width: '60px'};
	    if(this.props.black === true)
	        squareCSS.background = '#414141';
        if(this.props.yellow === true)
            squareCSS.background = '#fff675';
		return (
			<button style={squareCSS} className="square" onClick={() => this.props.handler(this.props.pos)}>
			{this.props.value}
			</button>
		);
	}
}

export default class MatchPage extends Component{
    componentDidMount() {
        let intervalHandle =  window.setInterval(this.getBoard, 3000);
        this.setState({intervalHandle: intervalHandle});
    }

    componentWillUnmount() {
        window.clearInterval(this.state.intervalHandle)
    }

    constructor(props){
        super(props);

        this.getBoard = this.getBoard.bind(this);
        this.handleBoardClick = this.handleBoardClick.bind(this);

        this.getBoard();

        this.state={
            matchInfo: null,
            pos1:"",
            pos2:"",
            promoteTo:"R",
            gameWinner: ""
        }

    }

    render(){
        return(
        <Container>
        <Row>
            <Col>
                <Button onClick={()=>this.props.setAppPage("homepage")}>Go to home</Button>
            </Col>
            <Col>
                <Button onClick={()=>this.forfeit()}>Forfeit</Button>
            </Col>
            <Col>
                <Button onClick={() => this.props.setAppPage("login")}>Logout</Button>
            </Col>
        </Row>
        {this.renderBasicInfo()}
        {this.createBoard(this.state.matchInfo? this.state.matchInfo["board"] :null)}
        <Row>
            {this.renderMoveForm()}
        </Row>
        <Row>
            {this.state.errorMessage}
        </Row>
        </Container>);
    }

		handleBoardClick(pos){
			if(this.state.pos1 === ""){
				this.setState({pos1:pos});
			}else if(this.state.pos1 !== "" && this.state.pos2 === ""){
				this.state.pos2 = pos;
				this.boardClickMove();
				this.state.pos1 = "";
				this.state.pos2 = "";
			}
		}

		boardClickMove(){
			let move = {token: this.props.token, matchId: this.props.matchID, promoteTo:this.state.promoteTo};
			move.to = this.state.pos2;
			move.from = this.state.pos1;
			sendServerRequestWithBody("move", move, this.props.serverPort).then((response)=>
			{
					if(response.body.success){
							this.setState({errorMessage:null});
							this.getBoard();
					}
					else{
							this.setState({errorMessage:<ErrorBanner message={response.body.message}/>});
					}
			})
		}


		createBoard(b){
			let lets = ['a','b','c','d','e','f','g'];
			let nums = [7,6,5,4,3,2,1];
			let output = [];
			if(b == null)
				return null;
			let lines = b.split('\n');
			for(let i = 1; i < lines.length; i+=2){
				let columns = [];
				let chars = lines[i].split("│");
				for(let x = 0; x < chars.length; x++){
					if(chars[x] !== ""){
					    if((i >= 5 && i < 11) && (x >= 3 && x <= 5)){
                            columns.push(<Square value={chars[x].trim()} pos={lets[x-1] + nums[i/2>>0]}
                                                 handler={this.handleBoardClick} black={true}/>)
                        }else if (this.state.pos1 === (lets[x-1]+nums[i/2>>0])){
                            columns.push(<Square value={chars[x].trim()} pos={lets[x - 1] + nums[i / 2 >> 0]}
                                                 handler={this.handleBoardClick} black={false} yellow={true}/>)
                        } else {
                            columns.push(<Square value={chars[x].trim()} pos={lets[x - 1] + nums[i / 2 >> 0]}
                                                 handler={this.handleBoardClick} black={false}/>)
                        }
					}
				}
				const leftLabelStyle = {
					marginRight:'10px',
					fontSize:'30px'
				};
				output.push(<Row><label style={leftLabelStyle}>{nums[i/2>>0]}</label>{columns}</Row>)
			}

			const labelStyle = {
				marginLeft:'44px',
				fontSize:'30px',
				marginTop:'-5px'
			};
			output.push(<Row>{lets.map(item => <label style={labelStyle}>{item}</label>)}</Row>);

			return (output);
		}


    renderMoveForm(){
        return (
            <Form onSubmit={(event)=>this.sendMove(event)}>
                <FormGroup row> 
                    <Label for="promoteSelect" sm={4}>Promote To:</Label>
                    <Col sm={8}>
                        <Input type="select" onChange={(event)=>this.setState({promoteTo: event.target.value})}>
                            <option value="R" selected>Rook</option>
                            <option value="B">Bishop</option>
                        </Input>
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Label for="fromBox" sm={2}>From</Label>
                    <Col sm={10}>
                        <Input type="text" name="from" id="fromBox" placeholder="format: [a-g][1-7]" />
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Label for="toBox" sm={2}>To</Label>
                    <Col sm={10}>
                        <Input type="text" name="to" id="toBox" placeholder="format: [a-g][1-7]" />
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
        let move = {token: this.props.token, matchId: this.props.matchID, promoteTo:this.state.promoteTo};
        move.to = event.target.elements.to.value;
        move.from = event.target.elements.from.value;

        sendServerRequestWithBody("move", move, this.props.serverPort).then((response)=>
        {
            if(response.body.success){
                this.setState({errorMessage:null});
                this.getBoard();
            }
            else{
                this.setState({errorMessage:<ErrorBanner message={response.body.message}/>});
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
                else if(response.statusCode >=200 && response.statusCode <300)
                    this.setState({matchInfo: response.body});
        })
    }

    renderBasicInfo(){
        if(this.state.matchInfo)
            return(
                <Row>
                    You are {this.props.token.id === this.state.matchInfo.whiteId? "White": "Black"}. {this.state.matchInfo.finished? this.getWinner() :"It is "+ (this.state.matchInfo.turn?"White":"Black")+"'s turn."}
                </Row>
            );
        else
            return null;
    }

    getWinner(){
        if(this.state.gameWinner !== "")
            return this.state.gameWinner;
        if(this.state.matchInfo.whiteForfeit)
            return "White has forfeited. Black wins the game! Time to gloat or weep and play again.";
        if(this.state.matchInfo.blackForfeit)
            return "Black has forfeited. White wins the game! Time to gloat or weep and play again.";
        if(this.state.matchInfo.turn === true)
            return ("Black has won the game! Time to gloat or weep and play again.");
        else
            return ("White has won the game! Time to gloat or weep and play again.");
    }
    forfeit(){
        sendServerRequestWithBody("move", {token: this.props.token, matchId: this.props.matchID, forfeit:true}, this.props.serverPort).then((response)=>{
            if(response.body.gameOver === "WHITE")
                this.setState({gameWinner: "Black has forfeited. White wins the game! Time to gloat or weep and play again."});
            else if(response.body.gameOver === "BLACK")
                this.setState({gameWinner: "White has forfeited. Black wins the game! Time to gloat or weep and play again."});
            this.getBoard});
    }
}
