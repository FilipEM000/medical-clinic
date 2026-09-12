package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreatePatientCommand;
import com.FilipEM000.medical_clinic.command.update.UpdatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.exception.PatientNotFoundException;
import com.FilipEM000.medical_clinic.mapper.PatientMapper;
import com.FilipEM000.medical_clinic.mapper.VisitMapper;
import com.FilipEM000.medical_clinic.model.Patient;
import com.FilipEM000.medical_clinic.model.Visit;
import com.FilipEM000.medical_clinic.repository.PatientJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientJpaRepository patientRepository;
    private final PatientMapper patientMapper;
    private final VisitMapper visitMapper;

    public PageDto<PatientDto> getAllPatients(Pageable pageable) {
        log.info("Fetching all patients");
        Page<PatientDto> page = patientRepository.findAll(pageable)
                .map(patientMapper::mapToDto);
        log.info("Fetched {} patients successfully", page.getContent().size());
        return new PageDto<>(page);
    }

    public PatientDto getPatientByEmail(String email) {
        log.info("Fetching patient by email: {}", email);
        Patient patient = findPatientByUserEmail(email);
        log.info("Fetched patient successfully");
        return patientMapper.mapToDto(patient);
    }

    @Transactional
    public PatientDto createPatient(CreatePatientCommand createPatientCommand) {
        log.info("Process of creating patient started");
        Patient patient = patientMapper.mapToEntity(createPatientCommand);
        Patient saved = patientRepository.save(patient);
        log.info("Process of creating patient completed successfully");
        return patientMapper.mapToDto(saved);
    }

    public void deletePatient(String email) {
        log.info("Process of deleting patient '{}' started", email);
        Patient patient = findPatientByUserEmail(email);
        patientRepository.delete(patient);
        log.info("Patient '{}' deleted successfully", email);
    }

    @Transactional
    public void updatePatient(String email, UpdatePatientCommand updatePatientCommand) {
        log.info("Process of updating patient '{}' started", email);
        Patient patient = findPatientByUserEmail(email);
        patient.update(updatePatientCommand);
        patientRepository.save(patient);
        log.info("Patient '{}' updated successfully", email);
    }

    public List<VisitDto> getAllVisits(String email) {
        log.info("Fetching all visits for patient '{}'", email);
        List<Visit> visits = findPatientByUserEmail(email).getVisits();
        log.info("Found {} visits for patient '{}'", visits.size(), email);
        return visits.stream()
                .map(visitMapper::mapToDto)
                .toList();
    }

    private Patient findPatientByUserEmail(String email) {
        return patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }
}
