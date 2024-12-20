package org.venow.backend.integration.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.venow.backend.TestBase;
import org.venow.backend.infrastructure.ContractRepositoryImpl;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.testdata.ContractDTOFactory;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ContractRepositoryTest extends TestBase {

    @Autowired
    private ContractRepositoryImpl contractRepository;

    @DisplayName("should save and retrieve contract by ID")
    @Test
    void shouldSaveAndRetrieveContractById() {
        ContractDTO contract = ContractDTOFactory.createDefault().build();
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

}
