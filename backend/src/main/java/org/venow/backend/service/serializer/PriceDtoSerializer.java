package org.venow.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.venow.backend.service.dto.PriceDto;

import java.io.IOException;

public class PriceDtoSerializer extends StdSerializer<PriceDto> {

    public PriceDtoSerializer() {
        super(PriceDto.class);
    }

    @Override
    public void serialize(PriceDto value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int amountInCents = value.getAmountInEuro().intValueExact();
        gen.writeNumber(amountInCents);
    }
}
