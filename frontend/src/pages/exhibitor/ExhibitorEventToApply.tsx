import {useNavigate, useParams} from "react-router-dom";
import { useGet } from "../../hooks/useGet";
import { useState } from "react";
import axios from "axios";
import { Card, CardContent, Button, TextField, RadioGroup, FormControlLabel, Radio } from "@mui/material";
import "../../styles/ApplicationForm.css";
import { Event } from "../../types/Event";

const ExhibitorEventToApply = () => {
    const { exhibitorId, eventId } = useParams();

    const { data: event, loading, error } = useGet<Event>(
        exhibitorId && eventId ? `http://localhost:8080/exhibitor/${exhibitorId}/events/${eventId}/application` : null
    );

    // Formularstatus
    const [contactInformation, setContactInformation] = useState("");
    const [businessName, setBusinessName] = useState("");
    const [typeOfBusiness, setTypeOfBusiness] = useState("");
    const [previousParticipation, setPreviousParticipation] = useState(false);
    const [message, setMessage] = useState("");
    const [submissionStatus, setSubmissionStatus] = useState("");
    const navigate = useNavigate();
    const handleSubmit = async () => {
        if (!exhibitorId || !eventId) return;

        try {
            const applicationData = {
                userId: exhibitorId,
                eventId: eventId,
                contactInformation,
                businessName,
                typeOfBusiness,
                previousParticipation,
                message,
                status: "IN_PROGRESS",
            };

            await axios.post(`http://localhost:8080/exhibitor/${exhibitorId}/events/${eventId}/application`, applicationData);

            setSubmissionStatus("Bewerbung erfolgreich eingereicht!");
            alert("Bewerbung wurde erfolgreich abgeschickt")
            navigate(`/exhibitor/${exhibitorId}/applications`);
        } catch (error) {
            console.error("Fehler beim Absenden der Bewerbung:", error);
            setSubmissionStatus("Fehler beim Einreichen der Bewerbung.");
            alert("Bewerbung wurde hatte einen Fehler")

        }
    };

    if (loading) return <div className="loading-message">Lade Event...</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (!event) return <div>Event nicht gefunden.</div>;

    return (
        <div className="application-container">
            <Card className="application-card">
                <CardContent>
                    <div variant="h5" className="application-title">
                        Bewerbungsdetails
                    </div>

                    {/* Bewerberinformationen */}
                    <div className="application-info">
                        <div><strong>Bewerber-ID:</strong> {exhibitorId}</div>
                        <div><strong>Veranstaltung:</strong> {event.title}</div>
                        <div><strong>Gewünschter Standplatz:</strong> Food Truck</div>
                    </div>

                    {/* Formular zur Bewerbung */}
                    <div variant="h6" className="application-subtitle">Bewerbungsinformationen</div>

                    <TextField
                        label="Kontaktinformationen"
                        fullWidth
                        margin="normal"
                        value={contactInformation}
                        onChange={(e) => setContactInformation(e.target.value)}
                        className="text-field"
                    />
                    <TextField
                        label="Firmenname"
                        fullWidth
                        margin="normal"
                        value={businessName}
                        onChange={(e) => setBusinessName(e.target.value)}
                        className="text-field"
                    />
                    <TextField
                        label="Art des Geschäfts"
                        fullWidth
                        margin="normal"
                        value={typeOfBusiness}
                        onChange={(e) => setTypeOfBusiness(e.target.value)}
                        className="text-field"
                    />

                    <div className="application-subtitle">Frühere Teilnahme:</div>
                    <RadioGroup row value={previousParticipation ? "yes" : "no"} onChange={(e) => setPreviousParticipation(e.target.value === "yes")} className="radio-group">
                        <FormControlLabel value="yes" control={<Radio />} label="Ja" />
                        <FormControlLabel value="no" control={<Radio />} label="Nein" />
                    </RadioGroup>

                    <TextField
                        label="Besondere Anforderungen"
                        fullWidth
                        margin="normal"
                        className="text-field"
                    />

                    <div className="application-subtitle">Nachricht:</div>
                    <TextField
                        multiline
                        rows={4}
                        fullWidth
                        margin="normal"
                        placeholder="Geben Sie eine Nachricht ein..."
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        className="text-field"
                    />

                    {/* Bewerben-Button */}
                    <Button variant="contained" fullWidth className="apply-button" onClick={handleSubmit}>
                        Bewerben
                    </Button>

                    {/* Erfolgs- oder Fehlermeldung */}
                    {submissionStatus && <div className="submission-status">{submissionStatus}</div>}
                </CardContent>
            </Card>
        </div>
    );
};

export default ExhibitorEventToApply;
