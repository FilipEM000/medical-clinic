package com.FilipEM000.medical_clinic.mapper;

import com.FilipEM000.medical_clinic.command.create.CreateClinicCommand;
import com.FilipEM000.medical_clinic.dto.ClinicDto;
import com.FilipEM000.medical_clinic.model.Clinic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
    ClinicDto mapToDto(Clinic clinic);

    Clinic mapToEntity(CreateClinicCommand createClinicCommand);
}
