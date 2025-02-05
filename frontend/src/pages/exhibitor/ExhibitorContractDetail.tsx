import { useParams } from "react-router-dom";
import { useGet } from "../../hooks/useGet";
import { Contract } from "../../types/Contract";
import "../../styles/ApplicationsDetail.css";

const ExhibitorContractDetail = () => {
    const { exhibitorId, contractId } = useParams();

    const { data: contract, loading, error } = useGet<Contract>(
        exhibitorId && contractId
            ? `http://localhost:8080/exhibitor/${exhibitorId}/contracts/details/${contractId}`
            : null
    );


    if (loading) return <div className="loading-message">Lade Vertragsdetails...</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (!contract) return <div>Keine Vertragsdaten gefunden.</div>;


    return (
        <div className="application-detail-container">
            <div className="application-detail-card">
                <h1 className="detail-title">Vertragsdetails</h1>

                <section className="detail-section">
                    <h2 className="section-header">Allgemeine Infos</h2>
                    <p><strong>Vertrag-ID:</strong> {contract.contractId}</p>
                    <p><strong>Status:</strong> {contract.status}</p>
                </section>

                <section className="detail-section">
                    <h2 className="section-header">Event Details</h2>
                    <p><strong>Event Name:</strong> {contract.eventName}</p>
                    <p><strong>Veranstaltungsort:</strong> {contract.location}</p>
                    <p><strong>Datum:</strong> {new Date(contract.startDate).toLocaleDateString()} - {new Date(contract.endDate).toLocaleDateString()}</p>
                </section>

                <section className="detail-section">
                    <h2 className="section-header">Bewerber / Aussteller</h2>
                    <p><strong>Name:</strong> {contract.applicantName}</p>
                    <p><strong>Unternehmen:</strong> {contract.businessName}</p>
                    <p><strong>Kontakt:</strong> {contract.contactInformation}</p>
                    <p><strong>Art des Geschäfts:</strong> {contract.typeOfBusiness}</p>
                </section>

                <section className="detail-section">
                    <h2 className="section-header">Vertragliche Details</h2>
                    <p><strong>Preis:</strong> {contract.price} €</p>
                    <p><strong>Standplatz:</strong> {contract.boothNumber}</p>
                    <p><strong>Bedingungen:</strong> {contract.terms}</p>
                </section>

            </div>
        </div>
    );
};

export default ExhibitorContractDetail;
