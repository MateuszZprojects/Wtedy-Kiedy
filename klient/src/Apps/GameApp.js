import React, { Component } from "react";
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import clockImage from './Art/clock.png'


class GameApp extends Component {

    constructor(props) {
        super(props);
        this.savedCards = 0; // Fixed initialization

        this.state = {
            yourpoints: 0,
            enemypoints: 0,
            savedCards:0,
            timer: 20,
            status: 'OK',
            gamecards: [],
            previousGamecards: [],
            filtrText: '',
            disabledButtons: localStorage.getItem('gameRole') === "player2", // Ustawienie disabledButtons na true dla player2
        
            
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.getGamecards = this.getGamecards.bind(this);
        this.fetchAndFilterGamecards = this.fetchAndFilterGamecards.bind(this);
        this.gameData = this.gameData.bind(this);

    }


    componentDidMount() { 
        this.fetchAndFilterGamecards();
        this.intervalId = setInterval(() => {
            this.gameData();
            this.fetchAndFilterGamecards();
        }, 1000);
    }

    componentWillUnmount() {
        clearInterval(this.intervalId);
    }

    async fetchAndFilterGamecards() {
        const filtrText = this.state.filtrText.toLowerCase();
        const gameId = localStorage.getItem('gameId'); 
    
        try {
            const previousGamecards = [...this.state.gamecards];
            const response = await axios.get(`http://localhost:8081/getgamecards?gameId=${gameId}`);
            const filteredGamecards = response.data.filter(gamecard => {
                const gamecardDescription = `${gamecard.id}, ${gamecard.playerid} ${gamecard.cardid}`;
                return filtrText === "" || gamecardDescription.toLowerCase().includes(filtrText);
            });

            if (this.savedCards < filteredGamecards.length) {
                this.setState({ timer: 20 });
                this.savedCards = filteredGamecards.length;
            }
            
            if (this.savedCards > 0) {
                this.setState((prevState) => ({ timer: prevState.timer - 1 }));
                if(this.state.timer==0) {this.handleTimeOut()}
                if (localStorage.getItem('gameRole') === 'player2' && filteredGamecards.length % 2 === 0) {
                    this.setState({ disabledButtons: false });
                }
                if (localStorage.getItem('gameRole') === 'player1' && filteredGamecards.length % 2 === 1) {
                    this.setState({ disabledButtons: false });
                }    
            }
    
            this.setState({ gamecards: filteredGamecards, previousGamecards, status: "OK" });
        } catch (err) {
            console.error("Błąd połączenia sieciowego.", err);
            this.setState({ status: "Błąd połączenia sieciowego." });
        }
    }
    
    async gameData() {       
        const gameId = localStorage.getItem('gameId');
        const gameRole = localStorage.getItem('gameRole'); 
    
        try {
            const response = await axios.get(`http://localhost:8081/getscore?gameId=${gameId}`);
            const { player2login, deleted, id, player1login, player1score, player2score } = response.data;
            localStorage.setItem('player2Login', player2login);
            if(gameRole=='player1'){
                this.setState({enemypoints: player2score})
                this.setState({yourpoints: player1score})
                if(this.state.enemypoints==10) {this.props.onEndGame();alert("Przeciwnik wygrał")}
                if(this.state.yourpoints==10) {this.props.onEndGame();alert("Wygrałeś")}
            }
            else {
                this.setState({enemypoints: player1score})
                this.setState({yourpoints: player2score})
                if(this.state.enemypoints==10) {this.props.onEndGame();alert("Przeciwnik wygrał")}
                if(this.state.yourpoints==10) {this.props.onEndGame();alert("Wygrałeś")}


            }


            
        } catch (err) {
            console.error("Błąd połączenia sieciowego.", err);
            this.setState({ status: "Błąd połączenia sieciowego." });
        }
    }
    

    async getGamecards() {
        await this.fetchAndFilterGamecards();
    }

    handleChange(evt) {
        const name = evt.target.name;
        if (name === "filtrText") {
            this.setState({ filtrText: evt.target.value }, () => {
                this.fetchAndFilterGamecards();
            });
        }
    }

    handleSubmit(event) {
        event.preventDefault();
    }

    async handleTimeOut(){
        if(this.state.disabledButtons==true) {
            alert("Przeciwnikowi skończył się czas");
            localStorage.removeItem("player2Login");
            this.props.onEndGame();

        }
        else if(this.state.disabledButtons==false) {
            alert("Skończył Ci się czas")
            try {
                const gameID = localStorage.getItem('gameId');
                const userLogin = localStorage.getItem("userLogin");
                const requestData = {
                    gameID: gameID,
                    userLogin: userLogin
                };
                console.log('Sending data:', requestData);
        
                const response = await axios.post('http://localhost:8081/surrender', requestData);
                console.log(response.data);
        
                // Clear player2login from localStorage
                localStorage.removeItem("player2Login");
        
                this.props.onEndGame();
            } catch (error) {
                console.error("Błąd: " + error.message);
            }
        }
    }

    handleButtonClick = async (time, pickedYear, cardYear) => {
        try {
            const gameID = localStorage.getItem('gameId'); // assuming you have gameId available
            const userID = localStorage.getItem("userId");
            const gameRole = localStorage.getItem("gameRole");
            this.setState({ disabledButtons: true });
    
            const requestData = {
                time: time,
                cardYear: cardYear,
                pickedYear: pickedYear,
                userID: userID,
                gameID: gameID,
                gameRole: gameRole
            };
            console.log('Sending data:', requestData);  // Dodaj tę linię

            const response = await axios.post('http://localhost:8081/addselect', requestData);
            console.log(response.data);
    
            // Continue with the rest of your logic after the HTTP request is complete
            this.fetchAndFilterGamecards();
        } catch (error) {
            console.error("Błąd podczas pobierania danych karty: " + error.message);
        }
    };

    handleEndGame = async() => {
        this.props.onEndGame(); // Wywołanie metody przekazanej jako props
      };
    
    
    handleSurrender = async () => {
        alert("Poddałeś się");
        try {
            const gameID = localStorage.getItem('gameId');
            const userLogin = localStorage.getItem("userLogin");
            const requestData = {
                gameID: gameID,
                userLogin: userLogin
            };
            console.log('Sending data:', requestData);
    
            const response = await axios.post('http://localhost:8081/surrender', requestData);
            console.log(response.data);
    
            // Clear player2login from localStorage
            localStorage.removeItem("player2Login");
    
            this.props.onEndGame();
        } catch (error) {
            console.error("Błąd: " + error.message);
        }
    };

    handleDeleteGame = async () => {
        try {
            const gameId = localStorage.getItem('gameId');
            const requestData = {
                gameID: gameId
            };
            console.log('Sending data:', requestData);
    
            const response = await axios.post('http://localhost:8081/deletegame', requestData);
            console.log(response.data);
            this.props.onEndGame();
        } catch (error) {
            console.error('Błąd: ' + error.message);
        }
    };

    render() {
        const { gamecards, yourpoints, enemypoints, timer, disabledButtons } = this.state;
        const gameId = localStorage.getItem('gameId');
        const userId = localStorage.getItem('userId');
        const player2Login = localStorage.getItem('player2Login');
        const player1Cards = gamecards.filter((gamecard) => gamecard.playerid == userId && gamecard.gameid == gameId);
        const player0Cards = gamecards.filter((gamecard) => gamecard.playerid == 0 && gamecard.gameid == gameId);
        const player2Cards = gamecards.filter(
            (gamecard) =>
                gamecard.playerid != userId &&
                gamecard.playerid != 0 &&
                gamecard.gameid == gameId
        );
        const sortedPlayer1Cards = player1Cards.slice().sort((a, b) => a.year - b.year);
        const sortedPlayer0Cards = player0Cards.slice().sort((a, b) => a.year - b.year);
        const sortedPlayer2Cards = player2Cards.slice().sort((a, b) => a.year - b.year);
        localStorage.setItem("Player1Cards", sortedPlayer1Cards.length);
        localStorage.setItem("Player2Cards", sortedPlayer2Cards.length);
    
        return (
            <Container className="text-center">
                <div>
                    {player2Login !== "" && (
                        <Button variant="danger" onClick={() => this.handleSurrender()}>
                            Poddaj się
                        </Button>
                    )}
                    {player2Login === "" && (
                        <Button variant="danger" onClick={() => this.handleDeleteGame()}>
                            Usuń grę
                        </Button>
                    )}
                    <div className="my-3">
                        Twoje punkty: {yourpoints} | Czas: {timer} <img src={clockImage} style={{ width: '3%', height: 'auto', marginLeft: '5px' }} alt="Zegar" /> | Punkty przeciwnika: {enemypoints} 
                    </div>
                </div>
                <div style={{ display: 'flex', justifyContent: 'center' }}>
                    <table className="table" style={{ backgroundColor: '#ea8c27', marginRight: '10px' }}>
                        <thead>
                            <tr>
                                <th>Przed</th>
                                <th>Twoje karty</th>
                                <th>Po</th>
                            </tr>
                        </thead>
                        <tbody>
                            {sortedPlayer1Cards.map((gamecard) => (
                                <tr key={gamecard.cardId}>
                                    <td><Button variant="primary" onClick={() => this.handleButtonClick("before", gamecard.year, player0Cards[0].year)} disabled={disabledButtons}>↑</Button></td>
                                    <td>[<b>{gamecard.year}</b>] {gamecard.description}</td>
                                    <td><Button variant="primary" onClick={() => this.handleButtonClick("after", gamecard.year, player0Cards[0].year)} disabled={disabledButtons}>↓</Button></td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
    
                    <table className="table" style={{ marginRight: '10px' }}>
                        <thead>
                            <tr>
                                <th>Karta</th>
                            </tr>
                        </thead>
                        <tbody>
                            {sortedPlayer0Cards.map((gamecard) => (
                                <tr key={gamecard.cardId}>
                                    <td style={{ backgroundColor: 'yellow' }}>
                                        [<b>{gamecard.year}</b>] {gamecard.description}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
    
                    <table className="table" style={{ backgroundColor: '#ea8c27' }}>
                        <thead>
                            <tr>
                                <th>Karty przeciwnika</th>
                            </tr>
                        </thead>
                        <tbody>
                            {sortedPlayer2Cards.map((gamecard) => (
                                <tr key={gamecard.cardId}>
                                    <td>[<b>{gamecard.year}</b>] {gamecard.description}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </Container>
        );
    }
}

export default GameApp;
