//package org.venow.backend.web;
//
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.venow.backend.applicationservice.ApplicationService;
//import org.venow.backend.applicationservice.dto.ApplicationDTO;
//import org.venow.backend.web.reponse.ContractResponse;
//
//import java.util.UUID;
//
//@Slf4j
//@RestController
//@AllArgsConstructor
//@RequestMapping("/organizer/{organizerId}")
//public class OrganizerContractController {
//
//    private final ApplicationService applicationService;
//
//    @GetMapping("/contracts/{applicationId}")
//    public ResponseEntity<ContractResponse> getContract(
//            @PathVariable final UUID organizerId,
//            @PathVariable final UUID applicationId
//    ) {
//        ApplicationDTO application = applicationService.getApplication(applicationId);
//
//        if (application == null) {
//            return ResponseEntity.notFound().build();
//        }
//        String applicantName = applicationService.getApplicantName(application.getUserId());
//
//
//        ContractResponse contractResponse = new ContractResponse(
//                application.getId(),
//                UUID.randomUUID(),
//                "Terms",
//        );
//
//
//        return ResponseEntity.ok();
//    }
//}
