import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Contract} from "../../types/Contract.tsx";
import axios from "axios";

const OrganizerContracts = () => {
    const {organizerId} = useParams();
    const navigate = useNavigate();
    const[contracts, setContracts] = useState<Contract[]>();
    const [loading, setLoading] = useState<boolean>(true);

    const fetchContracts = () => {
        if(!organizerId) {
            console.log("OrganizerId fehlt");
            return
    }
        axios
            .get<Contract[]>(`http://localhost:8080/organizer/${organizerId}/contracts`, {
                withCredentials: true
            })
            .then(reponse => {
                setContracts(reponse.data);
                setLoading(false);
            })
            .catch(error =>{
                console.log("Fehler beim Abrufen der Verträge", error);
                setLoading(false);
            });
    };
    useEffect(() => {
        fetchContracts();
    },[organizerId])

    const handleRowClick = (contractId: string) => {
        navigate(`/organizer/${organizerId}/contracts/details/${contractId}`)
    };

    if(loading) return <div>Lade Verträge...</div>


    return (
        <div className={"table-container"}>
            <h1 className="table-title">Vertragsübersicht</h1>
            <table className="T-table">
                <thead>
                <tr>
                    <th>Bewerbername</th>
                    <th>Titel</th>
                    <th>Ort</th>
                    <th>StandplatzTyp</th>
                    <th>Zeitraum</th>
                    <th>Status</th>
                    <th>StanplatzNummer</th>
                </tr>
                </thead>
                <tbody>
                {contracts?.map((contract) => (
                    <tr
                        key={contract.contractId}
                        className="clickable-row"
                        onClick={() => handleRowClick(contract.contractId)}
                    >
                        <td>{contract.applicantName}</td>
                        <td>{contract.eventName}</td>
                        <td>{contract.location}</td>
                        <td>{contract.typeOfBusiness}</td>
                        <td>{new Date(contract.startDate).toLocaleDateString()} - {new Date(contract.endDate).toLocaleDateString()}</td>
                        <td>{contract.status}</td>
                        <td>
                            {contract.boothNumber}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>


        </div>
    )
}

export default OrganizerContracts;