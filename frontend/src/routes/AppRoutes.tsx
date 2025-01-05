import { Routes, Route } from "react-router-dom";
import OrganizerLayout from "../components/OrganizerLayout";
import ExhibitorLayout from "../components/ExhibitorLayout";
import OrganizerEvents from "../pages/organizer/OrganizerEvents";
import OrganizerCreateEvent from "../pages/organizer/OrganizerCreateEvent";
import ExhibitorEvents from "../pages/exhibitor/ExhibitorEvents";
import ExhibitorApplicationDetails from "../pages/exhibitor/ExhibitorApplicationDetails.tsx";
import ExhibitorApplication from "../pages/exhibitor/ExhibitorApplication.tsx";
import OrganizerEventDetail from "../pages/organizer/OrganizerEventDetail.tsx";
import OrganizerEditEventDetail from "../pages/organizer/OrganizerEditEventDetail.tsx";

const AppRoutes = () => {
    return (
        <Routes>
            {/* Organizer Routes */}
            <Route path="/organizer/:organizerId" element={<OrganizerLayout />}>
                <Route path="events" element={<OrganizerEvents />} />
                <Route path="events/:eventId" element={<OrganizerEventDetail />} />
                <Route path="events/edit/:eventId" element={<OrganizerEditEventDetail />} />
                <Route path="events/create" element={<OrganizerCreateEvent />} />
            </Route>

            {/* Exhibitor Routes */}
            <Route path="/exhibitor/:exhibitorId" element={<ExhibitorLayout />}>
                <Route path="events" element={<ExhibitorEvents />} />
                <Route path="events/:eventId/application" element={<ExhibitorApplication />} />
                <Route path="events/:eventId/application/:applicationId" element={<ExhibitorApplicationDetails />} />
            </Route>
        </Routes>
    );
};

export default AppRoutes;
