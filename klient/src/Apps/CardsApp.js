import React, { Component } from "react";
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

class CardsApp extends Component {

  constructor(props) {
    super(props);

    this.state = {
      status: 'OK',
      cards: [],
      filtrText: "",
      newCardYear: "",
      newCardDescription: "",
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.getCards = this.getCards.bind(this);
    this.fetchAndFilterCards = this.fetchAndFilterCards.bind(this);
    this.deleteCard = this.deleteCard.bind(this);
  }

  async componentDidMount() {
    await this.fetchAndFilterCards();
  }

  async fetchAndFilterCards() {
    const filtrText = this.state.filtrText.toLowerCase();

    try {
      const response = await axios.get('http://localhost:8081/getcards');
      let cardListText = "";

      const filteredCards = response.data.filter((card) => {
        let { year, description, deleted } = card;
        return (
          (filtrText === "" || 
          (year.toString().includes(filtrText) || 
          description.toLowerCase().includes(filtrText))) && 
          !deleted  // Exclude cards where deleted is true
        );
      });

      filteredCards.forEach((card) => {
        let {
          id,
          year,
          description,
        } = card;

        let cardDescription = `${id}, ${year} ${description}`;

        cardListText += cardDescription + "\n";
      });

      // Update state with the filtered result
      this.setState({ cards: filteredCards, textArea: cardListText, status: "OK" });
    } catch (err) {
      let myerror = "Błąd połączenia sieciowego." + err;
      this.setState({ status: myerror });
    }
  }

  async getCards() {
    await this.fetchAndFilterCards(); // Update the transfer list after fetching
  }  

  handleChange(evt) {
    const name = evt.target.name;   
    this.setState({ [name]: evt.target.value }, () => {
      // Add callback to fetch and filter cards after updating state
      this.fetchAndFilterCards();
    });
  }

  async deleteCard(cardId) {
    try {
      const response = await axios.post('http://localhost:8081/deletecard', {        cardidtodelete: cardId,      });  
      // Check if the response indicates success before updating the list      
        await this.fetchAndFilterCards(); // Update the list after deletion       
    } catch (err) {
      
      console.error("Error deleting card:", err);
    }
  }

  async addCard() {
    try {
      const { newCardYear, newCardDescription } = this.state;
      
       // Dodaj walidację dla nowego roku
       const currentYear = new Date().getFullYear();
       if (
         isNaN(newCardYear) ||
         parseInt(newCardYear) <= 0 ||
         parseInt(newCardYear) > currentYear
       ) {
         console.error("Nieprawidłowy rok.");
         return;
       }
 
       // Dodaj walidację dla opisu
       if (!newCardDescription.trim()) {
         console.error("Opis nie może być pusty.");
         return;
       }

      await axios.post('http://localhost:8081/addcard', {
        year: parseInt(newCardYear), // Convert to integer if needed
        description: newCardDescription,
      });  
      await this.fetchAndFilterCards();
    } catch (err) {
      console.error("Error adding card:", err);
    }
  }

  handleSubmit(event) {
    event.preventDefault();
    this.addCard();
  }

  render() {
    const { cards } = this.state;

    return (
      <Container>
        <Form.Label> Filter Text: <input type="text" name='filtrText' value={this.state.filtrText} onChange={this.handleChange} /> </Form.Label>
        {" "}<Button variant="primary" onClick={this.getCards}>  Odśwież  </Button>
        
        <p></p>

        <Form onSubmit={this.handleSubmit}>
          Rok:{" "}
          <input
            type="number"
            name="newCardYear"
            value={this.state.newCardYear}
            onChange={this.handleChange}
            min="1"
            max={new Date().getFullYear()}
          />
          {" "} Opis:{" "}
          <input
            type="text"
            name="newCardDescription"
            value={this.state.newCardDescription}
            onChange={this.handleChange}
          />
          {" "} <Button variant="primary" type="submit">Dodaj kartę</Button>
        </Form>
        {(!this.state.newCardYear.trim() || !this.state.newCardDescription.trim()) && (
          <p style={{ color: 'red' }}>Jeśli chcesz dodać kartę to rok i Opis nie mogą być puste.</p>
        )}
        <p></p>

        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Year</th>
              <th>Description</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {cards.map((card) => (
              <tr key={card.id}>
                <td>{card.id}</td>
                <td>{card.year}</td>
                <td>{card.description}</td>
                <td> 
                  <Button variant="danger" onClick={() => this.deleteCard(card.id)}> Delete </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>       
      </Container>
    );
  }
}

export default CardsApp;
