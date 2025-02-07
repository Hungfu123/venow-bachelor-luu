package org.venow.backend.web;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.venow.backend.domain.model.User;
import org.venow.backend.service.UserService;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.web.request.LoginRequest;

import java.util.Map;

@RestController
@AllArgsConstructor
public class AuthController {


    @GetMapping("/health")
    public ResponseEntity<String> health(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        return ResponseEntity.ok("Authenticated");
    }
}
