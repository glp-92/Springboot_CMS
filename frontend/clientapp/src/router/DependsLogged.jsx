import React, { useState, useEffect } from 'react'
import { Outlet } from 'react-router-dom';
import NotFound from '../pages/notfound/NotFound';
import { ValidateToken } from '../util/requests/ValidateToken';

const DependsLogged = () => {

    const [tokenValid, setTokenValid] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchTokenValid = async () => {
            setIsLoading(true);
            const isValid = await ValidateToken();
            setTokenValid(isValid);
            setIsLoading(false);
        }
        fetchTokenValid();
    }, []);

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