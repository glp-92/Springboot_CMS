import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Home from "../pages/home/Home";
import Login from "../pages/login/Login";
import Search from "../pages/search/Search";
import Post from "../pages/post/Post";

export const Router = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<Home/>} />
                <Route exact path="/login" element={<Login/>} />
                <Route path="/search" element={<Search/>} />
                <Route path="/post/:idPost" element={<Post/>} />
                <Route path="*" element={<div>Not Found</div>} />
            </Routes>
        </BrowserRouter>
    )
}