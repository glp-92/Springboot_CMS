import React, { useState, useEffect } from 'react'
import PostList from '../../components/postlist/PostList'
import Pagination from '@mui/material/Pagination';
import { GetPostList } from '../../hooks/GetPostList';

const Search = () => {
  let queryparams = window.location.search.replace('?', '&');
  const [posts, setPosts] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [npages, setNPages] = useState(0);

  const handlePageChange = (event, value) => {
    setPage(value - 1);
  }

  useEffect(() => { // Al llegar a home
    const fetchPosts = async (page) => {
      setIsLoading(true);
      const posts = await GetPostList(page, queryparams);
      if (posts != null) {
        setPosts(posts["content"]);
        setNPages(posts["totalPages"])
      }
      else {
        setPosts([]);
      }
      setIsLoading(false);
    }
    fetchPosts(page);
  }, [page])

  return (
    <div>
      <h1>Resultados de búsqueda</h1>
      <main>
        {isLoading ? (
          <div>
            <h2>Cargando...</h2>
          </div>
        ) : (
          posts.length ? (
            <PostList postArr={posts} />
          ) : (
            <h2>No se han encontrado resultados</h2>
          )
        )}
      </main>
      <Pagination count={npages} page={page} onChange={handlePageChange}/>
    </div>
  )
}

export default Search;