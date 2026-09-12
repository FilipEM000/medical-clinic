package com.FilipEM000.medical_clinic.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class DateInThePastException extends MedicalClinicException {
    public DateInThePastException(String message) {
        super(BAD_REQUEST, message);
    }
}
