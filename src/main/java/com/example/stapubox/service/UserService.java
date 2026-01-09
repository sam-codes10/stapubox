package com.example.stapubox.service;

import java.util.Date;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.stapubox.entities.UserEntity;
import com.example.stapubox.middleware.Auth;
import com.example.stapubox.models.payloads.JwtClaims;
import com.example.stapubox.models.payloads.LoginPayload;
import com.example.stapubox.models.payloads.SignupPayload;
import com.example.stapubox.models.response.LoginResponse;
import com.example.stapubox.models.response.SignupResponse;
import com.example.stapubox.repositoryDao.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final Auth auth;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder, Auth auth) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.auth = auth;
    }

    public SignupResponse signUp(SignupPayload payload) {
        SignupResponse signupResponse = new SignupResponse();
        UserEntity user = new UserEntity(payload.getEmail(), payload.getPassword(), payload.getFullName(),
                payload.getRole());
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            UserEntity dbRes = userRepo.save(user);
            JwtClaims claims = new JwtClaims(dbRes.getEmail(), dbRes.getId().toString(), dbRes.getRole(),
                    new Date(), null);
            auth.generateToken(claims);
            signupResponse.setMessage("User signed up successfully");
            signupResponse.setEmail(dbRes.getEmail());
            signupResponse.setStatus(true);
            signupResponse.setToken(auth.generateToken(claims));
        } catch (DataIntegrityViolationException e) {
            signupResponse.setMessage("Error during signup: email already exisits");
            signupResponse.setStatus(false);
        } catch (Exception e) {
            signupResponse.setMessage("Error during signup: " + e.getMessage());
            signupResponse.setStatus(false);
        }

        return signupResponse;
    }

    public LoginResponse login(LoginPayload payload) {
        LoginResponse loginResponse = new LoginResponse();
        try {
            UserEntity user = userRepo.findByEmail(payload.getEmail());
            if (user == null || !passwordEncoder.matches(payload.getPassword(), user.getPassword())) {
                loginResponse.setMessage("Invalid email or password");
                loginResponse.setStatus(false);
                return loginResponse;
            }
            JwtClaims claims = new JwtClaims(user.getEmail(), user.getId().toString(), user.getRole(),
                    new Date(), null);
            loginResponse.setMessage("Login successful");
            loginResponse.setStatus(true);
            loginResponse.setEmail(user.getEmail());
            loginResponse.setToken(auth.generateToken(claims));
        } catch (Exception e) {
            loginResponse.setMessage("Error during login: " + e.getMessage());
            loginResponse.setStatus(false);
        }
        return loginResponse;
    }
}
