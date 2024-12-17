package org.venow.backend.service.dto;

import java.math.BigDecimal;

public class PriceDto {
    private final BigDecimal amountInEuro;

    public PriceDto(int amountInCents) {
        this.amountInEuro = BigDecimal.valueOf(amountInCents).divide(BigDecimal.valueOf(100));
    }
}
