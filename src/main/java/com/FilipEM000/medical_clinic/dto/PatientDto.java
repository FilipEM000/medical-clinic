package com.FilipEM000.medical_clinic.dto;

import java.time.LocalDate;

public record PatientDto(String email,
                         String password,
                         String idCard,
                         String firstName,
                         String lastName,
                         String phoneNumber,
                         LocalDate birthDay) {
}
