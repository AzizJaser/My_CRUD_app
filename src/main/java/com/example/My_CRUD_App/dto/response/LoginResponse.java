package com.example.My_CRUD_App.dto.response;

import java.time.Instant;

public class LoginResponse {

    private String token;

    private final String type = "Bearer ";

    private Instant issueAt;

    private Instant expireAt;

    public LoginResponse() {
    }

    public LoginResponse(String token, Instant expireAt,Instant issueAt) {
        this.token = token;
        this.issueAt = issueAt;
        this.expireAt = expireAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public Instant getExpireAt() {
        return expireAt;
    }

    public void setIssueAt(Instant issueAt) {
        this.issueAt = issueAt;
    }

    public void setExpireAt(Instant expireAt) {
        this.expireAt = expireAt;
    }

    public Instant getIssueAt() {
        return issueAt;
    }

}
