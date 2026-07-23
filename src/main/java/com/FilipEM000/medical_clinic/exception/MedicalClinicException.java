package com.FilipEM000.medical_clinic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public abstract class MedicalClinicException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final LocalDateTime createdAt;

    public MedicalClinicException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.createdAt = LocalDateTime.now();
    }
}
