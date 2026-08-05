package com.FilipEM000.medical_clinic.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class VisitAlreadyTakenException extends MedicalClinicException {
    public VisitAlreadyTakenException(String message) {
        super(CONFLICT, message);
    }
}
