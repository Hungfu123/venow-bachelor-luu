package org.venow.backend.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.venow.backend.domain.model.Price;
@Converter(autoApply = true)
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
