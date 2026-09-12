package com.FilipEM000.medical_clinic.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class PatientNotFoundException extends MedicalClinicException {
    public PatientNotFoundException(String message) {
        super(NOT_FOUND, "Patient with email '" + message + "' not found");
    }
}
