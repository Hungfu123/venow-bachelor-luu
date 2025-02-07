import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { Application } from "../../types/Application.tsx";
import axios from "axios";
import "../../styles/table.css";

const OrganizerApplications = () => {
    const { organizerId } = useParams();
    const navigate = useNavigate();
    const [applications, setApplications] = useState<Application[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const fetchApplications = () => {
        if (!organizerId) {
            console.error("Organizer Id fehlt");
            return;
        }
        axios
            .get<Application[]>(`http://localhost:8080/organizer/${organizerId}/applications`, {
                withCredentials: true
            })
            .then((response) => {
                setApplications(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("Fehler beim Abrufen der Applications", error);
                setErrorMessage("Fehler beim Laden der Applications: " + error.message);
                setLoading(false);
            });
    };

    useEffect(() => {
        fetchApplications();
    }, [organizerId]);

    const handleRowClick = (applicationId: string) => {
        if (!organizerId) return;
        navigate(`/organizer/${organizerId}/applications/${applicationId}`);
    };

    const handleReject = (event: React.MouseEvent, applicationId: string) => {
        event.stopPropagation();
        if (!organizerId) return;

        axios
            .post(`http://localhost:8080/organizer/${organizerId}/applications/${applicationId}/reject`,
                null
                , {
                    withCredentials: true
                })
            .then(() => {
                fetchApplications();
                setErrorMessage(null);
            })
            .catch((error) => {
                if (error.response && error.response.data.includes("Application already rejected")) {
                    setErrorMessage(`Die Bewerbung wurde bereits abgelehnt.`);
                } else {
                    setErrorMessage("Fehler beim Ablehnen der Bewerbung.");
                }

                setTimeout(() => {
                    setErrorMessage(null);
                }, 5000);
            });
    };

    if (loading) return <div>Lade Applications...</div>;

    return (
        <div className="table-container">
            <h1 className="table-title">Bewerbungsübersicht</h1>

            {errorMessage && (
                <div className="error-overlay">
                    {errorMessage}
                </div>
            )}

            <table className="T-table">
                <thead>
                <tr>
                    <th>Bewerbername</th>
                    <th>Titel</th>
                    <th>StandplatzTyp</th>
                    <th>Status</th>
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
                        <td>{app.exhibitorName}</td>
                        <td>{app.eventTitle}</td>
                        <td>{app.typeOfBusiness}</td>
                        <td>{app.status}</td>
                        {/*<td>*/}
                        {/*    /!*<button*!/*/}
                        {/*    /!*    className="reject-button"*!/*/}
                        {/*    /!*    onClick={(event) => handleReject(event, app.applicationId)}*!/*/}
                        {/*    /!*>*!/*/}
                        {/*    /!*    Ablehnen*!/*/}
                        {/*    /!*</button>*!/*/}
                        {/*</td>*/}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default OrganizerApplications;
