import React, { useState, useEffect } from 'react'
import './Home.css'
import PostList from '../../components/postlist/PostList'

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [page, setPage] = useState(0);
  const getPosts = async (page) => {
    try {
      setIsLoading(true);
      const response = await fetch(`http://localhost:8080/blog/post?page=${page}`);
      if (!response.ok) {
        throw new Error(`Error al obtener posts: ${response.statusText}`);
      }
      const fetchPosts = await response.json();
      setPosts(fetchPosts);
    } catch (error) {
      setPosts([]);
    }
    setIsLoading(false);
  };

  useEffect(() => {
    getPosts(page);
  }, [page])

  return (
    <div>
      <h1>Últimas entradas</h1>
      <main>
        {isLoading ? (
          <div>
            <h2>Cargando...</h2>
          </div>
        ) : (
          posts.length ? (
            <PostList postArr={posts} />
          ) : (
            <h1>En mantenimiento</h1>
          )
        )}
      </main>
    </div>
  )
}

export default Home;