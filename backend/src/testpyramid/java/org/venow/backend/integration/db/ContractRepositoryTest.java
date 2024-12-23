package org.venow.backend.integration.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.venow.backend.TestBase;
import org.venow.backend.infrastructure.ContractRepositoryImpl;
import org.venow.backend.service.dto.ApplicationDTO;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.testdata.ApplicationDTOFactory;
import org.venow.backend.testdata.ContractDTOFactory;

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
        ContractDTO contract = ContractDTOFactory.createDefault().withApplication(application).build();
        contractRepository.save(contract);

        Optional<ContractDTO> retrievedContract = contractRepository.findById(contract.getContractId());

        assertThat(retrievedContract).isPresent();
        assertThat(retrievedContract.get().getContractId()).isEqualTo(contract.getContractId());
        assertThat(retrievedContract.get().getOrganizerId()).isEqualTo(contract.getOrganizerId());
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
                .withUserId(UUID.randomUUID().toString())
                .withEventId("45353065-3834-3030-2d65-3239633d3431")
                .build();


        ContractDTO contract = ContractDTOFactory.createDefault().withApplication(application).build();
        ContractDTO contract2 = ContractDTOFactory.createDefault().withApplication(application2).withOrganizerId(contract.getOrganizerId().toString()).build();
        contractRepository.save(contract);
        contractRepository.save(contract2);



        List<ContractDTO> contractDTOList = contractRepository.findAllByOrganizerId(contract.getOrganizerId());

        assertThat(contractDTOList).hasSize(2);
    }



}
