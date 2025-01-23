package org.venow.backend.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.venow.backend.web.request.EventRequest;
import org.venow.backend.web.validator.DateRangeValidator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class DateRangeValidatorTest {

    private DateRangeValidator dateRangeValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        dateRangeValidator = new DateRangeValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    @DisplayName("Should return true when startDate is before endDate")
    void shouldReturnTrueWhenStartDateIsBeforeEndDate() {
        EventRequest validEvent = new EventRequest(
                "Test Event", "Test Description", "Duesseldorf",
                LocalDateTime.parse("2024-01-01T08:02"),
                LocalDateTime.parse("2024-01-03T08:02"),
                50
        );

        boolean isValid = dateRangeValidator.isValid(validEvent, context);
        assertThat(isValid).isTrue();
    }


    @Test
    @DisplayName("Should return true when startDate is null")
    void shouldReturnTrueWhenStartDateIsNull() {
        EventRequest eventWithNullStartDate = new EventRequest(
                "Test Event", "Test Description", "Duesseldorf",
                null,
                LocalDateTime.parse("2024-01-03T08:02"),
                50
        );

        boolean isValid = dateRangeValidator.isValid(eventWithNullStartDate, context);
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should return true when endDate is null")
    void shouldReturnTrueWhenEndDateIsNull() {
        EventRequest eventWithNullEndDate = new EventRequest(
                "Test Event", "Test Description", "Duesseldorf",
                LocalDateTime.parse("2024-01-01T08:02"),
                null,
                50
        );

        boolean isValid = dateRangeValidator.isValid(eventWithNullEndDate, context);
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should add violation message when startDate is after endDate")
    void shouldAddViolationMessageWhenStartDateIsAfterEndDate() {
        EventRequest invalidEvent = new EventRequest(
                "Test Event", "Test Description", "Duesseldorf",
                LocalDateTime.parse("2024-01-03T08:02"),
                LocalDateTime.parse("2024-01-01T08:02"),
                50
        );

        ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString()))
                .thenReturn(violationBuilder);

        boolean isValid = dateRangeValidator.isValid(invalidEvent, context);

        assertThat(isValid).isFalse();
        Mockito.verify(context).buildConstraintViolationWithTemplate("End date must be after start date");
        Mockito.verify(violationBuilder).addConstraintViolation();
    }
}
