package com.FilipEM000.medical_clinic.command.create;

public record CreateDoctorCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String specialization
) {
}
