import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import "../../styles/table.css";
import { Application } from "../../types/Application.tsx";

const ExhibitorApplication = () => {
    const { exhibitorId } = useParams();
    const navigate = useNavigate();
    const [applications, setApplications] = useState<Application[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    useEffect(() => {
        if (!exhibitorId) {
            setErrorMessage("Exhibitor ID fehlt.");
            setLoading(false);
            return;
        }

        axios
            .get<Application[]>(`http://localhost:8080/exhibitor/${exhibitorId}/applications`, {
                withCredentials: true
            })
            .then((response) => {
                setApplications(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Fehler beim Abrufen der Bewerbungen:", error);
                setErrorMessage("Fehler beim Laden der Bewerbungen.");
                setLoading(false);
            });
    }, [exhibitorId]);

    const handleRowClick = (applicationId: string) => {
        if (!exhibitorId) return;
        navigate(`/exhibitor/${exhibitorId}/applications/${applicationId}`);
    };

    const handleViewContract = (applicationId: string) => {
        event.stopPropagation(); // Verhindert, dass die Zeile ebenfalls angeklickt wird
        if (!exhibitorId) return;
        navigate(`/exhibitor/${exhibitorId}/applications/${applicationId}/contract`);
    };

    if (loading) return <div className="loading-message">Lade Bewerbungen...</div>;
    if (errorMessage) return <div className="error-message">{errorMessage}</div>;

    return (
        <div className="table-container">
            <h2 className="table-title">Meine Bewerbungen zu den Veranstaltungen</h2>

            <table className="T-table">
                <thead>
                <tr>
                    <th>Titel</th>
                    <th>StandplatzTyp</th>
                    <th>Application Status</th>
                    {/*<th>Aktionen</th>*/}
                </tr>
                </thead>
                <tbody>
                {applications.map((app) => (
                    <tr
                        key={app.applicationId}
                        className="clickable-row"
                        onClick={() => handleRowClick(app.applicationId)}
                    >
                        <td>{app.eventTitle}</td>
                        <td>{app.typeOfBusiness}</td>
                        <td>{app.status}</td>
                        {/*<td>*/}
                        {/*    <div*/}
                        {/*        className="contract-button"*/}
                        {/*        onClick={(event) => {*/}
                        {/*            event.stopPropagation(); // Verhindert, dass die ganze Zeile geklickt wird*/}
                        {/*            handleViewContract(app.applicationId);*/}
                        {/*        }}*/}
                        {/*    >*/}
                        {/*        Vertrag*/}
                        {/*    </div>*/}
                        {/*</td>*/}
                    </tr>
                ))}
                </tbody>

            </table>
        </div>
    );
};

export default ExhibitorApplication;
