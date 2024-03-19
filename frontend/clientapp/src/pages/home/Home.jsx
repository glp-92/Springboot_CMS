import React, { useState, useEffect } from 'react'
import './Home.css'
import PostList from '../../components/postlist/PostList'
import Pagination from '@mui/material/Pagination';
import { GetPostList } from '../../util/requests/GetPostList';
import Loading from '../../components/loading/Loading';

const Home = () => {
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
      const posts = await GetPostList(page, null);
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
      <h1>Últimas entradas</h1>
      <main>
        {isLoading ? (
          <Loading />
        ) : (
          posts.length ? (
            <PostList postArr={posts} />
          ) : (
            <h2>En mantenimiento</h2>
          )
        )}
      </main>
      <Pagination count={npages} page={page} onChange={handlePageChange}/>
    </div>
  )
}

export default Home;