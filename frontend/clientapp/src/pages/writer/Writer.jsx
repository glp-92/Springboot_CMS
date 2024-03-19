import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { savePost } from '../../util/requests/SavePost'
import { uploadImages } from '../../util/requests/UploadImages'
import { getCategories } from "../../util/requests/GetCategories";
import { cleanNonBlobFromArr } from "../../util/data/CleanNonBlobFromArr";

const Writer = () => {
  const navigate = useNavigate();
  const postToEdit = useLocation().state;
  const [title, setTitle] = useState(postToEdit ? postToEdit.title : '');
  const [slug, setSlug] = useState(postToEdit ? postToEdit.slug : '');
  const [excerpt, setExcerpt] = useState(postToEdit ? postToEdit.excerpt : '');
  const [content, setContent] = useState(postToEdit ? postToEdit.content : '');
  const [categories, setCategories] = useState([]);
  const [selectedCategorie, setSelectedCategorie] = useState(0);
  const [contentPosition, setContentPosition] = useState(null); // To track the pointer on content
  const [featuredImage, setFeaturedImage] = useState('');
  const [imageMappings, setImageMappings] = useState({});
  const [errorOnSend, setErrorOnSend] = useState(false);

  const readImage = async (imageFile, isMainImage) => {
    /*
      Read imageFile as a blob, if from the content, key - image pair will be added to imageMappings
    */
    if (imageFile && isMainImage) {
      setFeaturedImage(imageFile);
      return;
    }
    const imageName = imageFile.name;
    const dotIndex = imageName.lastIndexOf('.');
    const imgMdCode = `\n\n![${imageName.substring(0, dotIndex)}](${imageName})\n\n`; // Inserta la imagen en el content con 2 saltos de linea y el codigo markdown correspondiente
    const newContent = content.substring(0, contentPosition) + imgMdCode + content.substring(contentPosition)
    setContent(newContent);
    setImageMappings(prevImageMapping => ({
      ...prevImageMapping,
      [imageName]: imageFile
    }));
  }

  const handleSendPost = async (e) => {
    /*
      Send the post to the backend, it's divided on 2 requests
      1. Send the post content to the backend including path of images to store
        1.1. If editing, is not needed to send the slug or the authorId
        1.2. If new post, is needed to send the slug and the authorId
      2. Send the images to the backend to store them on filesystem if there's at least one blob
        2.1. Searches for ![]() string on 'content' and extracts the imageName, then, with the mappings, push the blob associated with the imageName to images
        2.2. If editing, maybe featuredImage won't be changed so backend won't override it, so cleanNonBlobFromArr eliminates non blob images on images var
    */
    e.preventDefault();
    setErrorOnSend(true);
    if (title.length === 0) return
    if (slug.length === 0) return
    if (excerpt.length === 0) return
    if (content.length === 0) return
    if (categories.length === 0) return
    if (featuredImage.length === 0 && !postToEdit) return

    try {
      const token = localStorage.getItem("jwt");
      const commonData = {
        "title": title,
        "slug": slug,
        "excerpt": excerpt,
        "content": content,
        "featuredImage": 'mainImage.webp',
        "date": null,
        "featuredPost": false,
        "categoryIds": [selectedCategorie],
      }
      const body = postToEdit ? { ...commonData, ...{ "postId": postToEdit.id } } : { ...commonData, ...{ "authorId": 1, "slug": slug } }; 
      const method = postToEdit ? 'PUT' : 'POST';
      let response = await savePost(
        method,
        token,
        body
      );
      if (response.ok) { 
        let images = [featuredImage];
        let imageNames = [`mainImage.webp`];
        const imageMDRegex = /!\[.*?\]\((.*?)\)/g; // regex image pattern
        let match;
        while ((match = imageMDRegex.exec(content)) !== null) {
          images.push(imageMappings[match[1]]); // match[1], image name
          imageNames.push(match[1]);
        }
        images, imageNames = cleanNonBlobFromArr(images, imageNames);
        if (images.length != 0) response = await uploadImages(token, slug, images, imageNames);
        setErrorOnSend(false);
        navigate(`/post/${slug}`);
      }
      else {
        console.log("Error en subida de post: ", response)
      }
    } catch (error) {
      console.error(error);
    }
  }

  useEffect(() => {
    /*
      Get categories from backend, if is editing mode, sets categorie id on combobox
    */
    const fetchCategories = async () => {
      const categories = await getCategories();
      setCategories(categories);
    }
    fetchCategories();
    if (postToEdit) {
      if (postToEdit.categories.length > 0) {
        setSelectedCategorie(postToEdit.categories[0].id);
      }
    }
  }, [])

  return (
    <div>
      <a href="/wpannel">Volver a Panel</a>
      <form onSubmit={handleSendPost}>
        <input type="text" placeholder="Titulo" value={title} onChange={(e) => { setTitle(e.target.value) }}></input>
        {errorOnSend && title.length === 0 && <p className="inputPostError">Titulo vacio</p>}
        <input type="text" placeholder="Slug" disabled={postToEdit} value={slug} onChange={(e) => { setSlug(e.target.value) }}></input>
        {errorOnSend && slug.length === 0 && <p className="inputPostError">Slug vacio</p>}
        <input type="text" placeholder="Resumen" value={excerpt} onChange={(e) => { setExcerpt(e.target.value) }}></input>
        {errorOnSend && excerpt.length === 0 && <p className="inputPostError">Resumen vacio</p>}
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
          {errorOnSend && content.length === 0 && <p className="inputPostError">Contenido vacio</p>}
        </div>
        <input type="file" accept="image/png, image/jpeg, image/webp" placeholder="Imagen" onChange={(e) => { readImage(e.target.files[0], true) }} />
        {featuredImage && <img src={URL.createObjectURL(featuredImage)} />}
        {errorOnSend && featuredImage.length === 0 && <p className="inputPostError">No se ha seleccionado imagen de portada</p>}
        <div>
          <p>Categoria</p>
          <select value={selectedCategorie} onChange={(e) => { setSelectedCategorie(e.target.value) }}>
            {categories.map((categorie) => (
              <option key={categorie.id} value={categorie.id}>
                {categorie.name}
              </option>
            ))}
          </select>
        </div>
        {errorOnSend && categories.length === 0 && <p className="inputPostError">No se ha seleccionado categoria</p>}
        <button type="submit">Enviar</button>
      </form>
    </div>
  )
}

export default Writer