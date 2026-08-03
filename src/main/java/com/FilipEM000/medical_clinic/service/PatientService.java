package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreatePatientCommand;
import com.FilipEM000.medical_clinic.command.update.UpdatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.exception.PatientNotFoundException;
import com.FilipEM000.medical_clinic.mapper.PatientMapper;
import com.FilipEM000.medical_clinic.model.Patient;
import com.FilipEM000.medical_clinic.repository.PatientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientJpaRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::mapToDto)
                .toList();
    }

    public PatientDto getPatientByEmail(String email) {
        Patient patient = findPatientByUserEmail(email);
        return patientMapper.mapToDto(patient);
    }

    public PatientDto createPatient(CreatePatientCommand createPatientCommand) {
        Patient patient = patientMapper.mapToEntity(createPatientCommand);
        Patient saved = patientRepository.save(patient);
        return patientMapper.mapToDto(saved);
    }

    public void deletePatient(String email) {
        Patient patient = findPatientByUserEmail(email);
        patientRepository.delete(patient);
    }

    public void updatePatient(String email, UpdatePatientCommand updatePatientCommand) {
        Patient patient = findPatientByUserEmail(email);
        patient.update(updatePatientCommand);
        patientRepository.save(patient);
    }

    private Patient findPatientByUserEmail(String email) {
        return patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(String.format("Nie znaleziono pacjenta o emailu %s", email)));
    }
}
