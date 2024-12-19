package org.venow.backend.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PriceDto {
    @NotNull(message = "Price must not be null")
    @Min(value = 1, message = "Price must be greater than 0")
    private final BigDecimal amountInEuro;

    public PriceDto(int amountInCents) {
        this.amountInEuro = BigDecimal.valueOf(amountInCents).divide(BigDecimal.valueOf(100));
    }
    public PriceDto() {
        this.amountInEuro = BigDecimal.ZERO;
    }
}
