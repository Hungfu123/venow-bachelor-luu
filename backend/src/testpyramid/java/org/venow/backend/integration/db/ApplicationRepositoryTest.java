package org.venow.backend.integration.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.TestBase;
import org.venow.backend.service.UserRepository;
import org.venow.backend.service.dto.ApplicationDTO;
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


    @DisplayName("saving one application and should find all applications by 4")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldFindAllApplicationsForGivenOrganizerId() {
        UUID organizerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withUserId(UUID.randomUUID().toString())
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .build();

        ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withUserId(UUID.randomUUID().toString())
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
                .withUserId(UUID.randomUUID().toString())
                .build();

        applicationRepository.save(application);
        boolean exists = applicationRepository.existsByUserIdAndEventId(application.getUserId(), application.getEventId());
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
        ApplicationDTO application = ApplicationDTOFactory.createDefault().withUserId(UUID.randomUUID().toString()).build();
        ApplicationDTO savedApplication = applicationRepository.save(application);

        Optional<ApplicationDTO> retrievedApplication = applicationRepository.findById(savedApplication.getApplicationId());

        assertThat(retrievedApplication).isPresent();
        assertThat(retrievedApplication.get().getApplicationId()).isEqualTo(savedApplication.getApplicationId());
        assertThat(retrievedApplication.get().getContactInformation()).isEqualTo(savedApplication.getContactInformation());
    }

    @DisplayName("should return empty optional if application is not found")
    @Test
    void shouldReturnEmptyOptionalIfApplicationNotFound() {
        UUID nonExistentId = UUID.randomUUID();

        Optional<ApplicationDTO> retrievedApplication = applicationRepository.findById(nonExistentId);

        assertThat(retrievedApplication).isEmpty();
    }



    @DisplayName("should not find applications")
    @Test
    void shouldNotFindApplications() {
        UUID organizerId = UUID.fromString("b23e4567-e89b-12d3-a456-426614174002");

        ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withUserId(UUID.randomUUID().toString())
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

    @DisplayName("should return all applications for given organizer and event id")
    @Test
    @Sql(scripts = { "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldReturnAllApplicationsForGivenOrganizerAndEventId() {
        UUID organizerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID eventId = UUID.fromString("45353065-3834-3030-2d65-3239633d3431");

        ApplicationDTO application1 = ApplicationDTOFactory.createDefault()
                .withUserId(UUID.randomUUID().toString())
                .withEventId(eventId.toString())
                .build();

        ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withUserId(UUID.randomUUID().toString())
                .withEventId(eventId.toString())
                .build();

        ApplicationDTO savedApplication1= applicationRepository.save(application1);
        ApplicationDTO savedApplication2 = applicationRepository.save(application2);

        List<ApplicationDTO> applications = applicationRepository.getAllApplicationsByOrganizerAndEventId(organizerId, eventId);

        assertThat(applications)
                .hasSize(5)
                .contains(savedApplication1, savedApplication2);
    }

    @DisplayName("should return empty list if no applications found for given organizer and event id")
    @Test
    void shouldReturnEmptyListIfNoApplicationsFoundForGivenOrganizerAndEventId() {
        UUID organizerId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        List<ApplicationDTO> applications = applicationRepository.getAllApplicationsByOrganizerAndEventId(organizerId, eventId);

        assertThat(applications).isEmpty();
    }

    @DisplayName("Should throw exception when organizer ID is null")
    @Test
    void shouldThrowExceptionWhenOrganizerIdIsNull() {
        UUID eventId = UUID.randomUUID();

        assertThatThrownBy(() -> applicationRepository.getAllApplicationsByOrganizerAndEventId(null, eventId))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessageContaining("Organizer ID must not be null");
    }

    @DisplayName("Should throw exception when event ID is null")
    @Test
    void shouldThrowExceptionWhenEventIdIsNull() {
        UUID organizerId = UUID.randomUUID();

        assertThatThrownBy(() -> applicationRepository.getAllApplicationsByOrganizerAndEventId(organizerId, null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessageContaining("Event ID must not be null");
    }




}
