import { useEffect, useState } from "react";
import axios from "axios";

export const useGet = <T,>(url: string | null) => {
    const [data, setData] = useState<T | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!url) return;

        axios
            .get<T>(url)
            .then((response) => {
                setData(response.data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message || "Fehler beim Laden der Daten.");
                setLoading(false);
            });

    }, [url]);

    return { data, loading, error };
};
