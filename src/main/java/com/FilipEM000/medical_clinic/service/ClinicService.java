package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreateClinicCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateClinicCommand;
import com.FilipEM000.medical_clinic.dto.ClinicDto;
import com.FilipEM000.medical_clinic.exception.ClinicNotFoundException;
import com.FilipEM000.medical_clinic.mapper.ClinicMapper;
import com.FilipEM000.medical_clinic.model.Clinic;
import com.FilipEM000.medical_clinic.repository.ClinicJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicJpaRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    public List<ClinicDto> getAllClinics() {
        return clinicRepository.findAll().stream()
                .map(clinicMapper::mapToDto)
                .toList();
    }

    public ClinicDto getClinicByName(String name) {
        Clinic clinic = findClinicByName(name);
        return clinicMapper.mapToDto(clinic);
    }

    @Transactional
    public ClinicDto createClinic(CreateClinicCommand createClinicCommand) {
        Clinic clinic = clinicMapper.mapToEntity(createClinicCommand);
        Clinic saved = clinicRepository.save(clinic);
        return clinicMapper.mapToDto(saved);
    }

    public void deleteClinic(String name) {
        Clinic clinic = findClinicByName(name);
        clinicRepository.delete(clinic);
    }

    @Transactional
    public void updateClinic(String name, UpdateClinicCommand updateClinicCommand) {
        Clinic clinic = findClinicByName(name);
        clinic.update(updateClinicCommand);
        clinicRepository.save(clinic);
    }

    private Clinic findClinicByName(String name) {
        return clinicRepository.findByName(name)
                .orElseThrow(() -> new ClinicNotFoundException(name));
    }
}
