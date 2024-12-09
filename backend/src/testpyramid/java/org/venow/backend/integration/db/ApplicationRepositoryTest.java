package org.venow.backend.integration.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.TestBase;
import org.venow.backend.applicationservice.ApplicationDTO;
import org.venow.backend.infrastructure.ApplicationRepositoryImpl;
import org.venow.backend.testdata.ApplicationDTOFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ApplicationRepositoryTest extends TestBase {

    @Autowired
    private ApplicationRepositoryImpl applicationRepository;


    @DisplayName("should fail to save Application without requiredField")
    @Test
    void shouldFailToSaveApplicationWithoutRequiredField() {
        ApplicationDTO invalidApplication = ApplicationDTOFactory.createDefault()
                .withContactInformation(null)
                .build();

        assertThatThrownBy(() -> applicationRepository.save(invalidApplication))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("could not execute statement");
    }
    @DisplayName("should return true if application exist for userid and eventId")
    @Test
    void shouldReturnTrueIfApplicationExistsForUserIdAndEventId() {
        ApplicationDTO application = ApplicationDTOFactory.createDefault()
                .build();

        applicationRepository.save(application);
        boolean exists = applicationRepository.existsByUserIdAndEventId(application.userId(), application.eventId());
        assertThat(exists).isTrue();

    }

    @DisplayName("should return false if application does not exist for userid and eventId")
    @Test
    void shouldReturnFalseIfApplicationDoesNotExistForUserIdAndEventId() {
        UUID userId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        boolean exists = applicationRepository.existsByUserIdAndEventId(userId, eventId);

        assertThat(exists).isFalse();
    }
    @DisplayName("should save and retrieve application")
    @Test
    void shouldSaveAndFindApplicationById() {
        ApplicationDTO application = ApplicationDTOFactory.createDefault().build();
        ApplicationDTO savedApplication = applicationRepository.save(application);

        Optional<ApplicationDTO> retrievedApplication = applicationRepository.findById(savedApplication.applicationId());

        assertThat(retrievedApplication).isPresent();
        assertThat(retrievedApplication.get().applicationId()).isEqualTo(savedApplication.applicationId());
        assertThat(retrievedApplication.get().contactInformation()).isEqualTo(savedApplication.contactInformation());
    }

    @DisplayName("should return empty optional if application is not found")
    @Test
    void shouldReturnEmptyOptionalIfApplicationNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<ApplicationDTO> retrievedApplication = applicationRepository.findById(nonExistentId);

        assertThat(retrievedApplication).isEmpty();
    }

    @DisplayName("saving one application and should find all applications by 4")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldFindAllApplicationsForGivenOrganizerId() {
        UUID organizerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .build();

        ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withEventId("17353065-3834-3030-2d65-3239633d3431")
                .build();

        ApplicationDTO savedApplication1= applicationRepository.save(application1);
        ApplicationDTO savedApplication2 = applicationRepository.save(application2);

        List<ApplicationDTO> applications = applicationRepository.getAllApplicationsByOrganizer(organizerId);

        assertThat(applications)
                .hasSize(4)
                .contains(savedApplication1)
                .doesNotContain(savedApplication2);
    }

    @DisplayName("should not find applications")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldNotFindApplications() {
        UUID organizerId = UUID.fromString("b23e4567-e89b-12d3-a456-426614174002");
        ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .build();

        ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withEventId("17353065-3834-3030-2d65-3239633d3431")
                .build();

        applicationRepository.save(application1);
        applicationRepository.save(application2);

        List<ApplicationDTO> applications = applicationRepository.getAllApplicationsByOrganizer(organizerId);

        assertThat(applications).isEmpty();

    }

}
