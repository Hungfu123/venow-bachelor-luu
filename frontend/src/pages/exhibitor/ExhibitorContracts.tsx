import {useParams, Link} from "react-router-dom";
import {Contract} from "../../types/Contract.tsx";
import {useGet} from "../../hooks/useGet";
import "../../styles/GridEvent.css"; // Verwendet vorhandene CSS-Datei

const ExhibitorContracts = () => {
    const {exhibitorId} = useParams();

    const {data: contracts, loading, error} = useGet<Contract[]>(
        exhibitorId ? `http://localhost:8080/exhibitor/${exhibitorId}/contracts` : null, {
            withCredentials: true
        }
    );

    if (loading) return <div>Lade Verträge...</div>;
    if (error) return <div>{error}</div>;
    if (!contracts || contracts.length === 0) return <div>Keine Verträge gefunden.</div>;

    return (
        <div className="grid-events-container">
            <h1 className="grid-events-title">Meine Verträge</h1>

            {/* Verträge anzeigen */}
            <div className="grid-container">
                {contracts.map((contract) => (
                    <Link
                        to={`/exhibitor/${exhibitorId}/applications/${contract.applicationId}/contracts/overview-detail/${contract.contractId}`}
                        key={`${contract.applicationId}-${contract.contractId}`}
                        className="grid-event-card bg-white rounded-lg shadow-md border border-gray-300 p-4 transition-transform duration-300 ease-in-out hover:shadow-lg hover:-translate-y-1 cursor-pointer"
                    >

                        <div className="grid-event-header flex justify-between items-center">
                            <h2 className="text-lg font-bold text-gray-800">{contract.eventName}</h2>
                        </div>
                        <div className="grid-eventOrganizerName mt-2 text-gray-600">
                            <p>{contract.organizerName}</p>
                        </div>
                        <div className="grid-event-details mt-4 text-gray-600">
                            <div className="flex justify-between">
                                <strong>Datum:</strong>
                                <span>
                                    {new Date(contract.startDate).toLocaleDateString()} - {new Date(contract.endDate).toLocaleDateString()}
                                </span>
                            </div>
                            <div className="flex justify-between">
                                <strong>Veranstaltungsort:</strong>
                                <span>{contract.location}</span>
                            </div>
                            <div className="flex justify-between">
                                <strong>Status:</strong>
                                <span className={`contract-status ${contract.status}`}>
                                {contract.status}
                                </span>
                            </div>
                        </div>
                        {/*<div className="event-footer">*/}
                        {/*    <Link*/}
                        {/*        to={`/exhibitor/${exhibitorId}/contracts/${contract.contractId}`}*/}
                        {/*        className="view-button"*/}
                        {/*        onClick={(e) => e.stopPropagation()}*/}
                        {/*    >*/}
                        {/*        Kontakt*/}
                        {/*    </Link>*/}
                        {/*</div>*/}

                    </Link>
                ))}
            </div>
        </div>
    );
};

export default ExhibitorContracts;
