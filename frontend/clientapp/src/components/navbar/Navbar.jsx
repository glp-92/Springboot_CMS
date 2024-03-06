import React, { useState } from 'react'
import './Navbar.css'

const NavBar = () => {
  const[searchText, setSearchText] = useState("");

  const handleSearch = (e) => {
    e.preventDefault(); //preventdefault evita actualizar la pagina
    if (searchText == "") return;
    window.location.href = `/search?keyword=${searchText}`;
  }

  return (
    <nav>
      <a href="/">Home</a>
      <form onSubmit={handleSearch}>
        <input type="text" placeholder="Search.." value={searchText} onChange={(e) => { setSearchText(e.target.value) }}/>
        <button type="submit">Buscar</button>
      </form>
    </nav>
  )
}

export default NavBar