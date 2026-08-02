package com.FilipEM000.medical_clinic.repository;

import com.FilipEM000.medical_clinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientJpaRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserEmail(String email);
}
