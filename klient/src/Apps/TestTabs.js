// TestTabs.js
import GameAppList from "./GameAppList";
import CardsApp from "./CardsApp";
import AccountApp from "./AccountApp";
import HistoryApp from "./HistoryApp";
import { Container, Tabs, Tab, Row, Col, Button } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import userListIcon from './Art/user.png'; 
import cardListIcon from './Art/cards.png';
import historyListIcon from './Art/history.png';
import gameListIcon from './Art/gamepad.png';

const TestTabs = ({ shouldRenderTestTabs, setShouldRenderTestTabs }) => {
  const accountRole = localStorage.getItem('role');
  const accountLogin = localStorage.getItem('userLogin');

  const handleLogout = () => {
    localStorage.removeItem('userLogin');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('gameId');
    localStorage.removeItem('gameRole');

    setShouldRenderTestTabs(!shouldRenderTestTabs);
  };

  const containerStyle = {
    backgroundColor: '#f9b813', // Ciemnożółty kolor tła
    minHeight: '100vh',
    paddingTop: '20px',
  };

  const tabContentStyle = {
    backgroundColor: '#f9b813', // Jasnożółty kolor tła dla treści zakładek
    padding: '20px',
  };
  
  const buttonColStyle = {
    textAlign: 'center',
    marginTop: '20px',
  };

  return (
    <Container style={containerStyle}>
    <Row>
      <Col>
        <Tabs id="tab-example" style={tabContentStyle} defaultActiveKey="gameroomlist">
          {accountRole && (
            <Tab eventKey="gameroomlist" title={<span><img src={gameListIcon} alt="Pokoje gier" /></span>}>
              <GameAppList />
            </Tab>
          )}
          {accountRole && (
            <Tab eventKey="gamelist" title={<span><img src={historyListIcon} alt="Historia gier" /></span>}>
              <HistoryApp />
            </Tab>
          )}
          {(accountRole === '2' || accountRole === '3') && (
            <Tab eventKey="cardlist" title={<span><img src={cardListIcon} alt="Karty" /></span>}>
              <CardsApp />
            </Tab>
          )}
          {accountRole === '3' && (
            <Tab eventKey="userlist" title={<span><img src={userListIcon} alt="Lista kont" /></span>}>
              <AccountApp />
            </Tab>
          )}
        </Tabs>
      </Col>
      <Col sm={1} style={buttonColStyle}>
        {accountLogin}
        <Button variant="danger" onClick={handleLogout}>Wyloguj</Button>        
      </Col>
    </Row>
  </Container>
  );
};

export default TestTabs;
