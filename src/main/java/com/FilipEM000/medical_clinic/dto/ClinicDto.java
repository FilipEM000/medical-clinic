package com.FilipEM000.medical_clinic.dto;

public record ClinicDto(
        Long id,
        String name,
        String city,
        String postcode,
        String street,
        String streetNumber
) {
}
