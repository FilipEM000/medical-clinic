package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreateClinicCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateClinicCommand;
import com.FilipEM000.medical_clinic.dto.ClinicDto;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.exception.ClinicNotFoundException;
import com.FilipEM000.medical_clinic.mapper.ClinicMapper;
import com.FilipEM000.medical_clinic.model.Clinic;
import com.FilipEM000.medical_clinic.repository.ClinicJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicJpaRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    public PageDto<ClinicDto> getAllClinics(Pageable pageable) {
        log.info("Fetching all clinics");
        Page<ClinicDto> page = clinicRepository.findAll(pageable)
                .map(clinicMapper::mapToDto);
        log.info("Fetched {} clinics successfully", page.getContent().size());
        return new PageDto<>(page);
    }

    public ClinicDto getClinicByName(String name) {
        log.info("Fetching clinic by name: {}", name);
        Clinic clinic = findClinicByName(name);
        log.info("Fetched successfully");
        return clinicMapper.mapToDto(clinic);
    }

    @Transactional
    public ClinicDto createClinic(CreateClinicCommand createClinicCommand) {
        log.info("Process of creating clinic started");
        Clinic clinic = clinicMapper.mapToEntity(createClinicCommand);
        Clinic saved = clinicRepository.save(clinic);
        log.info("Process of creating clinic completed successfully");
        return clinicMapper.mapToDto(saved);
    }

    public void deleteClinic(String name) {
        log.info("Process of deleting clinic '{}' started", name);
        Clinic clinic = findClinicByName(name);
        clinicRepository.delete(clinic);
        log.info("Clinic '{}' deleted successfully", name);
    }

    @Transactional
    public void updateClinic(String name, UpdateClinicCommand updateClinicCommand) {
        log.info("Process of updating clinic '{}' started", name);
        Clinic clinic = findClinicByName(name);
        clinic.update(updateClinicCommand);
        clinicRepository.save(clinic);
        log.info("Clinic '{}' updated successfully", name);
    }

    private Clinic findClinicByName(String name) {
        return clinicRepository.findByName(name)
                .orElseThrow(() -> new ClinicNotFoundException(name));
    }
}
