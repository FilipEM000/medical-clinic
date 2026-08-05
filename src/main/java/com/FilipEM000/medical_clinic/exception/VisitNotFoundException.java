package com.FilipEM000.medical_clinic.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class VisitNotFoundException extends MedicalClinicException {
    public VisitNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
