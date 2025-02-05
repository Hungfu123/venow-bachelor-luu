import { useParams, Link, useNavigate, useLocation } from "react-router-dom";
import { Event } from "../../types/Event.tsx";
import { useGet } from "../../hooks/useGet";
import "../../styles/GridEvent.css";

const ExhibitorEvents = () => {
    const { exhibitorId } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const searchQuery = queryParams.get("query") || "";

    const { data: events, loading, error } = useGet<Event[]>(
        exhibitorId ? `http://localhost:8080/exhibitor/${exhibitorId}/events?query=${searchQuery}` : null
    );

    const handleSearch = () => {
        const inputElement = document.getElementById("search-input") as HTMLInputElement;
        const query = inputElement?.value.trim();
        navigate(`/exhibitor/${exhibitorId}/events${query ? `?query=${query}` : ""}`);
    };

    if (loading) return <div>Lade Events...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div className="grid-events-container">
            <h1 className="grid-events-title">Alle Veranstaltungen</h1>

            {/* Suchleiste mit Button */}
            <div className="search-container">
                <input
                    id="search-input"
                    type="text"
                    placeholder="Veranstaltung suchen..."
                    defaultValue={searchQuery}
                    className="search-input"

                />
                <button className="search-button" onClick={handleSearch}>
                    Suchen
                </button>
            </div>

            {/* Events anzeigen */}
            <div className="grid-container">
                {events.map((event) => (
                    <Link
                        to={`/exhibitor/${exhibitorId}/events/${event.eventId}`}
                        key={event.eventId}
                        className="grid-event-card bg-white rounded-lg shadow-md border border-gray-300 p-4 transition-transform duration-300 ease-in-out hover:shadow-lg hover:-translate-y-1 cursor-pointer"
                    >
                        <div className="grid-event-header flex justify-between items-center">
                            <h2 className="text-lg font-bold text-gray-800">{event.title}</h2>
                        </div>
                        <div className="grid-eventOrganizerName mt-2 text-gray-600">
                            <p>{event.organizerName}</p>
                        </div>
                        <div className="grid-event-details mt-4 text-gray-600">
                            <div className="flex justify-between">
                                <strong>Datum:</strong>
                                <span>
                                    {new Date(event.startDate).toLocaleDateString()} - {new Date(event.endDate).toLocaleDateString()}
                                </span>
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
                                to={`/exhibitor/${exhibitorId}/events/${event.eventId}/application`}
                                className="view-button"
                                onClick={(e) => e.stopPropagation()}
                            >
                                Bewerben
                            </Link>
                        </div>
                    </Link>
                ))}
            </div>
        </div>
    );
};

export default ExhibitorEvents;
