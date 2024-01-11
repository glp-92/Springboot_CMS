import React, { useState } from "react";

const Writer = () => {
  const [title, setTitle] = useState('');
  const [slug, setSlug] = useState('');
  const [excerpt, setExcerpt] = useState('');
  const [content, setContent] = useState('');
  const [featuredImage, setFeaturedImage] = useState('');

  const readImage = async (imageFile) => {
    if (imageFile) {
      const reader = new FileReader();
      reader.onload = () => {
        setFeaturedImage(reader.result);
      };
      reader.readAsDataURL(imageFile);
    }
  }

  const handleSendPost = (e) => {
    e.preventDefault()
    const uploadPost = async () => {
      const token = localStorage.getItem("jwt");
      try {
        const response = await fetch("http://localhost:8080/blog/post", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify({
            "title": title,
            "slug": slug,
            "excerpt": excerpt,
            "content": content,
            "featuredImage": featuredImage,
            "date": null,
            "featuredPost": false,
            "categoryIds": [1],
            "authorId": 1
          })
        });
        if (response.ok) {
          console.log("Imagen subida exitosamente");
        } else {
          console.error("Error al subir la imagen al servidor");
        }
      } catch (error) {
        console.error("Error en la solicitud:", error);
      }
    }
    uploadPost();
  }

  return (
    <form onSubmit={handleSendPost}>
      <input type="text" placeholder="Titulo" value={title} onChange={(e) => { setTitle(e.target.value) }}></input>
      <input type="text" placeholder="Slug" value={slug} onChange={(e) => { setSlug(e.target.value) }}></input>
      <input type="text" placeholder="Resumen" value={excerpt} onChange={(e) => { setExcerpt(e.target.value) }}></input>
      <input type="text" placeholder="Contenido" value={content} onChange={(e) => { setContent(e.target.value) }}></input>
      <input type="file" placeholder="Imagen" onChange={(e) => { readImage(e.target.files[0]) }} />
      {featuredImage && <img src={featuredImage} />}
      <button type="submit">Enviar</button>
    </form>
  )
}

export default Writer