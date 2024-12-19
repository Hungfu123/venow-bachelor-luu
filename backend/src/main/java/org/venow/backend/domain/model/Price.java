package org.venow.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder(toBuilder = true)
public class Price {
    private final int amountInCents;

    public int getAmountInCents() {
        return amountInCents;
    }
}

