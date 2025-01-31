import { Routes, Route } from "react-router-dom";
import OrganizerLayout from "../components/OrganizerLayout";
import ExhibitorLayout from "../components/ExhibitorLayout";
import OrganizerEvents from "../pages/organizer/OrganizerEvents";
import OrganizerCreateEvent from "../pages/organizer/OrganizerCreateEvent";
import ExhibitorEvents from "../pages/exhibitor/ExhibitorEvents";
import ExhibitorApplicationDetails from "../pages/exhibitor/ExhibitorApplicationDetails.tsx";
import OrganizerEventDetail from "../pages/organizer/OrganizerEventDetail.tsx";
import OrganizerEditEventDetail from "../pages/organizer/OrganizerEditEventDetail.tsx";
import OrganizerApplications from "../pages/organizer/OrganizerApplications.tsx";
import OrganizerApplicationsDetail from "../pages/organizer/OrganizerApplicationsDetail.tsx";
import OrganizerContracts from "../pages/organizer/OrganizerContracts.tsx";
import OrganizerContractFormular from "../pages/organizer/OrganizerContractFormular.tsx";
import OrganizerContractDetails from "../pages/organizer/OrganizerContractDetails.tsx";
import ExhibitorEventDetail from "../pages/exhibitor/ExhibitorEventDetail.tsx";
import ExhibitorEventToApply from "../pages/exhibitor/ExhibitorEventToApply.tsx";
import ExhibitorContracts from "../pages/exhibitor/ExhibitorContracts.tsx";
import ExhibitorContractDetail from "../pages/exhibitor/ExhibitorContractDetail.tsx";
import ExhibitorContractDetailApplication from "../pages/exhibitor/ExhibitorContractDetailApplication.tsx";
import OrganizerAllApplicationsOnEvent from "../pages/organizer/OrganizerAllApplicationsOnEvent.tsx";
import ExhibitorApplication from "../pages/exhibitor/ExhibitorApplication.tsx";

const AppRoutes = () => {
    return (
        <Routes>
            {/* Organizer Routes */}
            <Route path="/organizer/:organizerId" element={<OrganizerLayout />}>
                <Route path="events" element={<OrganizerEvents />} />
                <Route path="events/:eventId" element={<OrganizerEventDetail />} />
                <Route path="events/edit/:eventId" element={<OrganizerEditEventDetail />} />
                <Route path="events/create" element={<OrganizerCreateEvent />} />
                {/*Application*/}
                <Route path="events/:eventId/applications" element={<OrganizerAllApplicationsOnEvent />}/>
                <Route path="applications" element={<OrganizerApplications />} />
                <Route path="applications/:applicationId" element={<OrganizerApplicationsDetail/> }/>
                {/*Contracts*/}
                <Route path="contracts" element={<OrganizerContracts/>} />
                <Route path="contracts/:applicationId" element={<OrganizerContractFormular/>} />
                <Route path="contracts/details/:contractId" element={<OrganizerContractDetails/>} />
            </Route>

            {/* Exhibitor Routes */}
            <Route path="/exhibitor/:exhibitorId" element={<ExhibitorLayout />}>
                <Route path="events" element={<ExhibitorEvents />} />
                <Route path="events/:eventId" element={<ExhibitorEventDetail />} />
                <Route path="events/:eventId/application" element={<ExhibitorEventToApply />} />
                <Route path="events/:eventId/application/:applicationId" element={<ExhibitorApplicationDetails />} />
                {/*Applications*/}
                <Route path="applications" element={<ExhibitorApplication/>}/>
                {/*Contracts*/}
                <Route path="contracts" element={<ExhibitorContracts />} />
                <Route path="contracts/details/:contractId" element={<ExhibitorContractDetail />} />
                <Route path="applications/:applicationId/contract-details/:contractId" element={<ExhibitorContractDetailApplication />} />
            </Route>
        </Routes>
    );
};

export default AppRoutes;
