export const getCategories = async () => {
    let categories = [];
    try {
        const response = await fetch(`http://localhost:8080/blog/categorie`);
        if (!response.ok) {
            throw new Error(`Error al obtener categorias: ${response.statusText}`);
        }
        categories = await response.json();
    } catch (error) {
        console.log(`Error on fetch Categories! ${error}`)
    }
    return categories;
};