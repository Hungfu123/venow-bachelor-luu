package org.venow.backend.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import org.venow.backend.domain.model.Price;

public class PriceConverter implements AttributeConverter<Price, Integer> {
    @Override
    public Integer convertToDatabaseColumn(final Price price) {
       return price == null ?  null: price.getAmountInCents();
    }

    @Override
    public Price convertToEntityAttribute(final Integer priceInCents) {
        return priceInCents == null ? null: new Price(priceInCents);
    }
}
