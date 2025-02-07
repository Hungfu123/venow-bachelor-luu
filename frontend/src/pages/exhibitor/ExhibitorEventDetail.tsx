import {Link, useParams} from "react-router-dom";
import { useGet } from "../../hooks/useGet";
import { Card, CardContent} from "@mui/material";
import { CalendarMonth, AccessTime, LocationOn, Search } from "@mui/icons-material";
import "../../styles/EventDetail.css";
import {Event} from "../../types/Event.tsx"

const ExhibitorEventDetail = () => {
    const { exhibitorId, eventId } = useParams();

    // Holt die Event-Daten
    const { data: event, loading, error } = useGet<Event>(
        exhibitorId && eventId ? `http://localhost:8080/exhibitor/${exhibitorId}/events/${eventId}` : null, {
            withCredentials: true
        }
    );

    if (loading) return <div className="loading-message">Lade Event...</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (!event) return <div>Event nicht gefunden.</div>;

    return (
        <div className="event-detail-container">
            <Card className="event-detail-card">
                <CardContent>
                    <div
                        variant="h5" className="event-detail-title">
                        {event.title}
                    </div
                    >

                    {/* Datum und Uhrzeit */}
                    <div
                        className="event-section-title">Datum und Uhrzeit</div
                    >
                    <div className="event-info">
                        <CalendarMonth className="icon" />
                        <span>{new Date(event.startDate).toLocaleDateString()} bis {new Date(event.endDate).toLocaleDateString()}</span>
                    </div>
                    <div className="event-info">
                        <AccessTime className="icon" />
                        <span>{event.startTime} bis {event.endTime}</span>
                    </div>

                    {/* Standort und Anzahl der Stellplätze */}
                    <div
                        className="event-section-title">Standort</div
                    >
                    <div className="event-info">
                        <LocationOn className="icon" />
                        <span>{event.location}</span>
                    </div>

                    <div
                        className="event-section-title">Anzahl der Standplätze</div
                    >
                    <div className="event-info">
                        <Search className="icon" />
                        <span>{event.booths}</span>
                    </div>

                    {/* Beschreibung */}
                    <div
                        className="event-section-title">Beschreibung</div
                    >
                    <div
                        className="event-description">{event.description}</div
                    >

                    {/* Bewerben-Button */}
                    <Link
                        to={`/exhibitor/${exhibitorId}/events/${eventId}/application`}
                        className="apply-button" onClick={(e) => e.stopPropagation()}
                    >
                        Bewerbung schicken
                    </Link>
                </CardContent>
            </Card>
        </div>
    );
};

export default ExhibitorEventDetail;
