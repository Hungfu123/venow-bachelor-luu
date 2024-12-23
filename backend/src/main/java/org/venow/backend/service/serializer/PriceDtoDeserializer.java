package org.venow.backend.service.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.venow.backend.service.dto.PriceDto;

import java.io.IOException;
import java.math.BigDecimal;

public class PriceDtoDeserializer extends JsonDeserializer {

    @Override
    public PriceDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        int amountInCents = p.getIntValue();
        BigDecimal amountInEuro = BigDecimal.valueOf(amountInCents);
        return new PriceDto(amountInEuro);
    }
}
