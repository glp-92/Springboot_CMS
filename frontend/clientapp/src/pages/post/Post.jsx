import React, { useState, useEffect } from 'react'
import Markdown from 'markdown-to-jsx'
import { Navigate } from "react-router-dom";
import './Post.css'

const Post = () => {
  const slug = window.location.pathname.replace(`/post/`, ""); // Se elimina el /post para la constante slug ya que en BBDD se introduce el slug tal cual
  const [postData, setPostData] = useState(null)
  const [isLoading, setIsLoading] = useState(true)

  const overrideContentImagePath = (props) => { // Funcion que provee la libreria markdown-to-jsx para sobreescribir la ruta de imagen que debe tener el directorio /post/slug/... y por defecto solo trae el nombre de la BBDD
    const overridesrc = `${postData["slug"]}/${props.src}`;
    const overrideProps = { ...props, src: overridesrc, loading: "lazy" };
    return <img {...overrideProps} />;
  }

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
          <article>
            <header>
              <h1>{postData["title"]}</h1>
              <img src={`${postData["slug"]}/${postData["featuredImage"]}`} loading="lazy" width="120" height="120" />
            </header>
            <main>
              <Markdown options={{
                overrides: {
                  img: overrideContentImagePath
                }
              }}>
                {postData["content"]}
              </Markdown>
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