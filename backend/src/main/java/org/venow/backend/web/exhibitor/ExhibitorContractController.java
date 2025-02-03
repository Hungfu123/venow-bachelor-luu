package org.venow.backend.web.exhibitor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.venow.backend.domain.model.Status;
import org.venow.backend.service.ContractService;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.service.exception.InvalidStatusException;
import org.venow.backend.web.reponse.ContractResponse;
import org.venow.backend.web.reponse.ResponseMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/exhibitor/{exhibitorId}")
public class ExhibitorContractController {

    private final ContractService contractService;

    @GetMapping("contracts/details/{contractId}")
    public ResponseEntity<ContractResponse> getContractDetails(
            @PathVariable("exhibitorId") final UUID exhibitorId,
            @PathVariable("contractId") final UUID contractId
    ) {
        final ContractDTO contractDTO = contractService.getContractByContractId(contractId);

        return ResponseEntity.ok(ResponseMapper.mapToContractResponse(contractDTO));
    }

    //TODO: writing tests
    @GetMapping("applications/{applicationId}/contracts/overview-detail/{contractId}")
    public ResponseEntity<Map<String, String>> overviewContractDetails(
            @PathVariable("exhibitorId") final UUID exhibitorId,
            @PathVariable("applicationId") final UUID applicationId,
            @PathVariable("contractId") final UUID contractId
    ) {
        final ContractDTO contractDTO = contractService.getContractByContractId(contractId);

        String redirectUrl;
        if (contractDTO.getStatus().equals(Status.CONTRACT.name())) {
            redirectUrl = "/exhibitor/" + exhibitorId + "/applications/" + applicationId + "/contract-details/" + contractId;
        } else {
            redirectUrl = "/exhibitor/" + exhibitorId + "/contracts/details/" + contractId;
        }

        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", redirectUrl);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/applications/{applicationId}/contract-details/{contractId}")
    public ResponseEntity<ContractResponse> getContractDetails(
            @PathVariable("exhibitorId") final UUID exhibitorId,
            @PathVariable("applicationId") final UUID applicationId,
            @PathVariable("contractId") final UUID contractId
    ) {
        final ContractDTO contractDTO = contractService.getContractByApplicationIdAndContactId(applicationId, contractId);

        return ResponseEntity.ok(ResponseMapper.mapToContractResponse(contractDTO));
    }

    @PutMapping("contract-details/{contractId}/confirm")
    public ResponseEntity<Void> confirmContract(
            @PathVariable("exhibitorId") final UUID exhibitorId,
            @PathVariable("contractId") final UUID contractId
    ) throws InvalidStatusException {
        contractService.updateContractStatus(contractId, Status.CONFIRMED);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("contract-details/{contractId}/reject")
    public ResponseEntity<Void> rejectContract(
            @PathVariable("exhibitorId") final UUID exhibitorId,
            @PathVariable("contractId") final UUID contractId
    ) throws InvalidStatusException {
        contractService.updateContractStatus(contractId, Status.REJECTED);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("contracts")
    public ResponseEntity<List<ContractResponse>> getAllContractsByExhibitor(
            @PathVariable("exhibitorId") final UUID exhibitorId
    ) {
        final List<ContractDTO> contractDTOList = contractService.getAllContractsByExhibitor(exhibitorId);

        return ResponseEntity.ok(contractDTOList.stream().map(ResponseMapper::mapToContractResponse).toList());
    }
}
