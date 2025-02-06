package org.venow.backend.web;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.venow.backend.domain.model.User;
import org.venow.backend.service.UserService;
import org.venow.backend.service.dto.UserDTO;
import org.venow.backend.web.request.LoginRequest;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO request) {
        if (userService.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        UserDTO user = userService.findByEmail(request.email()).orElseThrow();
        String redirectUrl = user.role().equals("ORGANIZER")
                ? "/organizer/" +user.userId() + "/events"
                : "/exhibitor/" +user.userId() + "/events";
        return ResponseEntity.ok(Map.of("redirectUrl", redirectUrl, "role", user.role()));
    }
}
