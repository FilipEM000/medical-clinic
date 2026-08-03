package com.FilipEM000.medical_clinic.repository;

import com.FilipEM000.medical_clinic.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicJpaRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findByName(String name);
}
