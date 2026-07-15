package com.FilipEM000.medical_clinic.repository;

import com.FilipEM000.medical_clinic.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PatientRepository {
    private final List<Patient> patients;

    public List<Patient> findAll() {
        return Collections.unmodifiableList(patients);
    }

    public Optional<Patient> findByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public Patient save(Patient patient) {
        if (patient != null) {
            patients.add(patient);
        }
        return patient;
    }

    public void remove(Patient patient) {
        patients.remove(patient);
    }
}
