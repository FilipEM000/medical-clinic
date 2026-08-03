package com.FilipEM000.medical_clinic.dto;

import java.util.List;

public record DoctorDto(
        Long id,
        String specialization,
        UserDto user,
        List<ClinicDto> clinics
) {
}
