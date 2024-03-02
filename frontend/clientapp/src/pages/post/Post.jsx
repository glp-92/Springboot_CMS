import React, { useState, useEffect } from 'react'
import Markdown from 'markdown-to-jsx'
import { Navigate } from "react-router-dom";
import './Post.css'

const Post = () => {
  const slug = window.location.pathname.replace(`/post/`, "");
  const [postData, setPostData] = useState(null)
  const [isLoading, setIsLoading] = useState(true)  

  const getPostData = async () => {
    try {
      setIsLoading(true);
      const response = await fetch(`http://localhost:8080/blog/post/${slug}`);
      if (!response.ok) {
        throw new Error(`Error al obtener post: ${response.statusText}`);
      }
      const data = await response.json();
      setPostData(data);
    } catch (error) {
      setPostData(null);
    }
    setIsLoading(false);
  };

  useEffect(() => {
    getPostData();
  }, [slug])

  return (
    <div>
      {isLoading ? (
        <div>
          <h1>Cargando...</h1>
        </div>
      ) : (
        postData ? (
          <article>
            <header>
              <h1>{postData["title"]}</h1>
              <img src={`${postData["slug"]}/${postData["featuredImage"]}`} loading="lazy" width="120" height="120" />
            </header>
            <main>
              <Markdown>{postData["content"]}</Markdown>
            </main>
            <footer>
              <p>{postData["date"]}</p>
              <p>{postData["users"]["name"]}</p>
            </footer>
          </article>
        ) : (
          <Navigate to="/not-found" replace={true} />
        )
      )}
    </div>
  )
}

export default Post;