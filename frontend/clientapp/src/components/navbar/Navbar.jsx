import React, { useState } from 'react'
import './Navbar.css'

const NavBar = () => {
  const [searchText, setSearchText] = useState("");

  const handleSearch = (e) => {
    e.preventDefault(); //preventdefault evita actualizar la pagina
    if (searchText == "" || searchText.length > 20) {
      console.log("Error en entrada de busqueda")
      return;
    }
    window.location.href = `/search?keyword=${searchText}`;
  }

  return (
    <nav>
      <a href="/">Home</a>
      <form onSubmit={handleSearch}>
        <input type="text" placeholder="Search.." value={searchText} maxLength="20" onChange={(e) => { setSearchText(e.target.value) }} />
        <button type="submit">Buscar</button>
      </form>
    </nav>
  )
}

export default NavBar