import React, { useState, useEffect } from 'react'
import { Outlet } from 'react-router-dom';
import NotFound from '../pages/notfound/NotFound';

const DependsLogged = () => {

    const [tokenValid, setTokenValid] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        TokenValidation();
    }, []);

    const TokenValidation = async () => {
        setIsLoading(true);
        const token = localStorage.getItem("jwt");
        if (!token) setTokenValid(false);
        try {
            const response = await fetch(`http://localhost:8080/auth/valid`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });
            if (!response.ok) {
                throw new Error(`Error validating token: ${response.statusText}`);
            }
            setTokenValid(true);
        } catch (error) {
            console.error(error);
            setTokenValid(false);
        }
        setIsLoading(false);
    };

    return (
        <div>
            {
                isLoading ? (
                    <div>
                        <h2>Cargando...</h2>
                    </div>
                ) : (
                    tokenValid ? (
                        <Outlet />
                    ) :
                        <NotFound />
                )
            }
        </div>
    )
}

export default DependsLogged