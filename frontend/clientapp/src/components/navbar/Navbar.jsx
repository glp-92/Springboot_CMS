import React from 'react'
import './Navbar.css'

const NavBar = () => {
  return (
    <nav>
      <a href="/">Home</a>
      <form onSubmit={(e) => e.preventDefault()}>
        <input type="text" placeholder="Search.."/>
        <button type="submit">Buscar</button>
      </form>
    </nav>
  )
}

export default NavBar