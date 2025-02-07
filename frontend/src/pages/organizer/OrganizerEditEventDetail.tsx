import { useEffect } from "react";
import { useForm, FieldValues, Controller } from "react-hook-form";
import { Button, TextField, Card, CardContent } from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import "../../styles/CreateEvent.css";

const OrganizerEditEventDetail = () => {
    const { organizerId, eventId } = useParams();
    const navigate = useNavigate();

    const {
        control,
        handleSubmit,
        formState: { errors },
        reset,
    } = useForm<FieldValues>({
        defaultValues: {
            title: "",
            description: "",
            location: "",
            startDate: "",
            endDate: "",
            booths: "",
        },
    });

    const formatToLocalDateTime = (date: string) => `${date}T00:00:00`;

    // 1) Beim Laden: Event-Daten holen und ins Formular setzen
    useEffect(() => {
        if (!organizerId || !eventId) {
            return;
        }

        axios
            .get(`http://localhost:8080/organizer/${organizerId}/events/${eventId}`, {
                withCredentials: true
            })
            .then((response) => {
                const eventData = response.data;
                console.log("Geladene Event-Daten:", eventData);
                reset({
                    title: eventData.title,
                    description: eventData.description,
                    location: eventData.location,
                    startDate: eventData.startDate.substring(0, 10),
                    endDate: eventData.endDate.substring(0, 10),
                    booths: String(eventData.booths),
                });
            })
            .catch((error) => {
                console.error("Fehler beim Laden des Events:", error);
            });
    }, [organizerId, eventId, reset]);

    // 2) Beim Abschicken: Post-Request zum Bearbeiten (oder PUT, je nach API)
    const onSubmit = async (data: FieldValues) => {
        if (!organizerId || !eventId) return;
        const apiUrl = `http://localhost:8080/organizer/${organizerId}/events/edit/${eventId}`;

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
            alert("Veranstaltung erfolgreich bearbeitet!");
            navigate(`/organizer/${organizerId}/events`);
        } catch (error) {
            alert("Fehler beim Bearbeiten der Veranstaltung.");
        }
    };

    return (
        <div className="container">
            <Card className="card-container">
                <CardContent>
                    <h2 className="heading">Veranstaltung bearbeiten</h2>
                    <form onSubmit={handleSubmit(onSubmit)} className="form">
                        {/* Titel */}
                        <Controller
                            name="title"
                            control={control}
                            rules={{ required: "Titel ist erforderlich" }}
                            render={({ field }) => (
                                <TextField
                                    {...field}
                                    label="Titel *"
                                    fullWidth
                                    error={!!errors.title}
                                    helperText={errors.title?.message}
                                    className="textfield-container"
                                />
                            )}
                        />

                        {/* Datum */}
                        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "16px" }}>
                            <Controller
                                name="startDate"
                                control={control}
                                rules={{ required: "Startdatum ist erforderlich" }}
                                render={({ field }) => (
                                    <TextField
                                        {...field}
                                        label="Startdatum *"
                                        type="date"
                                        fullWidth
                                        InputLabelProps={{ shrink: true }}
                                        error={!!errors.startDate}
                                        helperText={errors.startDate?.message}
                                        className="textfield-container"
                                    />
                                )}
                            />
                            <Controller
                                name="endDate"
                                control={control}
                                rules={{ required: "Enddatum ist erforderlich" }}
                                render={({ field }) => (
                                    <TextField
                                        {...field}
                                        label="Enddatum *"
                                        type="date"
                                        fullWidth
                                        InputLabelProps={{ shrink: true }}
                                        error={!!errors.endDate}
                                        helperText={errors.endDate?.message}
                                        className="textfield-container"
                                    />
                                )}
                            />
                        </div>

                        {/* Ort */}
                        <Controller
                            name="location"
                            control={control}
                            rules={{ required: "Ort ist erforderlich" }}
                            render={({ field }) => (
                                <TextField
                                    {...field}
                                    label="Wo? *"
                                    fullWidth
                                    error={!!errors.location}
                                    helperText={errors.location?.message}
                                    className="textfield-container"
                                />
                            )}
                        />

                        {/* Anzahl Stellplätze */}
                        <Controller
                            name="booths"
                            control={control}
                            rules={{ required: "Anzahl ist erforderlich", min: { value: 1, message: "Mindestanzahl ist 1" } }}
                            render={({ field }) => (
                                <TextField
                                    {...field}
                                    label="Anzahl d. Stellplätze *"
                                    type="number"
                                    fullWidth
                                    error={!!errors.booths}
                                    helperText={errors.booths?.message}
                                    className="textfield-container"
                                />
                            )}
                        />

                        {/* Beschreibung */}
                        <Controller
                            name="description"
                            control={control}
                            rules={{ required: "Beschreibung ist erforderlich" }}
                            render={({ field }) => (
                                <TextField
                                    {...field}
                                    label="Beschreibung *"
                                    multiline
                                    rows={8}
                                    fullWidth
                                    error={!!errors.description}
                                    helperText={errors.description?.message}
                                    className="textfield-container"
                                />
                            )}
                        />

                        {/* Submit Button */}
                        <Button type="submit" variant="contained" fullWidth className="submit-button">
                            Speichere Bearbeitung der Veranstaltung
                        </Button>
                    </form>
                </CardContent>
            </Card>
        </div>
    );
};

export default OrganizerEditEventDetail;
