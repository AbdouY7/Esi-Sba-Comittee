// import react from 'react'

import { Route, Routes } from "react-router-dom";
import "./App.css";
import LandingPage from "./LandingPage";
import NotFound from "./NotFound";
import Login from "./authentification_pages/Login";
import Registration from "./authentification_pages/Registration";
import ForgetPassword from "./authentification_pages/ForgetPassword";

function App() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="*" element={<NotFound />} />
      <Route path="login" element={<Login />} />
      <Route path="registration" element={<Registration />} />
      <Route path="forget_password" element={<ForgetPassword />} />
    </Routes>
  );
}

export default App;
