import { Link, Outlet, useParams, useLocation } from "react-router-dom";
import "../styles/layout.css";
import { ReactComponent as EventsIcon } from "../assets/events-icon.svg";
import { ReactComponent as ApplicationsIcon } from "../assets/applications-icon.svg";
import { ReactComponent as ContractsIcon } from "../assets/contracts-icon.svg";
import { ReactComponent as LogoutIcon } from "../assets/profile-icon.svg";

const ExhibitorLayout = () => {
    // exhibitorId aus der URL
    const { exhibitorId } = useParams();
    const location = useLocation();

    return (
        <div className="layout-container">
            {/* Sidebar */}
            <aside id="default-sidebar">
                <div className="sidebar-content">
                    <h2>Venow</h2>
                    <ul>
                        <li className="group">
                            <Link
                                to={`/exhibitor/${exhibitorId}/events`}
                                className={`sidebar-link ${
                                    location.pathname === `/exhibitor/${exhibitorId}/events` ? "active" : ""
                                }`}
                            >
                                <EventsIcon className="sidebar-icon" />
                                <span>Alle Events</span>
                            </Link>
                        </li>
                        <li className="group">
                            <Link
                                to={`/exhibitor/${exhibitorId}/applications`}
                                className={`sidebar-link ${
                                    location.pathname === `/exhibitor/${exhibitorId}/applications` ? "active" : ""
                                }`}
                            >
                                <ApplicationsIcon className="sidebar-icon" />
                                <span>Bewerbungen</span>
                            </Link>
                        </li>
                        <li className="group">
                            <Link
                                to={`/exhibitor/${exhibitorId}/contracts`}
                                className={`sidebar-link ${
                                    location.pathname === `/exhibitor/${exhibitorId}/contracts` ? "active" : ""
                                }`}
                            >
                                <ContractsIcon className="sidebar-icon" />
                                <span>Meine Verträge</span>
                            </Link>
                        </li>
                    </ul>
                    <div className="logout">
                        <Link to="/logout" className="sidebar-link">
                            <LogoutIcon className="sidebar-icon" />
                            <span>Abmelden</span>
                        </Link>
                    </div>
                </div>
            </aside>

            {/* Header */}
            <header>
                <span>Hallo, Exhibitor</span>
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

export default ExhibitorLayout;
