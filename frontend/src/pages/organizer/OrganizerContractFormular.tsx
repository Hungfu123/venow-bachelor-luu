import { useEffect, useState } from "react";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import { Application } from "../../types/Application";
import {
    Card,
    CardContent,
    Typography,
    TextField,
    Checkbox,
    FormControlLabel,
} from "@mui/material";
import "../../styles/ContractForm.css";

const OrganizerContractFormular = () => {
    const { organizerId, applicationId } = useParams();
    const navigate = useNavigate();

    const [application, setApplication] = useState<Application | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [price, setPrice] = useState<number>();
    const [standplatznummer, setStandplatznummer] = useState<string>("");
    const [agreed, setAgreed] = useState<boolean>(false);

    useEffect(() => {
        if (!organizerId || !applicationId) {
            setError("Fehlende Parameter (organizerId oder applicationId)");
            setLoading(false);
            return;
        }

        axios
            .get<Application>(
                `http://localhost:8080/organizer/${organizerId}/applications/${applicationId}`, {
                    withCredentials: true
                }
            )
            .then((response) => {
                setApplication(response.data);
                setLoading(false);
            })
            .catch(() => {
                setError("Fehler beim Laden der Bewerbungsdaten.");
                setLoading(false);
            });
    }, [organizerId, applicationId]);

    if (loading) return <div className="loading-message">Lade Daten…</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (!application) return <div>Keine Daten gefunden.</div>;

    const handleCreateContract = () => {
        if (!agreed) {
            alert("Bitte bestätige die AGB, bevor du den Vertrag erstellst!");
            return;
        }
        axios
            .post(
                `http://localhost:8080/organizer/${organizerId}/contracts/${applicationId}`,
                {
                    price: parseFloat(price),
                    boothNumber: standplatznummer,
                }, {
                    withCredentials: true
                }
            )
            .then(() => {
            axios.post(
                `http://localhost:8080/organizer/${organizerId}/applications/${applicationId}/accept`, {
                    withCredentials: true
                }
            )
        })
            .then(() => {
                navigate(`/organizer/${organizerId}/applications`);
                alert("Vertrag erfolgreich erstellt!");
            })
            .catch(() => {
                alert("Fehler beim Erstellen des Vertrags.");
            });
    };

    return (
        <div className="contract-form-container">
            <Card className="contract-card">
                <CardContent>
                    <Typography variant="h5" className="contract-title">
                        Vertrag
                    </Typography>

                    {/* --- Bewerbungsdetails (Application Details) --- */}
                    <section className="contract-section">
                        <Typography variant="h6" className="section-header">
                            Bewerbungsdetails (Application Details)
                        </Typography>
                        <div className="detail-row">
                            <strong>Bewerbername:</strong>
                            <span>{application.exhibitorName}</span>
                        </div>
                        {/*<div className="detail-row">*/}
                        {/*    <strong>Date of Application:</strong>*/}
                        {/*    <span>/!* Falls du ein date-Feld hast – hier Placeholder *!/15.10.2023</span>*/}
                        {/*</div>*/}
                        <div className="detail-row">
                            <strong>Name der Veranstaltung:</strong>
                            <span>{application.eventTitle}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Requested Standplatz:</strong>
                            <span>{application.typeOfBusiness}</span>
                        </div>
                    </section>

                    {/* --- Applicant Information --- */}
                    <section className="contract-section">
                        <Typography variant="h6" className="section-header">
                            Bewerber Informationen
                        </Typography>
                        <div className="detail-row">
                            <strong>Kontaktinformationen:</strong>
                            <span>{application.contactInformation}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Unternehmen:</strong>
                            <span>{application.businessName}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Unternehmensart:</strong>
                            <span>{application.typeOfBusiness}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Schon daran teilgenommen?:</strong>
                            <span>{application.previousParticipation ? "Yes" : "No"}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Nachricht:</strong>
                            <span>{application.message}</span>
                        </div>
                    </section>

                    {/* --- Infos vom Organisator (Dummy) --- */}
                    <section className="contract-section">
                        <Typography variant="h6" className="section-header">
                            Veranstalter Information
                        </Typography>
                        {/*<div className="detail-row">*/}
                        {/*    <strong>Name d. Organisator:</strong>*/}
                        {/*    <span>Event Co.</span>*/}
                        {/*</div>*/}
                        <div className="detail-row">
                            <strong>Organizer Email:</strong>
                            <span>contact@eventco.com</span>
                        </div>
                        {/*<div className="detail-row">*/}
                        {/*    <strong>Organizer Address:</strong>*/}
                        {/*    <span>123 Event St, Cityville</span>*/}
                        {/*</div>*/}
                    </section>

                    {/* --- Preis, Standplatznummer --- */}
                    <section className="contract-section">
                        <TextField
                            label="Preis"
                            variant="outlined"
                            fullWidth
                            value={price}
                            onChange={(e) => setPrice(e.target.value)}
                            placeholder="Enter agreed price"
                            style={{ marginBottom: "1rem" }}
                        />

                        <TextField
                            label="Standplatznummer"
                            variant="outlined"
                            fullWidth
                            value={standplatznummer}
                            onChange={(e) => setStandplatznummer(e.target.value)}
                            placeholder="Standplatznummer"
                        />
                    </section>

                    {/* --- Checkbox --- */}
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={agreed}
                                onChange={(e) => setAgreed(e.target.checked)}
                                color="primary"
                            />
                        }
                        label="I have read and agree to the terms and conditions"
                    />

                    {/* --- Button zum Erstellen des Vertrags --- */}
                    <button
                        variant="contained"
                        fullWidth
                        className="create-contract-button"
                        onClick={handleCreateContract}
                    >
                        Erstelle Vertrag
                    </button>
                </CardContent>
            </Card>
        </div>
    );
};

export default OrganizerContractFormular;
