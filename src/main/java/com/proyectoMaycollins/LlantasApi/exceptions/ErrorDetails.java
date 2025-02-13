package com.proyectoMaycollins.LlantasApi.exceptions;


import java.time.LocalDateTime;

public class ErrorDetails {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorDetails(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}