import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { Contract } from "../../types/Contract";
import { Card, CardContent} from "@mui/material";
import "../../styles/ApplicationsDetail.css";

const OrganizerContractDetails = () => {
    const { organizerId, contractId } = useParams();
    const [contract, setContract] = useState<Contract | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);


    useEffect(() => {
        if (!organizerId || !contractId) {
            setError("Fehlende Parameter (OrganizerId oder ContractId).");
            setLoading(false);
            return;
        }

        axios
            .get<Contract>(`http://localhost:8080/organizer/${organizerId}/contracts/details/${contractId}`)
            .then((response) => {
                setContract(response.data);
                setLoading(false);
            })
            .catch(() => {
                setError("Fehler beim Laden des Vertrags.");
                setLoading(false);
            });
    }, [organizerId, contractId]);

    if (loading) return <div className="loading-message">Lade Vertragsdetails…</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (!contract) return <div>Keine Vertragsdaten gefunden.</div>;


    // const handleCancelContract = () => {
    //     alert("Vertrag stornieren (Platzhalter-Funktion)!");
    // };

    return (
        <div className="application-detail-container">
            <Card className="application-detail-card">
                <CardContent>
                    <div className="detail-title">
                        Vertragsdetails
                    </div>

                    {/* Beispiel-Struktur, du kannst sie beliebig anpassen */}
                    <section className="detail-section">
                        <div className="section-header">Allgemein</div>
                        <div className="detail-row">
                            <strong>Vertrags-ID:</strong>
                            <span>{contract.contractId}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Status:</strong>
                            <span>{contract.status}</span>
                        </div>
                    </section>

                    <section className="detail-section">
                        <div className="section-header">Event</div>
                        <div className="detail-row">
                            <strong>Event Name:</strong>
                            <span>{contract.eventName}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Standort:</strong>
                            <span>{contract.location}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Startdatum:</strong>
                            <span>{new Date(contract.startDate).toLocaleDateString()}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Enddatum:</strong>
                            <span>{new Date(contract.endDate).toLocaleDateString()}</span>
                        </div>
                    </section>

                    <section className="detail-section">
                        <div className="section-header">Bewerber / Aussteller</div>
                        <div className="detail-row">
                            <strong>Ausstellername:</strong>
                            <span>{contract.applicantName}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Unternehmen:</strong>
                            <span>{contract.businessName}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Kontakt:</strong>
                            <span>{contract.contactInformation}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Type of Business:</strong>
                            <span>{contract.typeOfBusiness}</span>
                        </div>
                    </section>

                    <section className="detail-section">
                        <div className="section-header">Vertrag</div>
                        <div className="detail-row">
                            <strong>Preis:</strong>
                            <span>{contract.price}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Standplatznummer:</strong>
                            <span>{contract.boothNumber}</span>
                        </div>
                        <div className="detail-row">
                            <strong>Terms:</strong>
                            <span>{contract.terms}</span>
                        </div>
                    </section>

                    {/*<section className="detail-section">*/}
                    {/*    <div className="section-header">Aktionen</div>*/}
                    {/*    /!*<Button*!/*/}
                    {/*    /!*    variant="contained"*!/*/}
                    {/*    /!*    color="error"*!/*/}
                    {/*    /!*    onClick={handleCancelContract}*!/*/}
                    {/*    /!*>*!/*/}
                    {/*    /!*    Vertrag stornieren*!/*/}
                    {/*    /!*</Button>*!/*/}
                    {/*</section>*/}
                </CardContent>
            </Card>
        </div>
    );
};

export default OrganizerContractDetails;
