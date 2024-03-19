export const uploadImages = async (token, slug, images, imageNames) => {
    console.log(images, imageNames);
    const formData = new FormData();

    let imageNameList = "";
    for (let i = 0; i < images.length; i++) {
        const image = images[i];
        // Añadir cada imagen al FormData con su respectivo nombre
        formData.append("imageList", image, imageNames[i]);
        imageNameList += `${slug}/${imageNames[i]},`;
    }
    imageNameList = imageNameList.substring(0, imageNameList.length - 1);
    formData.append('imagenameList', imageNameList);

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