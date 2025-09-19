import React, { Component } from "react";
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

class AccountApp extends Component {
  constructor(props) {
    super(props);

    this.state = {
      status: 'OK',
      accounts: [],
      filtrText: "",
    };

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.getAccounts = this.getAccounts.bind(this);
    this.fetchAndFilterAccounts = this.fetchAndFilterAccounts.bind(this);
  }

  userId = localStorage.getItem('userId');

  async componentDidMount() {
    await this.fetchAndFilterAccounts();
  }

  async fetchAndFilterAccounts() {
    const filtrText = this.state.filtrText.toLowerCase();

    try {
      const response = await axios.get('http://localhost:8081/getaccounts');
      let accountListText = "";

      const filteredAccounts = response.data.filter((account) => {
        let { login, deleted } = account;
        return (
          (filtrText === "" || login.toLowerCase().includes(filtrText)) &&
          !deleted // Exclude deleted accounts
        );
      });

      filteredAccounts.forEach((account) => {
        let {
          id,
          login,
          type,
        } = account;

        let accountDescription = `${id}, ${login} ${type}`;
        accountListText += accountDescription + "\n";
      });

      // Sort accounts by ID before updating state
      filteredAccounts.sort((a, b) => a.id - b.id);

      // Update state with the sorted and filtered result
      this.setState({ accounts: filteredAccounts, textArea: accountListText, status: "OK" });
    } catch (err) {
      let myerror = "Błąd połączenia sieciowego." + err;
      this.setState({ status: myerror });
    }
  }

  async getAccounts() {
    await this.fetchAndFilterAccounts(); // Update the transfer list after fetching
  }

  handleChange(evt) {
    const { name, value } = evt.target;

    this.setState({ [name]: value }, () => {
      this.fetchAndFilterAccounts(); // Call the function after updating filtrText
    });
  }

  handleSubmit(event) {
    event.preventDefault();
  }

  getRoleName(type) {
    switch (type) {
      case '1':
        return "Gracz";
      case '2':
        return "Moderator";
      case '3':
        return "Admin";
      default:
        return "Unknown";
    }
  }

  async deleteAccount(accountId) {
    try {
      const confirmed = window.confirm("Czy na pewno chcesz usunąć to konto?");

      if (!confirmed) {
        return; // If the user cancels the deletion, exit the function
      }
      const response = await axios.post('http://localhost:8081/deleteaccount', {
        accountidtodelete: accountId, // Fix the parameter name
      });
      await this.fetchAndFilterAccounts();      
    } catch (err) {
      console.error("Error deleting account:", err);
    }
  }

  async updateAccount(accountId) {
    try {      
      const response = await axios.post('http://localhost:8081/updateaccount', {
        accountidtoupdate: accountId, // Fix the parameter name
      });
      await this.fetchAndFilterAccounts();      
    } catch (err) {
      console.error("Error updating account:", err);
    }
  }

  render() {
    const { accounts, userId } = this.state;

    return (
      <Container>
        <Form.Group>
          <Form.Label>
            Filter Text:{" "}
            <input
              type="text"
              name="filtrText"
              value={this.state.filtrText}
              onChange={this.handleChange}
            />{" "}
          </Form.Label>
          <Button variant="primary" onClick={this.getAccounts}>
            Odśwież listę
          </Button>
        </Form.Group>

        <p></p>
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Login</th>
              <th>Typ</th>
              <th>Akcja</th>
            </tr>
          </thead>
          <tbody>
            {accounts.map((account) => (
              <tr key={account.id}>
                <td>{account.id}</td>
                <td>{account.login}</td>
                <td>{this.getRoleName(account.type)}</td>
                <td>
                  {account.type !== '3' && account.id !== userId && (
                    <>
                      <Button variant="info" onClick={() => this.updateAccount(account.id)}>Zmień uprawnienia</Button>
                      {" "}
                      <Button variant="danger" onClick={() => this.deleteAccount(account.id)}> Usuń</Button>
                    </>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </Container>
    );
  }
}

export default AccountApp;
