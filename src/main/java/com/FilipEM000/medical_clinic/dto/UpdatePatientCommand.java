package com.FilipEM000.medical_clinic.dto;

public record UpdatePatientCommand(
        String email,
        String password,
        String firstName,
        String lastName) {
}