package com.FilipEM000.medical_clinic.repository;

import com.FilipEM000.medical_clinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VisitJpaRepository extends JpaRepository<Visit, Long> {
    boolean existsByDoctorIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(Long doctorId, LocalDateTime startDate, LocalDateTime endDate);
}
