import React from 'react'

const Home = () => {
  return (
      <body>
        <header>
          <h1>Encabezado</h1>
          <nav>
            <ul>
              <li><a href="#">Enlace 1</a></li>
              <li><a href="#">Enlace 2</a></li>
              <li><a href="#">Enlace 3</a></li>
            </ul>
          </nav>
        </header>
        <main>
          <form action="#">
            <input type="text" placeholder="Buscar"/>
            <button type="submit">Buscar</button>
          </form>
          <article>
            <h2>Título del contenido</h2>
            <p>Contenido de la página</p>
          </article>
        </main>
      </body>
  )
}

export default Home;