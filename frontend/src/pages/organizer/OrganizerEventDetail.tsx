import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Event} from "../../types/Event";
import {Card, CardContent, Typography, Button} from "@mui/material";
import {CalendarMonth, AccessTime, LocationOn, Search} from "@mui/icons-material";
import "../../styles/EventDetail.css";

const OrganizerEventDetail = () => {
    const {organizerId, eventId} = useParams();
    const [event, setEvent] = useState<Event | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!eventId || !organizerId) {
            setError("Keine Event-Id oder Organizer-Id angegeben.");
            setLoading(false);
            return;
        }

        axios
            .get<Event>(`http://localhost:8080/organizer/${organizerId}/events/${eventId}`)
            .then((response) => {
                console.log("Event data:", response.data);

                setEvent(response.data);
                setLoading(false);
            })
            .catch(() => {
                setError("Fehler beim Laden des Events.");
                setLoading(false);
            });
    }, [eventId, organizerId]);

    if (loading) return <div className="loading-message">Lade Event...</div>;
    if (error) return <div className="error-message">{error}</div>;

    return (
        <div className="event-detail-container">
            <Card className="event-detail-card">
                <CardContent>
                    <div className="event-detail-title">
                        {event?.title}
                    </div>

                    {/* Datum und Uhrzeit */}
                    <div className="event-section-title">Datum und Uhrzeit</div>
                    <div className="event-info">
                        <CalendarMonth className="icon"/>
                        <span>{new Date(event?.startDate).toLocaleDateString()} bis {new Date(event?.endDate).toLocaleDateString()}</span>
                    </div>

                    {/* Standort und Anzahl der Stellplätze */}
                    <div className="event-section-title">Standort</div>
                    <div className="event-info">
                        <LocationOn className="icon"/>
                        <span>{event?.location}</span>
                    </div>

                    <div className="event-section-title">Anzahl der Standplätze</div>
                    <div className="event-info">
                        <Search className="icon"/>
                        <span>{event?.booths}</span>
                    </div>

                    {/* Beschreibung */}
                    <div className="event-section-title">Beschreibung</div>
                    <Typography className="event-description">
                        {event?.description}
                    </Typography>

                    {/* Bearbeiten-Button */}
                    <Button variant="contained" fullWidth className="edit-event-detail-button"
                            component={Link}
                            to={`/organizer/${organizerId}/events/edit/${eventId}`}
                    >
                        Bearbeite Veranstaltung
                    </Button>
                </CardContent>
            </Card>
        </div>
    );
};

export default OrganizerEventDetail;
