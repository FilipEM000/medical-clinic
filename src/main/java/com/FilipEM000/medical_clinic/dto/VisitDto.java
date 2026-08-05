package com.FilipEM000.medical_clinic.dto;

import java.time.LocalDateTime;

public record VisitDto(
        Long id,
        LocalDateTime startDate,
        LocalDateTime endDate,
        PatientDto patient,
        DoctorDto doctor
) {

}
