import { useParams, useNavigate } from "react-router-dom";
import { useGet } from "../../hooks/useGet";
import { Contract } from "../../types/Contract";
import axios from "axios";
import "../../styles/ApplicationsDetail.css";

const ExhibitorContractDetailApplication = () => {
    const { exhibitorId, applicationId, contractId } = useParams();
    const navigate = useNavigate();

    const { data: contract, loading, error} = useGet<Contract>(
        exhibitorId && applicationId && contractId
            ? `http://localhost:8080/exhibitor/${exhibitorId}/applications/${applicationId}/contract-details/${contractId}`
            : null
    );

    if (loading) return <div className="loading-message">Lade Vertragsdetails...</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (!contract) return <div>Keine Vertragsdaten gefunden.</div>;

    const updateContractStatus = async (status: "confirm" | "reject") => {
        try {
            await axios.put(
                `http://localhost:8080/exhibitor/${exhibitorId}/contract-details/${contractId}/${status}`
            );
            navigate(`/exhibitor/${exhibitorId}/contracts`);
        } catch (error) {
            console.error(`Fehler beim Ändern des Vertragsstatus:`, error);
        }
    };

    return (
        <div className="application-detail-container">
            <div className="application-detail-card">
                <h1 className="detail-title">Vertragsdetails</h1>

                <section className="detail-section">
                    <h2 className="section-header">Allgemeine Infos</h2>
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

                <div className="contract-buttons">
                    <button className="confirm-application-button" onClick={() => updateContractStatus("confirm")}>
                        Bestätigen
                    </button>
                    <button className="reject-application-exhibitor-button" onClick={() => updateContractStatus("reject")}>
                        Ablehnen
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ExhibitorContractDetailApplication;
