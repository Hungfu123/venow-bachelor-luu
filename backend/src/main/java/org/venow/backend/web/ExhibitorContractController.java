package org.venow.backend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.venow.backend.service.ContractService;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.web.reponse.ContractResponse;
import org.venow.backend.web.reponse.ResponseMapper;

import java.util.UUID;
@Slf4j
@RestController()
@AllArgsConstructor
@RequestMapping("/exhibitor/{exhibitorId}")
public class ExhibitorContractController {

    private final ContractService contractService;

    @GetMapping("contracts/details/{applicationId}")
    public ResponseEntity<ContractResponse> getContractDetails(
            @PathVariable("exhibitorId") final UUID exhibitorId,
            @PathVariable("applicationId") final UUID applicationId
    ) {
        final ContractDTO contractDTO = contractService.getContractByExhibitor(applicationId, exhibitorId);

        return ResponseEntity.ok(ResponseMapper.mapToContractResponse(contractDTO));
    }


}
