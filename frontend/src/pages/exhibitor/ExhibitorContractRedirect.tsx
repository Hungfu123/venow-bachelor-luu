import { useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

const ExhibitorContractRedirect = () => {
    const { exhibitorId, applicationId, contractId } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if (!exhibitorId || !applicationId || !contractId) return;

        axios
            .get(`http://localhost:8080/exhibitor/${exhibitorId}/applications/${applicationId}/contracts/overview-detail/${contractId}`)
            .then((response) => {
                const redirectUrl = response.data.redirectUrl;
                if (redirectUrl) {
                    navigate(redirectUrl, { replace: true });
                }
            })
            .catch((error) => {
                console.error("Fehler beim Abrufen des Redirect-Links:", error);
            });
    }, [exhibitorId, applicationId, contractId, navigate]);

    return <div className="loading-message">Leite weiter...</div>;
};

export default ExhibitorContractRedirect;
