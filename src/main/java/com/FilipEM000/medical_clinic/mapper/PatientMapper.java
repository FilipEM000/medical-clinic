package com.FilipEM000.medical_clinic.mapper;

import com.FilipEM000.medical_clinic.dto.CreatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.model.Patient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PatientMapper {

    public static PatientDto mapToDto(Patient patient) {
        return new PatientDto(
                patient.getEmail(),
                patient.getPassword(),
                patient.getIdCardNo(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthday());
    }

    public static Patient mapToEntity(CreatePatientCommand dto) {
        return new Patient(
                dto.email(),
                dto.password(),
                null,
                dto.firstName(),
                dto.lastName(),
                null,
                null
        );
    }
}