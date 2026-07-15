package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.dto.PatientCreateDto;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.dto.PatientUpdateDto;
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

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public PatientDto getPatientByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono pacjenta o emailu " + email));
        return PatientMapper.mapToDto(patient);
    }

    public PatientDto createNewPatient(PatientCreateDto dto) {
        Patient patient = PatientMapper.mapToEntity(dto);
        Patient saved = patientRepository.save(patient);
        return PatientMapper.mapToDto(saved);
    }

    public void deletePatient(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono pacjenta o emialu" + email));

        patientRepository.remove(patient);
    }

    public PatientDto updatePatient(String email, PatientUpdateDto dto) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono pacjenta o emailu " + email));

        patient.setFirstName(dto.firstName());
        patient.setLastName(dto.lastName());
        patient.setPassword(dto.password());
        return PatientMapper.mapToDto(patient);
    }
}
