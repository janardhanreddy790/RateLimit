package com.nagam.example.ratelimiter.model;

public class RateLimitErrorResponse {
    private int status;
    private String message;

    public RateLimitErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
