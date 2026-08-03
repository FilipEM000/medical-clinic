package com.FilipEM000.medical_clinic.command.create;

public record CreateClinicCommand(
        String name,
        String city,
        String postcode,
        String street,
        String streetNumber
) {
}
