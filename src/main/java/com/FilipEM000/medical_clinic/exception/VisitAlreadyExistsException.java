package com.FilipEM000.medical_clinic.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class VisitAlreadyExistsException extends MedicalClinicException {
    public VisitAlreadyExistsException(String message) {
        super(CONFLICT, message);
    }
}
