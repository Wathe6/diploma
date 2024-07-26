import React from "react";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import AdminPage from "./pages/AdminPage.jsx";
import DefaultPage from "./pages/DefaultPage.jsx";

function App() {
    return(
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<DefaultPage/>}/>
                <Route path="/admin" element={<AdminPage />} />
            </Routes>
        </BrowserRouter>
    )
}
export default App;
