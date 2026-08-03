package com.FilipEM000.medical_clinic.mapper;

import com.FilipEM000.medical_clinic.command.create.CreatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.model.Patient;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface PatientMapper {

    PatientDto mapToDto(Patient patient);

    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.firstName", source = "firstName")
    @Mapping(target = "user.lastName", source = "lastName")
    Patient mapToEntity(CreatePatientCommand dto);

    @AfterMapping
    default void linkUserToPatient(@MappingTarget Patient patient) {
        if (patient.getUser() != null) {
            patient.getUser().setPatient(patient);
        }
    }
}