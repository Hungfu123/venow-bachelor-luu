package org.venow.backend.integration.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.TestBase;
import org.venow.backend.infrastructure.ContractRepositoryImpl;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.ContractDTOFactory;
import org.venow.backend.testdata.UserDTOFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ContractRepositoryTest extends TestBase {

    @Autowired
    private ContractRepositoryImpl contractRepository;

    @DisplayName("should save and retrieve contract by ID")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldSaveAndRetrieveContractById() {
        ApplicationDTO application = ApplicationDTOFactory.createDefault()
                .withId(1L)
                .withApplicationId("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a")
                .withUserId("df333e19-6ed6-4c8b-b86e-ef78943a98a4")
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .build();

        UserDTO organizer = UserDTOFactory.createDefault()
                .withId(7L)
                .withUserId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .withEmail("organizer1@example.com")
                .withName("Gnad Envoll")
                .build();

        UserDTO exhibitor = UserDTOFactory.createDefault()
                .withId(2L)
                .withUserId(UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"))
                .withEmail("showmantest@example.com")
                .withName("Misty Exi")
                .withRole("EXHIBITOR")
                .build();

        ContractDTO contract = ContractDTOFactory.createDefault().withApplication(application).withOrganizer(organizer).withExhibitor(exhibitor).build();
        contractRepository.save(contract);

        Optional<ContractDTO> retrievedContract = contractRepository.findById(contract.getContractId());

        assertThat(retrievedContract).isPresent();
        assertThat(retrievedContract.get().getContractId()).isEqualTo(contract.getContractId());
        assertThat(retrievedContract.get().getOrganizer().userId()).isEqualTo(contract.getOrganizer().userId());
        assertThat(retrievedContract.get().getTerms()).isEqualTo(contract.getTerms());
        assertThat(retrievedContract.get().getPrice().getAmountInEuro()).isEqualTo(contract.getPrice().getAmountInEuro());
        assertThat(retrievedContract.get().getBoothNumber()).isEqualTo(contract.getBoothNumber());
        assertThat(retrievedContract.get().getStatus()).isEqualTo(contract.getStatus());

    }

    @DisplayName("should return empty optional if contract is not found")
    @Test
    void shouldReturnEmptyOptionalIfContractNotFound() {
        Optional<ContractDTO> retrievedContract = contractRepository.findById(UUID.randomUUID());

        assertThat(retrievedContract).isEmpty();
    }

    @DisplayName("should return all contracts by organizer")
    @Test
    @Sql(scripts = {"/db/testmigration/999_cleanup.sql", "/db/testmigration/001_insert-event-user-application.sql"})
    void shouldReturnTrueIfContractExistsByApplicationId() {
        ApplicationDTO application = ApplicationDTOFactory.createDefault()
                .withId(1L)
                .withApplicationId("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0a")
                .withUserId("df333e19-6ed6-4c8b-b86e-ef78943a98a4")
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .build();

        ApplicationDTO application2 = ApplicationDTOFactory.createDefault()
                .withId(2L)
                .withApplicationId("b79a8e9b-7d74-4a5f-93e1-73f451e1ae0b")
                .withUserId("af333e19-6ed6-4c8b-b86e-ef78943a98a2")
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .build();

        UserDTO organizer = UserDTOFactory.createDefault()
                .withId(5L)
                .withUserId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .withEmail("organizer1@example.com")
                .withName("Gnad Envoll")
                .build();

        UserDTO exhibitor = UserDTOFactory.createDefault()
                .withId(2L)
                .withUserId(UUID.fromString("af333e19-6ed6-4c8b-b86e-ef78943a98a2"))
                .withEmail("showmantest@example.com")
                .withName("Misty Exi")
                .withRole("EXHIBITOR")
                .build();

        UserDTO exhibitor2 = UserDTOFactory.createDefault()
                .withId(2L)
                .withUserId(UUID.fromString("df333e19-6ed6-4c8b-b86e-ef78943a98a4"))
                .withEmail("showman@example.com")
                .withName("Maxi Testi")
                .withRole("EXHIBITOR")
                .build();

        ContractDTO contract = ContractDTOFactory.createDefault().withApplication(application).withOrganizer(organizer).withExhibitor(exhibitor).build();
        ContractDTO contract2 = ContractDTOFactory.createDefault().withApplication(application2).withOrganizer(contract.getOrganizer()).withExhibitor(exhibitor2).build();
        contractRepository.save(contract);
        contractRepository.save(contract2);

        List<ContractDTO> contractDTOList = contractRepository.findAllByOrganizerId(contract.getOrganizer().userId());

        assertThat(contractDTOList).hasSize(2);
    }



}
