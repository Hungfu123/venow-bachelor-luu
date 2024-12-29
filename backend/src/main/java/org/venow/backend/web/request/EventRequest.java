package org.venow.backend.web.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.venow.backend.web.validator.StartDateIsBeforeEndDate;

import java.time.LocalDateTime;

@StartDateIsBeforeEndDate
public record EventRequest (
        @NotEmpty String title,
        @NotEmpty String description,
        @NotEmpty String location,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate
){
}
