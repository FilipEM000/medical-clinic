package com.FilipEM000.medical_clinic.command.update;

public record UpdateUserCommand(
        String email,
        String firstName,
        String lastName) {
}
