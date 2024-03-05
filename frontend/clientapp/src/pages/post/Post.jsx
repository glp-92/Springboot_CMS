import React, { useState, useEffect } from 'react'
import PostContent from '../../components/postContent/PostContent'
import { Navigate } from "react-router-dom";
import './Post.css'

const Post = () => {
  const slug = window.location.pathname.replace(`/post/`, ""); // Se elimina el /post para la constante slug ya que en BBDD se introduce el slug tal cual
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

  // Mientras carga se muestra un layout, una vez ha hecho la consulta, si postData es null, se mostrara un notFound, en caso contrario, se renderizara el post
  return (
    <div>
      {isLoading ? (
        <div>
          <h1>Cargando...</h1>
        </div>
      ) : (
        postData ? (
          <PostContent postData={postData} />
        ) : (
          <Navigate to="/not-found" replace={true} />
        )
      )}
    </div>
  )
}

export default Post;