import {useNavigate, useParams} from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { Application } from "../../types/Application";
import { Card, CardContent } from "@mui/material";
import "../../styles/ApplicationsDetail.css";

const ExhibitorApplicationView  = () => {
    const {exhibitorId, applicationId} = useParams();
    const [application, setApplication] = useState<Application | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [ setErrorMessage] = useState<string | null>(null);
    const navigate = useNavigate();
    useEffect(() => {
        if (!exhibitorId || !applicationId) {
            setError("Fehlende Parameter (OrganizerId oder ApplicationId).");
            setLoading(false);
            return;
        }

        axios
            .get<Application>(
                `http://localhost:8080/exhibitor/${exhibitorId}/applications/${applicationId}`, {
                    withCredentials: true
                }
            )
            .then((response) => {
                setApplication(response.data);
                setLoading(false);
            })
            .catch(() => {
                setError("Fehler beim Laden der Bewerbungsdetails.");
                setLoading(false);
            });
    }, [exhibitorId, applicationId]);

    if (loading) return <div className="loading-message">Lade Bewerbungsdetails…</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (!application) return <div>Keine Daten vorhanden.</div>;

    const handleCreateContract = () => {
        navigate(`/exhibitor/${exhibitorId}/contracts/${applicationId}`)
    };

    const handleReject = () => {
        axios
            .post(`http://localhost:8080/exhibitor/${exhibitorId}/applications/${applicationId}/reject`)
            .then(() => {

                setErrorMessage(null);

                navigate(`/exhibitor/${exhibitorId}/applications`);
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
    }
    return (
        <div className="application-detail-container">
            <Card className="application-detail-card">
                <CardContent>
                    <div variant="h5" className="detail-title">
                        Schausteller Bewerbungs - Ansicht
                    </div>

                    {/* Bewerbungsdetails */}
                    <section className="detail-section">
                        <div variant="h6" className="section-header">
                            Bewerbungsdetails
                        </div>
                        <div className="detail-row">
                            <strong>Bewerber Name:</strong>
                            <span>{application.exhibitorName}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Veranstaltungs Titel:</strong>
                            <span>{application.eventTitle}</span>
                        </div>
                        {/* Falls du ein Date-Feld hast, analog:
            <div className="detail-row">
              <strong>Date of Application:</strong>
              <span>{formatDate(application.createdAt)}</span>
            </div>
            */}
                        <div className="detail-row">
                            <strong>Angefragter Standplatz</strong>
                            <span>
                                {application.typeOfBusiness}
                            </span>
                        </div>
                    </section>

                    {/* Applicant Information */}
                    <section className="detail-section">
                        <div variant="h6" className="section-header">
                            Bewerber Informationen
                        </div>
                        <div className="detail-row">
                            <strong>Kontakt</strong>
                            <span>{application.contactInformation}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Unternehmen:</strong>
                            <span>{application.businessName}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Art des Geschäftes:</strong>
                            <span>{application.typeOfBusiness}</span>
                        </div>
                        {/*<div className="detail-row">*/}
                        {/*    <strong>Previous Participation:</strong>*/}
                        {/*    <span>{application.previousParticipation}</span>*/}
                        {/*</div>*/}
                        {/*<div className="detail-row">*/}
                        {/*    <strong>Special Requirements:</strong>*/}
                        {/*    <span>/!* z. B. "Requires electricity" – falls dein Interface es hergibt *!/</span>*/}
                        {/*</div>*/}
                    </section>

                    {/* Nachricht */}
                    <section className="detail-section">
                        <div variant="h6" className="section-header">
                            Nachricht
                        </div>
                        <div className="detail-message">
                            {application.message}
                        </div>
                    </section>


                </CardContent>
            </Card>
        </div>
    );
};

export default ExhibitorApplicationView;
