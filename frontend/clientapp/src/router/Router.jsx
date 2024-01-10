import React from "react";
import { BrowserRouter, Navigate, Routes, Route } from "react-router-dom";

import Home from "../pages/home/Home";
import Login from "../pages/login/Login";
import Search from "../pages/search/Search";
import Post from "../pages/post/Post";
import NotFound from "../pages/notfound/NotFound";
import Writer from "../pages/writer/Writer";

import DependsLogged from "./DependsLogged";

export const Router = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<Home />} />
                <Route exact path="/login" element={<Login />} />
                <Route path="/search" element={<Search />} />
                <Route path="/post/:postSlug" element={<Post />} />
                <Route exact path="/notfound" element={<NotFound />} />
                <Route element={<DependsLogged />}>
                    <Route path="/wpannel" element={<Writer />} />
                </Route>
                <Route path="*" element={<Navigate to="/notfound" />} />
            </Routes>
        </BrowserRouter>
    )
}