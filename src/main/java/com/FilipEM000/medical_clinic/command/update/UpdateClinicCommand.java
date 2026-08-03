package com.FilipEM000.medical_clinic.command.update;

public record UpdateClinicCommand(
        String name,
        String city,
        String postcode,
        String street,
        String streetNumber
) {
}
