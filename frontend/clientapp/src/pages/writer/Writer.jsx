import React, { useState } from "react";
import { uploadPost } from './../../util/api/UploadPost'
import { uploadImages } from './../../util/api/UploadImages'

const Writer = () => {
  const [title, setTitle] = useState('');
  const [slug, setSlug] = useState('');
  const [excerpt, setExcerpt] = useState('');
  const [content, setContent] = useState('');
  const [featuredImage, setFeaturedImage] = useState('');

  const readImage = async (imageFile) => {
    if (imageFile) {
      setFeaturedImage(imageFile);
    }
  }

  const handleSendPost = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("jwt");
      let response = await uploadPost(
        token,
        {
          "title": title,
          "slug": slug,
          "excerpt": excerpt,
          "content": content,
          "featuredImage": 'featuredImage',
          "date": null,
          "featuredPost": false,
          "categoryIds": [1],
          "authorId": 1
        }
      );
      if (response.ok) {
        response = await uploadImages(token, slug, featuredImage)
        console.log("Subida efectiva de POST")
      }
      else {
        console.log("Error en subida de post: ", response)
      }
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <form onSubmit={handleSendPost}>
      <input type="text" placeholder="Titulo" value={title} onChange={(e) => { setTitle(e.target.value) }}></input>
      <input type="text" placeholder="Slug" value={slug} onChange={(e) => { setSlug(e.target.value) }}></input>
      <input type="text" placeholder="Resumen" value={excerpt} onChange={(e) => { setExcerpt(e.target.value) }}></input>
      <input type="text" placeholder="Contenido" value={content} onChange={(e) => { setContent(e.target.value) }}></input>
      <input type="file" placeholder="Imagen" onChange={(e) => { readImage(e.target.files[0]) }} />
      {featuredImage && <img src={URL.createObjectURL(featuredImage)} />}
      <button type="submit">Enviar</button>
    </form>
  )
}

export default Writer