package com.FilipEM000.medical_clinic.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class UserNotFoundException extends MedicalClinicException {
    public UserNotFoundException(String message) {
        super(NOT_FOUND, "User with email '" + message + "' not found");
    }
}
