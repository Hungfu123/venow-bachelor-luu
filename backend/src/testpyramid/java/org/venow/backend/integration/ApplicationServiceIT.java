package org.venow.backend.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.TestBase;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.ApplicationService;
import org.venow.backend.service.exception.DuplicateApplicationException;
import org.venow.backend.infrastructure.ApplicationRepositoryImpl;
import org.venow.backend.testdata.ApplicationDTOFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ApplicationServiceIT extends TestBase {

    @Autowired
    private ApplicationService applicationservice;

    @Autowired
    private ApplicationRepositoryImpl applicationRepository;


    @DisplayName("should create a new application and save to database")
    @Test
    void shouldCreateApplication() {
        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault().build();

        final ApplicationDTO result = applicationservice.createApplication(applicationDTO);


        assertThat(applicationRepository.existsByUserIdAndEventId(result.getUserId(), result.getEventId())).isTrue();
        assertThat(result.getEventId()).isEqualTo(applicationDTO.getEventId());
        assertThat(result.getStatus()).isEqualTo(applicationDTO.getStatus());
    }

    @DisplayName("should not create a new application when exhibitor already applied to the same event")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldNotCreateApplicationWhenExhibitorAlreadyApplied() {

        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault()
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .withUserId("df333e19-6ed6-4c8b-b86e-ef78943a98a4")
                .build();

        assertThatThrownBy(() -> applicationservice.createApplication(applicationDTO))
                .isInstanceOf(DuplicateApplicationException.class)
                .hasMessageContaining("User has already applied for this event");
    }

    @DisplayName("another application of another user on the same event should be created ")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldCreateAnotherApplicationForSameEvent() {

        final ApplicationDTO applicationDTO = ApplicationDTOFactory.createDefault()
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .withUserId("bf333e19-6ed6-4c8b-b86e-ef78943a98a4")
                .build();

        final ApplicationDTO result = applicationservice.createApplication(applicationDTO);
        assertThat(result.getEventId()).isEqualTo(applicationDTO.getEventId());
        assertThat(result.getUserId()).isEqualTo(applicationDTO.getUserId());

    }


}
