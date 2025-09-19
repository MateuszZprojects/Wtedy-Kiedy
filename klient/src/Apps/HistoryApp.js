import React, { Component } from "react";
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import trophyImage from './Art/trophy.png'

class HistoryApp extends Component {
  constructor(props) {
    super(props);

    this.state = {
      status: 'OK',
      games: [],
      filtrText: '',
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.getGames = this.getGames.bind(this);
    this.fetchAndFilterGames = this.fetchAndFilterGames.bind(this);
  }

  async componentDidMount() {
    await this.fetchAndFilterGames();
  }

  async fetchAndFilterGames() {
    const filtrText = this.state.filtrText.toLowerCase();
    const userLogin = localStorage.getItem('userLogin');
    const userRole = localStorage.getItem('role');

    try {
      const response = await axios.get('http://localhost:8081/getgames');

      // Filtrowanie danych
      const filteredGames = response.data.filter(game => {
        const gameDescription = `${game.id}, ${game.player1login} ${game.player2login}`;
      
        // Check user role and filter accordingly
        if (userRole === '1') {
          return (
            (filtrText === "" || gameDescription.toLowerCase().includes(filtrText)) &&
            (game.player1login === userLogin || game.player2login === userLogin) &&
            game.player2login !== "0000" &&
            game.player2login !== "Removed"  // Exclude games where player2login is "0000" or "removed"
          );
        } else {
          return (
            (filtrText === "" || gameDescription.toLowerCase().includes(filtrText)) &&
            game.player2login !== null && game.player2login !== undefined && game.player2login !== "" &&
            game.player2login !== "0000" && game.player2login !== "Removed" && game.id !== 1
          );
        }
      });
      
      // Update state z danymi
      this.setState({ games: filteredGames, status: "OK" });
    } catch (err) {
      console.error("Błąd połączenia sieciowego.", err);
      this.setState({ status: "Błąd połączenia sieciowego." });
    }
  }

  async getGames() {
    await this.fetchAndFilterGames(); // Update the transfer list after fetching
  }

  handleChange(evt) {
    const name = evt.target.name;
    const value = evt.target.value;

    this.setState({ [name]: value }, () => {
      this.fetchAndFilterGames(); // Call the function after updating filtrText
    });
  }

  handleSubmit(event) {
    event.preventDefault();
  }

  render() {
    const { games } = this.state;

    return (
      <Container>
          <Form.Label>Szukaj: </Form.Label>
          <input 
            type="text"
            name='filtrText'
            value={this.state.filtrText}
            onChange={this.handleChange}
          />

        {" "}<Button variant="primary" onClick={this.getGames}>Odśwież</Button>

        <p></p>

        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Login Player 1</th>
              <th>Player 1 Score</th>
              <th></th>
              <th>Player 2 Score</th>
              <th>Login Player 2</th>
            </tr>
          </thead>
          <tbody>
            {games.map((game) => (
              (game.player2login !== 'removed') && (
                <tr key={game.id}>
                  <td>{game.id}</td>
                  <td>{game.player1login}</td>
                  <td>
                    {game.player1score === 10 ? (
                      <img src={trophyImage} alt="Trophy" style={{ width: '10%', height: 'auto' }} />
                    ) : (
                      game.player1score
                    )}
                  </td>
                  <td></td>
                  <td>
                    {game.player2score === 10 ? (
                      <img src={trophyImage} alt="Trophy" style={{ width: '10%', height: 'auto' }} />
                    ) : (
                      game.player2score
                    )}
                  </td>
                  <td>{game.player2login}</td>
                </tr>
              )
            ))}
          </tbody>
        </table>
      </Container>
    );
  }
}

export default HistoryApp;
