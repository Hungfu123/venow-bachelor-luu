import "../../styles/GridEvent.css";
import {ReactComponent as EditPenIcon} from "../../assets/edit-pen-icon.svg";
import {Link} from "react-router-dom";

const OrganizerEvents = () => {
    const events = Array(8).fill({
        id: "123198231-sdfws-23423",
        title: "Tech Konferenz 2023",
        organizer: "Max Mustermann",
        date: "15. März 2023",
        location: "Berlin",
        spots: 5,
    });

    return (
        <div className="events-container">
            <h1 className="events-title">Veranstaltungsübersicht</h1>
            <div className="grid-container">
                {events.map((event) => (
                    <Link
                        to={`/events/${event.id}`} // Dynamische Route basierend auf der Event-ID
                        key={event.id}
                        className="event-card bg-white rounded-lg shadow-md border border-gray-300 p-4 transition-transform duration-300 ease-in-out hover:shadow-lg hover:-translate-y-1 cursor-pointer"
                    >
                        <div className="event-header flex justify-between items-center">
                            <h2 className="text-lg font-bold text-gray-800">{event.title}</h2>
                            <Link to={`/edit/${event.id}`} onClick={(e) => e.stopPropagation()}>
                                <EditPenIcon className="edit-button"/>
                            </Link>
                        </div>
                        <div className="eventOrganizerName mt-2 text-gray-600">
                            <p>{event.organizer}</p>
                        </div>
                        <div className="event-details mt-4 text-gray-600">
                            <div className="flex justify-between">
                                <strong>Datum:</strong>
                                <span>{event.date}</span>
                            </div>
                            <div className="flex justify-between">
                                <strong>Veranstaltungsort:</strong>
                                <span>{event.location}</span>
                            </div>
                            <div className="flex justify-between">
                                <strong>Standplätze:</strong>
                                <span>{event.spots}</span>
                            </div>
                        </div>
                        <div className="event-footer">
                            <Link
                                to={`/events/${event.id}/applications`}
                                className="view-button"
                                onClick={(e) => e.stopPropagation()} // Verhindert Navigation der Karte
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
