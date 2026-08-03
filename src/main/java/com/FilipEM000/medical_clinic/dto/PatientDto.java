package com.FilipEM000.medical_clinic.dto;

import java.time.LocalDate;

public record PatientDto(
        Long id,
        String idCardNo,
        String phoneNumber,
        LocalDate birthday,
        UserDto user) {
}
