export const uploadPost = async (token, postData) => {
    try {
        const response = await fetch("http://localhost:8080/blog/post", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(postData)
        });
        return response;
    } catch (error) {
        throw new Error(`Error en la solicitud: ${error}`);
    }
};