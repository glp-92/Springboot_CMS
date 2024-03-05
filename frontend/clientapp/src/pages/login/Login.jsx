import React, { useState, useEffect } from 'react'
import './Login.css'
import { Navigate } from 'react-router-dom';
import { ValidateToken } from '../../hooks/ValidateToken';

const Login = () => {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  // const [isLoading, setIsLoading] = useState(true); Al ser utilizado de forma propia, en principio no se mostrara pantalla de carga
  const [logged, setLogged] = useState(false);

  const handleLogin = (e) => { // Envia el formulario de login, carga el token en almacenamiento
    e.preventDefault(); // Previene el comportamiento por defecto, en caso de un form, refrescar la pagina
    const login = async () => {
      try {
        // setIsLoading(true);
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
      // setIsLoading(false);
    };
    login();
  };

  useEffect(() => { // Se verifica si se esta logueado actualmente y se redirige al panel de administracion
    const fetchTokenValid = async () => {
      const isValid = await ValidateToken();
      setLogged(isValid);
    }
    fetchTokenValid();
  }, [])

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