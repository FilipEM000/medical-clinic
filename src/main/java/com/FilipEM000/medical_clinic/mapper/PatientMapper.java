package com.FilipEM000.medical_clinic.mapper;

import com.FilipEM000.medical_clinic.command.CreatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto mapToDto(Patient patient);

    Patient mapToEntity(CreatePatientCommand dto);
}