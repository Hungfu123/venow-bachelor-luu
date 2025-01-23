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

        if (!request.startDate().isBefore(request.endDate())) {
            context.buildConstraintViolationWithTemplate("End date must be after start date")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
