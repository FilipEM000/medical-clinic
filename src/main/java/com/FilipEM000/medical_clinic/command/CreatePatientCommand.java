package com.FilipEM000.medical_clinic.command;

public record CreatePatientCommand(
        String email,
        String password,
        String firstName,
        String lastName) {
}
