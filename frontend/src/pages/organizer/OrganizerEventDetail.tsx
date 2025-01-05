import {useParams} from "react-router-dom";

const OrganizerEventDetail = () => {

    const {id} = useParams();
    return <h1>Details for Event {id}</h1>;
};

export default OrganizerEventDetail;