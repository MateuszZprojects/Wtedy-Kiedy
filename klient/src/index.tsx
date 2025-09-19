// index.tsx
import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';

import TestTabs from './Apps/TestTabs';
import LoginForm from './Apps/Login';

// Umieść style bezpośrednio w pliku
const styles = `
  body {
    background-color: #f79618; /* Odcień żółtego, bardziej brunatny */
    margin: 0;
    padding: 0;
    font-family: 'Arial', sans-serif;
  }

  .status-message {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    padding: 10px;
    text-align: center;
    opacity: 1;
    transition: opacity 0.5s ease;
  }

  .status-message.error {
    background-color: red; /* Kolor czerwony dla komunikatów błędów */
    color: white;
  }

  .status-message.success {
    background-color: green; /* Kolor zielony dla komunikatów sukcesu */
    color: white;
  }

  .status-message.hidden {
    opacity: 0;
  }
`;

const App = () => {
  const [shouldRenderTestTabs, setShouldRenderTestTabs] = useState(false);
  const [status, setStatus] = useState<string | null>(null);

  const ServerStatus = async () => {
    try {
      const response = await axios.get(`http://localhost:8081/status`);
      console.log(response);
      setStatus('Połączono');

      setTimeout(() => {
        setStatus(null);
      }, 3000);
    } catch (err) {
      console.error("Błąd połączenia sieciowego.", err);
      setStatus('Błąd połączenia sieciowego.');

      setTimeout(() => {
        setStatus(null);
      }, 3000);
    }
  };

  useEffect(() => {
    ServerStatus();
  }, []);

  return (
    <React.StrictMode>
      <style>{styles}</style>

      {status && (
        <div
          className={`status-message ${status.includes('Błąd') ? 'error' : 'success'}`}
        >
          {status}
        </div>
      )}

      {shouldRenderTestTabs ? (
        <TestTabs
          shouldRenderTestTabs={shouldRenderTestTabs}
          setShouldRenderTestTabs={setShouldRenderTestTabs}
        />
      ) : (
        <LoginForm
          shouldRenderTestTabs={shouldRenderTestTabs}
          setShouldRenderTestTabs={setShouldRenderTestTabs}
        />
      )}
    </React.StrictMode>
  );
};

ReactDOM.render(<App />, document.getElementById('root'));
