package com.FilipEM000.medical_clinic.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class InvalidDateException extends MedicalClinicException {
    public InvalidDateException(String message) {
        super(BAD_REQUEST, message);
    }
}
