// import { useEffect } from "react";
//
// const AuthCheck = () => {
//     useEffect(() => {
//         fetch("http://localhost:8080/api/protected-resource", {
//             method: "GET",
//             credentials: "include" // Falls du Cookies für Authentifizierung nutzt
//         })
//             .then(response => {
//                 if (response.redirected) {
//                     window.location.href = response.url; // Leitet auf /login weiter
//                 } else if (response.status === 401) {
//                     window.location.href = "http://localhost:8080/login"; // Falls kein Redirect kommt, aber 401
//                 }
//             })
//             .catch(() => {
//                 window.location.href = "http://localhost:8080/login"; // Falls Fehler auftritt
//             });
//     }, []);
//
//     return null;
// };
//
// export default AuthCheck;
