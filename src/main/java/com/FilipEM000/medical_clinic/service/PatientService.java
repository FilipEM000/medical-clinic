package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.CreatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.command.UpdatePatientCommand;
import com.FilipEM000.medical_clinic.exception.PatientNotFoundException;
import com.FilipEM000.medical_clinic.mapper.PatientMapper;
import com.FilipEM000.medical_clinic.model.Patient;
import com.FilipEM000.medical_clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::mapToDto)
                .toList();
    }

    public PatientDto getPatientByEmail(String email) {
        Patient patient = findPatientByEmail(email);
        return patientMapper.mapToDto(patient);
    }

    public PatientDto createPatient(CreatePatientCommand dto) {
        Patient patient = patientMapper.mapToEntity(dto);
        Patient saved = patientRepository.save(patient);
        return patientMapper.mapToDto(saved);
    }

    public void deletePatient(String email) {
        Patient patient = findPatientByEmail(email);
        patientRepository.remove(patient);
    }

    public PatientDto updatePatient(String email, UpdatePatientCommand updatePatientCommand) {
        Patient patient = findPatientByEmail(email);
        patient.update(updatePatientCommand);
        return patientMapper.mapToDto(patient);
    }

    public PatientDto changePassword(String email, String password) {
        Patient patient = findPatientByEmail(email);
        patient.changePassword(password);
        return patientMapper.mapToDto(patient);
    }

    private Patient findPatientByEmail(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(String.format("Nie znaleziono pacjenta o emailu %s", email)));
    }
}
