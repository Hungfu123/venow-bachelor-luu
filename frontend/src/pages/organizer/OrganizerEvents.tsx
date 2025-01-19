import "../../styles/GridEvent.css";
import { ReactComponent as EditPenIcon } from "../../assets/edit-pen-icon.svg";
import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import {Event} from "../../types/Event.tsx";

const OrganizerEvents = () => {
    const { organizerId } = useParams();
    const [events, setEvents] = useState<Event[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!organizerId) {
            console.error("Organizer ID fehlt!");
            return;
        }

        axios
            .get<Event[]>(`http://localhost:8080/organizer/${organizerId}/events`)
            .then((response) => {
                setEvents(response.data);
                setLoading(false);
            })
            .catch((error) => {
                console.error("❌ Fehler beim Abrufen der Events:", error);
                setError("Fehler beim Laden der Events.");
                setLoading(false);
            });
    }, [organizerId]);



    if (loading) return <div>Lade Events...</div>;


    if (error) return <div>{error}</div>;

    return (
        <div className="events-container">
            <h1 className="events-title">Veranstaltungsübersicht</h1>
            <div className="grid-container">
                {events.map((event) => (
                    <Link
                        to={`/organizer/${organizerId}/events/${event.eventId}`} // Dynamische Route basierend auf Organizer-ID und Event-ID
                        key={event.eventId}
                        className="event-card bg-white rounded-lg shadow-md border border-gray-300 p-4 transition-transform duration-300 ease-in-out hover:shadow-lg hover:-translate-y-1 cursor-pointer"
                    >
                        <div className="event-header flex justify-between items-center">
                            <h2 className="text-lg font-bold text-gray-800">{event.title}</h2>
                            <Link
                                to={`/organizer/${organizerId}/events/edit/${event.eventId}`}
                                onClick={(e) => e.stopPropagation()}
                            >
                                <EditPenIcon className="edit-button" />
                            </Link>
                        </div>
                        <div className="eventOrganizerName mt-2 text-gray-600">
                            <p>{event.organizerName}</p>
                        </div>
                        <div className="event-details mt-4 text-gray-600">
                            <div className="flex justify-between">
                                <strong>Datum:</strong>
                                <span>{new Date(event.startDate).toLocaleDateString()} - {new Date(event.endDate).toLocaleDateString()}</span>
                            </div>
                            <div className="flex justify-between">
                                <strong>Veranstaltungsort:</strong>
                                <span>{event.location}</span>
                            </div>
                            <div className="flex justify-between">
                                <strong>Standplätze:</strong>
                                <span>{event.booths}</span>
                            </div>
                        </div>
                        <div className="event-footer">
                            <Link
                                to={`/organizer/${organizerId}/events/${event.eventId}/applications`} // KORREKTE URL für Bewerbungen
                                className="view-button"
                                onClick={(e) => e.stopPropagation()} // Verhindert Karten-Navigation
                            >
                                Bewerbungen ansehen
                            </Link>
                        </div>
                    </Link>
                ))}
            </div>
        </div>
    );
};

export default OrganizerEvents;
