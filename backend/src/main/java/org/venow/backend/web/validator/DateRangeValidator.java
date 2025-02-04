package org.venow.backend.web.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.venow.backend.web.request.EventRequest;

public class DateRangeValidator implements ConstraintValidator<StartDateIsBeforeEndDate, EventRequest> {

    @Override
    public boolean isValid(EventRequest request, ConstraintValidatorContext context) {
        if (request.startDate() == null || request.endDate() == null) {
            return true;
        }

        if (request.startDate().isAfter(request.endDate())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End date must be after or equal to start date")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
