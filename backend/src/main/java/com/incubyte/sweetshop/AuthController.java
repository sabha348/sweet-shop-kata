package com.incubyte.sweetshop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // Sends 201 instead of 200
    public void register(@RequestBody RegisterRequest request) {
        authService.register(request.email(), request.password());
    }
}