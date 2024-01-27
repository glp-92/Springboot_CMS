import React, { useEffect, useState } from "react";
import { uploadPost } from './../../util/api/UploadPost'
import { uploadImages } from './../../util/api/UploadImages'

const Writer = () => {
  const [title, setTitle] = useState('');
  const [slug, setSlug] = useState('');
  const [excerpt, setExcerpt] = useState('');
  const [content, setContent] = useState('');
  const [contentPosition, setContentPosition] = useState(0);
  const [featuredImage, setFeaturedImage] = useState('');

  const readImage = async (imageFile, isMainImage) => {
    if (imageFile && isMainImage) {
      setFeaturedImage(imageFile);
      return;
    }
    const imageName = imageFile.name;
    const dotIndex = imageName.lastIndexOf('.');
    const imgMdCode = `\n\n![${imageName.substring(0, dotIndex)}](${imageName})\n\n`;
    const newContent = content.substring(0, contentPosition) + imgMdCode + content.substring(contentPosition)
    setContent(newContent);
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
          "featuredImage": 'mainImage.webp',
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
      <div className="editor-wrapper">
        <div className="editor-toolbar">
          <label>
            <img src="imgIcon.svg" width="30" height="30" />
            <input id="image-input" type="file" accept="image/png, image/jpeg, image/webp" placeholder="Imagen" onChange={(e) => { readImage(e.target.files[0], false) }} style={{ "display": "none" }} />
          </label>
        </div>
        <textarea className="editor"
          placeholder="Contenido"
          value={content}
          onClick={(e) => { setContentPosition(e.target.selectionStart) }}
          onChange={(e) => { setContent(e.target.value); setContentPosition(e.target.selectionStart) }}
        />
      </div>
      <input type="file" accept="image/png, image/jpeg, image/webp" placeholder="Imagen" onChange={(e) => { readImage(e.target.files[0], true) }} />
      {featuredImage && <img src={URL.createObjectURL(featuredImage)} />}
      <button type="submit">Enviar</button>
    </form>
  )
}

export default Writer