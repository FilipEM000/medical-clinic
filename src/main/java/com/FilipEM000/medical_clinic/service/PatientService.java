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
import com.FilipEM000.medical_clinic.repository.PatientJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientJpaRepository patientRepository;
    private final PatientMapper patientMapper;
    private final VisitMapper visitMapper;

    public PageDto<PatientDto> getAllPatients(Pageable pageable) {
        Page<PatientDto> page = patientRepository.findAll(pageable)
                .map(patientMapper::mapToDto);
        return new PageDto<>(page);
    }

    public PatientDto getPatientByEmail(String email) {
        Patient patient = findPatientByUserEmail(email);
        return patientMapper.mapToDto(patient);
    }

    @Transactional
    public PatientDto createPatient(CreatePatientCommand createPatientCommand) {
        Patient patient = patientMapper.mapToEntity(createPatientCommand);
        Patient saved = patientRepository.save(patient);
        return patientMapper.mapToDto(saved);
    }

    public void deletePatient(String email) {
        Patient patient = findPatientByUserEmail(email);
        patientRepository.delete(patient);
    }

    @Transactional
    public void updatePatient(String email, UpdatePatientCommand updatePatientCommand) {
        Patient patient = findPatientByUserEmail(email);
        patient.update(updatePatientCommand);
        patientRepository.save(patient);
    }

    public List<VisitDto> getAllVisits(String email) {
        Patient patient = findPatientByUserEmail(email);
        return patient.getVisits().stream()
                .map(visitMapper::mapToDto)
                .toList();
    }

    private Patient findPatientByUserEmail(String email) {
        return patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }
}
