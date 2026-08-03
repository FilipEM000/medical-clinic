package com.FilipEM000.medical_clinic.repository;

import com.FilipEM000.medical_clinic.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorJpaRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserEmail(String email);
}

