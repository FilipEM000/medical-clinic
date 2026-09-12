package com.FilipEM000.medical_clinic.command.create;

import java.time.LocalDate;

public record CreatePatientCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String idCardNo,
        String phoneNumber,
        LocalDate birthday) {
}
