import React, { Component } from "react";
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';
import axios from 'axios';
import GameApp from "./GameApp";
import joinImage from './Art/join.png'

class GameAppList extends Component {
  constructor(props) {
    
    super(props);

    this.state = {
      status: 'OK',
      games: [],
      filtrText: '',
      isGameCreated: false,
      isInGameAppView: false,
    };
  }

  componentDidMount() {
    this.fetchAndFilterGames();
    this.intervalId = setInterval(() => {
      this.fetchAndFilterGames();
    }, 1000);
  }

  componentWillUnmount() {
    clearInterval(this.intervalId);
  }

  async fetchAndFilterGames() {
    const filtrText = this.state.filtrText.toLowerCase();

    try {
      const response = await axios.get('http://localhost:8081/getgames');

      const filteredGames = response.data.filter(game => {
        const gameDescription = `${game.id}, ${game.player1login} ${game.player2login}`;
        return (
          (filtrText === "" || gameDescription.toLowerCase().includes(filtrText)) &&
          game.player2login === ""
        );
      });

      this.setState({ games: filteredGames, status: "OK" });
    } catch (err) {
      console.error("Błąd połączenia sieciowego.", err);
      this.setState({ status: "Błąd połączenia sieciowego." });
    }
  }

  createGame = async () => {
    try {
        const response = await axios.post('http://localhost:8081/addgame', {
            login: localStorage.getItem('userLogin'),
        });

        console.log("Pokój utworzony:", response.data.gameId);
        localStorage.setItem('gameRole', 'player1');
        localStorage.setItem('gameId', response.data.gameId);
        this.setState({ isGameCreated: true, isInGameAppView: true });
    } catch (err) {
        console.error("Błąd podczas tworzenia pokoju.", err);
    }
}

  joinGame = async (gameId) => {
    try {
      const userLogin = localStorage.getItem('userLogin');
      await axios.post('http://localhost:8081/joingame', {
        gameId: gameId,
        userLogin: userLogin,
      });

      // Dodaj tutaj dowolną logikę, którą chcesz wykonać po udanym dołączeniu do gry

      console.log(`Dołączono do gry o ID: ${gameId} jako użytkownik: ${userLogin}`);
      localStorage.setItem('gameRole', 'player2');
      localStorage.setItem('gameId', gameId);
      this.setState({ isGameCreated: true, isInGameAppView: true });
    } catch (err) {
      console.error("Błąd podczas dołączania do gry.", err);
    }
  }


  handleChange = (evt) => {
    const name = evt.target.name;
    if (name === "filtrText") {
      this.setState({ filtrText: evt.target.value }, () => {
        this.fetchAndFilterGames();
      });
    }
  }

  handleEndGame = () => {
    this.setState({ isGameCreated: false, isInGameAppView: false });
  };

  render() {
    const { games, isGameCreated, isInGameAppView} = this.state;

    if (isInGameAppView) {
      // Jeśli aktualnie znajdujesz się w widoku GameApp, zwróć ten widok
      return (
        <Container>
          <div>
            <GameApp onEndGame={this.handleEndGame} isInGameAppView={isInGameAppView} />
          </div>
        </Container>
      );
    }

    // W przeciwnym razie, renderuj tabelkę z listą dostępnych gier
    return (
      <Container>
        <div>
          {!isGameCreated && (
            <Button variant="secondary" onClick={this.createGame}>
              Utwórz pokój
            </Button>
          )}
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Pokój gracza</th>
                <th> </th>
              </tr>
            </thead>
            <tbody>
              {games.map((game) => (
                <tr key={game.id}>
                  <td>{game.id}</td>
                  <td>{game.player1login}</td>
                  <td><button onClick={() => this.joinGame(game.id)}><img src={joinImage} style={{ width: '50%', height: 'auto' }} alt="Dołącz"/></button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </Container>
    );
  }
}

export default GameAppList;
