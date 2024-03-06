import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { uploadPost } from './../../util/api/UploadPost'
import { uploadImages } from './../../util/api/UploadImages'

const Writer = () => {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [slug, setSlug] = useState('');
  const [excerpt, setExcerpt] = useState('');
  const [content, setContent] = useState('');
  const [categories, setCategories] = useState([]);
  const [selectedCategorie, setSelectedCategorie] = useState(null);
  const [contentPosition, setContentPosition] = useState(null); // Content position se utiliza para traquear la posicion para insertar imagenes o cualquier otra cosa desde el editor rapido
  const [featuredImage, setFeaturedImage] = useState('');
  const [imageMappings, setImageMappings] = useState({}); // Actualmente, las imagenes solo se insertan a traves del icono destinado para ello

  const readImage = async (imageFile, isMainImage) => { // Se lee imagen, si es la principal se renderizara, si es del content se añadira a un mapping de imagenes clave - imagen
    if (imageFile && isMainImage) {
      setFeaturedImage(imageFile); // Mostrara la imagen de portada, esta imagen es fundamental y requerida por cada post
      return;
    }
    const imageName = imageFile.name;
    const dotIndex = imageName.lastIndexOf('.');
    const imgMdCode = `\n\n![${imageName.substring(0, dotIndex)}](${imageName})\n\n`; // Inserta la imagen en el content con 2 saltos de linea y el codigo markdown correspondiente
    const newContent = content.substring(0, contentPosition) + imgMdCode + content.substring(contentPosition)
    setContent(newContent);
    setImageMappings(prevImageMapping => ({
      ...prevImageMapping, // Añade al clave valor anterior un nuevo clave valor al añadir una imagen
      [imageName]: imageFile
    }));
  }

  const getCategories = async () => {
    try {
      const response = await fetch(`http://localhost:8080/blog/categorie`);
      if (!response.ok) {
        throw new Error(`Error al obtener categorias: ${response.statusText}`);
      }
      const data = await response.json();
      setCategories(data);
      setSelectedCategorie(data[0].id);
    } catch (error) {
      setCategories([]);
    }
  };

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
          "categoryIds": [selectedCategorie], // Esto debe cambiarse
          "authorId": 1 // Esto deberia sacarse del jwt
        }
      );
      if (response.ok) { // Si el post se sube correctamente, envia las imagenes a la API para el almacenamiento local
        const images = [featuredImage];
        const imageNames = [`mainImage.webp`];
        const imageMDRegex = /!\[.*?\]\((.*?)\)/g; // Regex de patron de imagenes en markdown
        let match;
        while ((match = imageMDRegex.exec(content)) !== null) {
          images.push(imageMappings[match[1]]); // match[1], nombre de imagen
          imageNames.push(match[1]);
        }
        response = await uploadImages(token, slug, images, imageNames);
        // console.log("Subida efectiva de POST")
        navigate(`/post/${slug}`);
      }
      else {
        console.log("Error en subida de post: ", response)
      }
    } catch (error) {
      console.error(error);
    }
  }

  useEffect(() => { // Se verifica si se esta logueado actualmente y se redirige al panel de administracion
    getCategories();
  }, [])

  return (
    <form onSubmit={handleSendPost}>
      <input type="text" placeholder="Titulo" value={title} onChange={(e) => { setTitle(e.target.value) }}></input>
      <input type="text" placeholder="Slug" value={slug} onChange={(e) => { setSlug(e.target.value) }}></input>
      <input type="text" placeholder="Resumen" value={excerpt} onChange={(e) => { setExcerpt(e.target.value) }}></input>
      <div className="editor-wrapper">
        <div className="editor-toolbar">
          <label>
            <img src="imgIcon.svg" width="30" height="30" />
            <input id="image-input" type="file" accept="image/png, image/jpeg, image/webp" placeholder="Imagen" disabled={contentPosition === null} onChange={(e) => { readImage(e.target.files[0], false) }} style={{ "display": "none" }} />
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
      <div>
        <p>Categoria</p>
        <select onChange={(e) => { setSelectedCategorie(e.target.value) }}>
          {categories.map(categorie => (
            <option key={categorie.id} value={categorie.id}>
              {categorie.name}
            </option>
          ))}
        </select>
      </div>
      <button type="submit">Enviar</button>
    </form>
  )
}

export default Writer