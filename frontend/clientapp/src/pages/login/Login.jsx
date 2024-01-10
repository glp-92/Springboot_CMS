import React, { useState } from 'react'
import './Login.css'
import { Navigate } from 'react-router-dom';

const Login = () => {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setIsLoading] = useState(true);
  const [logged, setLogged] = useState(false);

  const handleLogin = (e) => {
    e.preventDefault(); // Previene el comportamiento por defecto, en caso de un form, refrescar la pagina
    const login = async () => {
      try {
        setIsLoading(true);
        const response = await fetch(`http://localhost:8080/auth/login`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({ "username": username, "password": password })
        });
        if (!response.ok) {
          throw new Error(`Error en Login: ${response.statusText}`);
        }
        const authorizationHeader = response.headers.get("Authorization");
        if (authorizationHeader) {
          const token = authorizationHeader.split(" ")[1];
          localStorage.setItem("jwt", token);
          setLogged(true);
        }
      } catch (error) {
        console.log(error)
      }
      setIsLoading(false);
    };
    login();
  };


  return (
    <div className="login-wrapper">
      {logged && (
        <Navigate to="/wpannel" replace={true} />
      )}
      <h1>Login</h1>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder='Username'
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder='Password'
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">
          Login
        </button>
      </form>
    </div>
  )
}

export default Login;