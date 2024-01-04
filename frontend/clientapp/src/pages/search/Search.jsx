import React from 'react'
import PostList from '../../components/postlist/PostList'

const Search = () => {
  return (
    <div>
      <h1>Palabras clave buscadas:</h1>
      <main>
        <PostList/>
      </main>
    </div>
  )
}

export default Search;