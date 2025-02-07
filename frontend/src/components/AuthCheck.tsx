import { useEffect } from "react";

const AuthCheck = () => {
    useEffect(() => {
        fetch("http://localhost:8080/health", {
            method: "GET",
            credentials: "include"
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else if (response.status === 401) {
                    window.location.href = "http://localhost:8080/login";
                }
            })
            .catch(() => {
                window.location.href = "http://localhost:8080/login";
            });
    }, []);

    return null;
};

export default AuthCheck;
