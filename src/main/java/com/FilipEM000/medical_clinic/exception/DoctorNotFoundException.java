package com.FilipEM000.medical_clinic.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class DoctorNotFoundException extends MedicalClinicException {
    public DoctorNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
