package org.venow.backend.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.venow.backend.service.serializer.PriceDtoDeserializer;
import org.venow.backend.service.serializer.PriceDtoSerializer;

import java.math.BigDecimal;

@Getter
@JsonSerialize(using = PriceDtoSerializer.class)
@JsonDeserialize(using = PriceDtoDeserializer.class)
public class PriceDto {
    @NotNull(message = "Price must not be null")
    @Min(value = 1, message = "Price must be greater than 0")
    private final BigDecimal amountInEuro;

    public PriceDto(BigDecimal amountInEuro) {
        this.amountInEuro = amountInEuro;
    }


    public PriceDto(int amountInCents) {
        this.amountInEuro = BigDecimal.valueOf(amountInCents).divide(BigDecimal.valueOf(100));
    }
    public PriceDto() {
        this.amountInEuro = BigDecimal.ZERO;
    }
}
