package com.FilipEM000.medical_clinic.mapper;

import com.FilipEM000.medical_clinic.command.create.CreateVisitCommand;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.model.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PatientMapper.class, DoctorMapper.class})
public interface VisitMapper {
    VisitDto mapToDto(Visit visit);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "id", ignore = true)
    Visit mapToEntity(CreateVisitCommand createVisitCommand);
}
