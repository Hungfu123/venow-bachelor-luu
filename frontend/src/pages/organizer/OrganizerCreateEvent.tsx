import { useForm, FieldValues, Controller } from "react-hook-form";
import { Button, TextField, Card, CardContent } from "@mui/material";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import "../../styles/CreateEvent.css";

const OrganizerCreateEvent = () => {
    const { organizerId } = useParams();
    const { control, handleSubmit, formState: { errors } } = useForm<FieldValues>();
    const navigate = useNavigate();

    const formatToLocalDateTime = (date: string) => `${date}T00:00:00`;

    const onSubmit = async (data: FieldValues) => {
        const apiUrl = `http://localhost:8080/organizer/${organizerId}/events/create`;

        const eventRequest = {
            title: data.title,
            description: data.description,
            location: data.location,
            startDate: formatToLocalDateTime(data.startDate),
            endDate: formatToLocalDateTime(data.endDate),
            booths: Number(data.booths),
        };

        try {
            await axios.post(apiUrl, eventRequest, {
                withCredentials: true,
            });

            navigate(`/organizer/${organizerId}/events`)
            alert("Veranstaltung erfolgreich erstellt!");
        } catch (error) {
            alert("Fehler beim Erstellen des Events.");
        }
    };

    return (
        <div className="container">
            <Card className="card-container">
                <CardContent>
                    <h2 className="heading">Erstelle Veranstaltung</h2>
                    <form onSubmit={handleSubmit(onSubmit)} className="form">

                        {/* Titel */}
                        <Controller
                            name="title"
                            control={control}
                            rules={{ required: "Titel ist erforderlich" }}
                            render={({ field }) => (
                                <TextField {...field} label="Titel *" fullWidth error={!!errors.title} helperText={errors.title?.message} className="textfield-container" />
                            )}
                        />

                        {/* Datum */}
                        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "16px" }}>
                            <Controller
                                name="startDate"
                                control={control}
                                rules={{ required: "Startdatum ist erforderlich" }}
                                render={({ field }) => (
                                    <TextField {...field} label="Startdatum *" type="date" fullWidth InputLabelProps={{ shrink: true }} error={!!errors.startDate} helperText={errors.startDate?.message} className="textfield-container" />
                                )}
                            />
                            <Controller
                                name="endDate"
                                control={control}
                                rules={{ required: "Enddatum ist erforderlich" }}
                                render={({ field }) => (
                                    <TextField {...field} label="Enddatum *" type="date" fullWidth InputLabelProps={{ shrink: true }} error={!!errors.endDate} helperText={errors.endDate?.message} className="textfield-container" />
                                )}
                            />
                        </div>

                        {/* Ort */}
                        <Controller
                            name="location"
                            control={control}
                            rules={{ required: "Ort ist erforderlich" }}
                            render={({ field }) => (
                                <TextField {...field} label="Wo? *" fullWidth error={!!errors.location} helperText={errors.location?.message} className="textfield-container" />
                            )}
                        />

                        {/* Anzahl Stellplätze */}
                        <Controller
                            name="booths"
                            control={control}
                            rules={{ required: "Anzahl ist erforderlich", min: { value: 1, message: "Mindestanzahl ist 1" } }}
                            render={({ field }) => (
                                <TextField {...field} label="Anzahl d. Stellplätze *" type="number" fullWidth error={!!errors.booths} helperText={errors.booths?.message} className="textfield-container" />
                            )}
                        />

                        {/* Beschreibung */}
                        <Controller
                            name="description"
                            control={control}
                            rules={{ required: "Beschreibung ist erforderlich" }}
                            render={({ field }) => (
                                <TextField {...field} label="Beschreibung *" multiline rows={8} fullWidth error={!!errors.description} helperText={errors.description?.message} className="textfield-container" />
                            )}
                        />

                        {/* Submit Button */}
                        <Button type="submit" variant="contained" fullWidth className="submit-button">
                            Erstelle Veranstaltung
                        </Button>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
};

export default OrganizerCreateEvent;
