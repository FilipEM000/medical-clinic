package com.FilipEM000.medical_clinic.mapper;

import com.FilipEM000.medical_clinic.command.create.CreateDoctorCommand;
import com.FilipEM000.medical_clinic.dto.DoctorDto;
import com.FilipEM000.medical_clinic.model.Doctor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ClinicMapper.class, UserMapper.class})
public interface DoctorMapper {
    DoctorDto mapToDto(Doctor doctor);

    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.firstName", source = "firstName")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clinics", ignore = true)
    Doctor mapToEntity(CreateDoctorCommand createDoctorCommand);

    @AfterMapping
    default void linkUserToDoctor(@MappingTarget Doctor doctor) {
        if (doctor.getUser() != null) {
            doctor.getUser().setDoctor(doctor);
        }
    }
}
