import { Link, Outlet, useParams, useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import "../styles/layout.css";
import { ReactComponent as EventsIcon } from "../assets/events-icon.svg";
import { ReactComponent as ApplicationsIcon } from "../assets/applications-icon.svg";
import { ReactComponent as ContractsIcon } from "../assets/contracts-icon.svg";
import { ReactComponent as LogoutIcon } from "../assets/profile-icon.svg";

const OrganizerLayout
    = () => {
    const { organizerId } = useParams();
    const location = useLocation();

    // Logout-Funktion
    const handleLogout = () => {
        window.location.href = "http://localhost:8080/logout";
    };



    return (
        <div className="layout-container">
            {/* Sidebar */}
            <aside id="default-sidebar">
                <div className="sidebar-content">
                    <h2>Venow</h2>
                    <ul>
                        <li className="group">
                            <Link
                                to={`/organizer/${organizerId}/events`}
                                className={`sidebar-link ${
                                    location.pathname === `/organizer/${organizerId}/events` ? "active" : ""
                                }`}
                            >
                                <EventsIcon className="sidebar-icon" />
                                <span>Meine Events</span>
                            </Link>
                        </li>
                        <li className="group">
                            <Link
                                to={`/organizer/${organizerId}/applications`}
                                className={`sidebar-link ${
                                    location.pathname === `/organizer/${organizerId}/applications` ? "active" : ""
                                }`}
                            >
                                <ApplicationsIcon className="sidebar-icon" />
                                <span>Bewerbungen</span>
                            </Link>
                        </li>
                        <li className="group">
                            <Link
                                to={`/organizer/${organizerId}/contracts`}
                                className={`sidebar-link ${
                                    location.pathname === `/organizer/${organizerId}/contracts` ? "active" : ""
                                }`}
                            >
                                <ContractsIcon className="sidebar-icon" />
                                <span>Verträge</span>
                            </Link>
                        </li>
                    </ul>

                    {/* Logout-Button statt Link */}
                    <div className="logout">
                        <button onClick={handleLogout} className="sidebar-link">
                            <LogoutIcon className="sidebar-icon" />
                            <span>Abmelden</span>
                        </button>
                    </div>
                </div>
            </aside>

            {/* Header */}
            <header>
                <span>Hallo, Organizer</span>
                <Link
                    to={`/organizer/${organizerId}/events/create`}
                    className="add-event-button"
                >
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        fill="currentColor"
                        viewBox="0 0 18 18"
                        className="add-event-icon"
                    >
                        <path
                            stroke="currentColor"
                            strokeWidth="2"
                            d="M9 1v16M1 9h16"
                        />
                    </svg>
                    <span>Veranstaltung</span>
                </Link>
            </header>

            {/* Trennlinie */}
            <hr className="header-hr" />

            {/* Hauptinhalt */}
            <div className="main-content">
                <main>
                    <Outlet />
                </main>
            </div>
        </div>
    );
};

export default OrganizerLayout
;
