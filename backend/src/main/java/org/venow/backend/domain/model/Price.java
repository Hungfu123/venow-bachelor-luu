package org.venow.backend.domain.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Price {
    private final int amountInCents;

    public int getAmountInCents() {
        return amountInCents;
    }
}

