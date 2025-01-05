import { Link, Outlet, useParams } from "react-router-dom";
import "../styles/layout.css";

const OrganizerLayout = () => {
    const { organizerId } = useParams();

    return (
        <div className="layout-container">
            {/* Sidebar */}
            <aside id="default-sidebar" aria-label="Sidebar">
                <div>
                    <h2>Venow</h2>
                    <ul>
                        <li className="group">
                            <Link
                                to={`/organizer/${organizerId}/events`}
                                className="sidebar-link"
                            >
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    fill="currentColor"
                                    viewBox="0 0 22 21"
                                    className="sidebar-icon"
                                >
                                    <path
                                        d="M16.975 11H10V4.025a1 1 0 0 0-1.066-.998 8.5 8.5 0 1 0 9.039 9.039.999.999 0 0 0-1-1.066h.002Z"/>
                                    <path
                                        d="M12.5 0c-.157 0-.311.01-.565.027A1 1 0 0 0 11 1.02V10h8.975a1 1 0 0 0 1-.935c.013-.188.028-.374.028-.565A8.51 8.51 0 0 0 12.5 0Z"/>
                                </svg>
                                <span>Meine Events</span>
                            </Link>
                        </li>
                        <li className="group">
                            <Link
                                to={`/organizer/${organizerId}/events/create`}
                                className="sidebar-link"
                            >
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    fill="currentColor"
                                    viewBox="0 0 18 18"
                                    className="sidebar-icon"
                                >
                                    <path
                                        stroke="currentColor"
                                        strokeWidth="2"
                                        d="M9 1v16M1 9h16"
                                    />
                                </svg>
                                <span>Create Event</span>
                            </Link>
                        </li>
                    </ul>
                </div>
            </aside>

            {/* Header */}
            <header>
                <span>Welcome, Organizer</span>
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

            {/* HR Line */}
            <hr className="header-hr"/>

            {/* Main Content */}
            <div className="main-content">
                <main>
                    <Outlet/>
                </main>
            </div>
        </div>

    );
};

export default OrganizerLayout;
