package com.FilipEM000.medical_clinic.exception.handler;

import com.FilipEM000.medical_clinic.dto.ErrorMessageDto;
import com.FilipEM000.medical_clinic.exception.MedicalClinicException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MedicalClinicException.class)
    public ResponseEntity<ErrorMessageDto> handleException(MedicalClinicException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorMessageDto(ex.getMessage()));
    }
}
