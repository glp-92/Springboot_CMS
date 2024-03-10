import React, { useState, useEffect } from 'react'
import Pagination from '@mui/material/Pagination';
import { GetPostList } from '../../hooks/GetPostList';
import { useNavigate } from "react-router-dom";

const AdminPannel = () => {
    const navigate = useNavigate();
    const [posts, setPosts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [inputCategorie, setInputCategorie] = useState('');
    const [page, setPage] = useState(0);
    const [npages, setNPages] = useState(0);

    const editPost = (id) => {
        console.log(id);
    }

    const handleDeletePost = (id) => {
        const deletePost = async () => {
            const token = localStorage.getItem("jwt");
            if (!token) return false;
            try {
                const response = await fetch(`http://localhost:8080/blog/post/${id}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });
                if (!response.ok) {
                    throw new Error(`Error validating token: ${response.statusText}`);
                }
            } catch (error) {
                console.log("Post no eliminado");
            }
        };
        if (confirm('Esta accion borrara el Post de la base de datos, continuar?')) {
            deletePost();
            window.location.reload();
        }
    }

    const createNewPost = () => {
        navigate(`/wpannel/writer`);
    }

    const handlePageChange = (event, value) => {
        setPage(value - 1);
    }

    const handleCreateCategorie = () => {
        const createCategorie = async () => {
            const token = localStorage.getItem("jwt");
            if (!token) return false;
            try {
                const newCategorie = inputCategorie.toLowerCase();
                const payload = {
                    name: newCategorie,
                    slug: newCategorie
                };
                const response = await fetch("http://localhost:8080/blog/categorie", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`
                    },
                    body: JSON.stringify(payload)
                });
                if (!response.ok) {
                    throw new Error(`Error validating token: ${response.statusText}`);
                }
            } catch (error) {
                console.log(`Error en creacion de categoria!`, error);
                return false;
            }
            return true;
        }
        const success = createCategorie();
        if (success) {
            window.location.reload();
        }
    }

    const handleEditCategorie = (newName, index) => {
        const newCategories = [...categories];
        newCategories[index]["name"] = newName;
        newCategories[index]["slug"] = newName;
        setCategories(newCategories);
    }

    const handleUpdateCategorie = (index) => {
        const updateCategorie = async (categorie) => {
            const token = localStorage.getItem("jwt");
            if (!token) return false;
            try {
                const response = await fetch(`http://localhost:8080/blog/categorie`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`
                    },
                    body: JSON.stringify(categorie)
                });
                if (!response.ok) {
                    throw new Error(`Error validating token: ${response.statusText}`);
                }
            } catch (error) {
                console.log("Error. Categorie not updated!", error);
            }
        };
        updateCategorie(categories[index]);
    }

    const handleDeleteCategorie = (id, index) => {
        const deletePost = async () => {
            const token = localStorage.getItem("jwt");
            if (!token) return false;
            try {
                const response = await fetch(`http://localhost:8080/blog/categorie/${id}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });
                if (!response.ok) {
                    throw new Error(`Error validating token: ${response.statusText}`);
                }
            } catch (error) {
                console.log("Categoria no eliminada!");
                return false;
            }
            return true;
        };
        if (confirm('Esta accion borrara la Categoria de la base de datos, continuar?')) {
            const success = deletePost();
            if (success) {
                const newCategories = [...categories];
                setCategories(newCategories.slice(0, index));
            }
        }
    }

    useEffect(() => {
        const fetchPosts = async (page) => {
            const posts = await GetPostList(page, null);
            if (posts != null) {
                setPosts(posts["content"]);
                setNPages(posts["totalPages"])
                console.log(posts);
            }
            else {
                setPosts([]);
            }
        }
        const fetchCategories = async () => {
            try {
                const response = await fetch(`http://localhost:8080/blog/categorie`)
                if (!response.ok) {
                    throw new Error(`Error fetching categories`);
                }
                setCategories(await response.json());
            } catch (error) {
                console.log(error);
            }
        }
        fetchCategories();
        fetchPosts(page);
    }, [page])

    return (
        <div>
            <h1>Panel de administracion</h1>
            <button onClick={createNewPost}>Crear nuevo Post</button>
            <div>
                <h2>Categorias</h2>
                <input type='text' placeholder='Nombre de categoria' value={inputCategorie} onChange={(e) => { setInputCategorie(e.target.value) }} />
                <button onClick={handleCreateCategorie}>Crear Categoria</button>
                <ul>
                    {categories.map((categorie, index) => (
                        <li key={index}>
                            <p contenteditable="true" onBlur={e => {
                                handleEditCategorie(e.currentTarget.textContent, index);
                            }}>{categorie["name"]}</p>
                            <button onClick={() => handleDeleteCategorie(categorie["id"], index)}>Eliminar</button>
                            <button onClick={() => handleUpdateCategorie(index)}>Actualizar</button>
                        </li>
                    ))}
                </ul>
            </div>
            <div>
                <h2>Editar contenido</h2>
                {posts.map(item => (
                    <div key={item["id"]}>
                        <p>{item["title"]}</p>
                        <button onClick={() => editPost(item["id"])}>Editar</button>
                        <button onClick={() => handleDeletePost(item["id"])}>Eliminar</button>
                    </div>
                ))}
                <Pagination count={npages} page={page} onChange={handlePageChange} />
            </div>
        </div>
    )


}

export default AdminPannel