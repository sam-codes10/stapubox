package com.example.stapubox.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stapubox.models.payloads.LoginPayload;
import com.example.stapubox.models.payloads.SignupPayload;
import com.example.stapubox.models.response.LoginResponse;
import com.example.stapubox.models.response.SignupResponse;
import com.example.stapubox.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/min-auth/")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("sign-up")
    public ResponseEntity<?> SignUp(@RequestBody SignupPayload signupPayload) {
        SignupResponse response = userService.signUp(signupPayload);
        if (response.isStatus()) {
            return ResponseEntity.ok(response);
        } else if (response.getMessage().contains("email already exisits")) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> Login(@RequestBody LoginPayload loginPayload) {
        LoginResponse loginResponse = userService.login(loginPayload);
        if (loginResponse.isStatus()) {
            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.status(401).body(loginResponse);
        }
    }

}
