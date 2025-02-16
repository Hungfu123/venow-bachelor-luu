package org.venow.backend.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {


    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Authenticated");
    }
}
