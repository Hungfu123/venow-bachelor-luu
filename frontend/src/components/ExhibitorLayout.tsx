import {Link, Outlet, useParams} from "react-router-dom";

const ExhibitorLayout = () => {
    const { exhibitorId } = useParams();

    return (
        <div className="layout">
            <aside className="sidebar">
                <h2>Exhibitor</h2>
                <nav>
                    <ul>
                        <li>
                            <Link to={`/exhibitor/${exhibitorId}/events`}>Meine Events</Link>
                        </li>
                        <li>
                            <Link to={`/exhibitor/${exhibitorId}/applications`}>Meine Bewerbungen</Link>
                        </li>
                    </ul>
                </nav>
            </aside>
            <main className="main-content">
                <header>
                    <span>Wilkommen, Exhibitor</span>
                    <button>Meine Bewerbungen</button>
                </header>
                <section>
                    <Outlet/>
                </section>
            </main>
        </div>
    );
};

export default ExhibitorLayout;
