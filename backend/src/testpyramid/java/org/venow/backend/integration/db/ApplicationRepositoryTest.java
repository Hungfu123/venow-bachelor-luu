package org.venow.backend.integration.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.venow.backend.TestBase;
import org.venow.backend.applicationservice.ApplicationDTO;
import org.venow.backend.infrastructure.ApplicationRepositoryImpl;
import org.venow.backend.testdata.ApplicationDTOFactory;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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


}
