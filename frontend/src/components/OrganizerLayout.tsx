import {Link, Outlet, useParams} from "react-router-dom";

const OrganizerLayout = () => {
    const { organizerId } = useParams();

    return (
        <div className="layout">
            <aside className="sidebar">
                <h2>Organizer</h2>
                <nav>
                    <ul>
                        <li>
                            <Link to={`/organizer/${organizerId}/events`}>My Events</Link>
                        </li>
                        <li>
                            <Link to={`/organizer/${organizerId}/events/create`}>Create Event</Link>
                        </li>
                    </ul>
                </nav>
            </aside>
            <main className="main-content">
                <header>
                    <span>Welcome, Organizer</span>
                    <button>Veranstaltung</button>
                </header>
                <section>
                    <Outlet/>
                </section>
            </main>
        </div>
    )
}

export default OrganizerLayout;