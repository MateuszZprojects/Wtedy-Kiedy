// LoginForm.js
import React, { useState } from 'react';
import axios from 'axios';

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100vh',
    fontFamily: 'Arial, sans-serif',
  },
  form: {
    width: '300px',
    padding: '20px',
    border: '1px solid #ccc',
    borderRadius: '8px',
    boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
    backgroundColor: '#fff',
  },
  input: {
    width: '100%',
    padding: '8px',
    margin: '8px 0',
    boxSizing: 'border-box',
  },
  button: {
    width: '100%',
    padding: '10px',
    margin: '10px 0',
    backgroundColor: '#4CAF50',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
  },
  switchButton: {
    background: 'none',
    border: 'none',
    color: '#4CAF50',
    cursor: 'pointer',
  },
};

function LoginForm({ shouldRenderTestTabs, setShouldRenderTestTabs }) {
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const [showRegister, setShowRegister] = useState(false);
  const [error, setError] = useState(null); // Nowy stan przechowujący informację o błędzie


  const handleAction = async (e) => {
    e.preventDefault();

    try {
      let endpoint = showRegister ? 'register' : 'login';
      const response = await axios.post(`http://localhost:8081/${endpoint}`, { login, password });

      if (showRegister) {
        console.log('Konto utworzone:', response.data);
        // Po pomyślnym utworzeniu konta, wywołujemy funkcję logowania
        handleLogin();
      } else {
        const { userLogin, userId, userRole } = response.data;
        localStorage.setItem('userLogin', userLogin);
        localStorage.setItem('userId', userId);
        localStorage.setItem('role', userRole);
        setShouldRenderTestTabs(!shouldRenderTestTabs);
      }
      setError(null); // Zresetowanie błędu po udanym logowaniu/rejestracji

    } catch (error) {
      console.error(showRegister ? 'Błąd podczas tworzenia konta:' : 'Login error:', error.message);
      setError(showRegister ? 'Błąd podczas tworzenia konta. Spróbuj ponownie.' : 'Błędny login lub hasło.');
    }
  };

  const handleLogin = async () => {
    try {
      const response = await axios.post('http://localhost:8081/login', { login, password });
      const { userLogin, userId, userRole } = response.data;

      localStorage.setItem('userLogin', userLogin);
      localStorage.setItem('userId', userId);
      localStorage.setItem('role', userRole);
      setShouldRenderTestTabs(!shouldRenderTestTabs);
    } catch (error) {
      console.error('Login error:', error.message);
      // Handle authentication error, show error message, etc.
    }
  };

  return (
    <div style={styles.container}>
      <form style={styles.form} onSubmit={handleAction}>
        <h1>{showRegister ? 'Rejestracja' : 'Logowanie'}</h1>
        <label>
          Login:
          <input
            type="text"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
            style={styles.input}
          />
        </label>
        <br />
        <label>
          Hasło:
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={styles.input}
          />
        </label>
        {error && <p style={{ color: 'red' }}>{error}</p>} {/* Dodanie komunikatu o błędzie */}
        <br />
        <button type="submit" style={styles.button}>
          {showRegister ? 'Utwórz konto' : 'Zaloguj się'}
        </button>
        <p>
          {showRegister ? 'Masz konto? Zaloguj się!' : 'Nie masz konta? Utwórz konto'}
          <button
            type="button"
            style={styles.switchButton}
            onClick={() => {
              setShowRegister((prevShowRegister) => !prevShowRegister);
              setError(null); // Zresetowanie błędu po zmianie trybu logowania/rejestracji
            }}
          >
            {showRegister ? 'Powrót' : 'Utwórz konto'}
          </button>
        </p>
      </form>
    </div>
  );
}

export default LoginForm;