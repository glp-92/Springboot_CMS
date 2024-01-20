export const uploadImages = async (token, slug, featuredImage) => {
    const formData = new FormData();
    formData.append('imageList', featuredImage, 'featuredImage');
    formData.append('imagenameList', slug);

    try {
        const response = await fetch(`http://localhost:8080/blog/post/${slug}/image-upload`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            },
            body: formData,
        });
        return response;
    } catch (error) {
        throw new Error(`Error en la solicitud: ${error}`);
    }
};