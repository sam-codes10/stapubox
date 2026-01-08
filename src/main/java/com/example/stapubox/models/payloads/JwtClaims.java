package com.example.stapubox.models.payloads;

import java.util.Date;


public class JwtClaims {
    private String email;
    private String userId;
    private String role;
    private Date issuedAt;
    private Date expiry;

    public JwtClaims(String email, String userId, String role, Date issuedAt, Date expiry) {
        this.email = email;
        this.userId = userId;
        this.role = role;
        this.issuedAt = issuedAt;
        this.expiry = expiry;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public Date getExpiry() {
        return expiry;
    }
}
