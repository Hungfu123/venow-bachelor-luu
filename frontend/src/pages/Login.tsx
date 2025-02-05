import { useState } from "react";
import "../styles/login.css";

const LoginPage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const response = await fetch("/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password }),
            });

            const data = await response.json();

            if (response.ok) {
                window.location.href = data.redirect;
            } else {
                setError(data.error);
            }
        } catch (err) {
            setError("Login fehlgeschlagen");
        }
    };

    return (
        <div className="login-container">
            {/* Linke Seite */}
            <div className="login-left">
                <h1>Venow</h1>
                <p>Finde dein Event oder veranstalte eins selbst!?</p>
                <p>Jetzt loslegen und vernetzen!</p>
            </div>

            {/* Rechte Seite */}
            <div className="login-right">
                <div className="login-box">
                    <h2>Anmelden</h2>

                    {/* Login-Formular */}
                    <form onSubmit={handleLogin}>
                        <input
                            type="email"
                            placeholder="E-Mail-Adresse"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            className="login-input"
                        />
                        <input
                            type="password"
                            placeholder="Passwort"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            className="login-input"
                        />
                        <button type="submit" className="login-button">
                            Anmelden
                        </button>
                    </form>

                    {/* Fehlermeldung */}
                    {error && <p className="error-message">{error}</p>}

                    {/* Registrierung-Link */}
                    <p className="text">
                        Noch keinen Account?{" "}
                        <a href="/register" className="text-link">
                            Registrieren
                        </a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;
