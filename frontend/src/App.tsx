import './App.css'
import {BrowserRouter as Router} from "react-router-dom";

import AppRoutes from "./routes/AppRoutes.tsx";
import AuthCheck from "./components/AuthCheck.tsx";


const App = () => {

    return (
        <Router>
            <AppRoutes/>
            <AuthCheck/>
        </Router>

    );
};

export default App
