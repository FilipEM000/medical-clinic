package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreateVisitCommand;
import com.FilipEM000.medical_clinic.command.update.AssignPatientCommand;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.exception.*;
import com.FilipEM000.medical_clinic.mapper.VisitMapper;
import com.FilipEM000.medical_clinic.model.Doctor;
import com.FilipEM000.medical_clinic.model.Patient;
import com.FilipEM000.medical_clinic.model.Visit;
import com.FilipEM000.medical_clinic.repository.DoctorJpaRepository;
import com.FilipEM000.medical_clinic.repository.PatientJpaRepository;
import com.FilipEM000.medical_clinic.repository.VisitJpaRepository;
import com.FilipEM000.medical_clinic.validator.VisitValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitJpaRepository visitRepository;
    private final DoctorJpaRepository doctorRepository;
    private final PatientJpaRepository patientRepository;
    private final VisitMapper visitMapper;

    public PageDto<VisitDto> getAllVisits(Pageable pageable) {
        log.info("Fetching all visits");
        Page<VisitDto> page = visitRepository.findAll(pageable)
                .map(visitMapper::mapToDto);
        log.info("Fetched {} visits successfully", page.getContent().size());
        return new PageDto<>(page);
    }

    @Transactional
    public VisitDto createVisit(CreateVisitCommand createVisitCommand) {
        log.info("Process of creating visit started");
        VisitValidator.validateVisitData(createVisitCommand);

        if (visitRepository.existsByDoctorIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(createVisitCommand.doctor(), createVisitCommand.startDate(), createVisitCommand.endDate())) {
            throw new VisitAlreadyExistsException("Doctor already has visit at that time");
        }

        Doctor doctor = doctorRepository.findById(createVisitCommand.doctor())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
        Visit visit = visitMapper.mapToEntity(createVisitCommand);
        doctor.addVisit(visit);
        Visit saved = visitRepository.save(visit);
        log.info("Process of creating visit completed successfully");
        return visitMapper.mapToDto(saved);
    }

    @Transactional
    public void assignPatient(Long visitId, AssignPatientCommand assignPatientCommand) {
        log.info("Process of booking visit started for visit ID: {} and patient email: {}", visitId, assignPatientCommand.patientEmail());
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException(String.valueOf(visitId)));

        Patient patient = patientRepository.findByUserEmail(assignPatientCommand.patientEmail())
                .orElseThrow(() -> new PatientNotFoundException(assignPatientCommand.patientEmail()));

        if (visit.getPatient() != null) {
            throw new VisitAlreadyTakenException(String.format("Visit with id %s is already taken", visitId));
        }

        if (visit.getStartDate().isBefore(LocalDateTime.now())) {
            throw new DateInThePastException("Visit is already in the past");
        }

        patient.addVisit(visit);
        visitRepository.save(visit);
        log.info("Process of booking visit completed successfully. Visit ID: {}, Patient: {}", visitId, assignPatientCommand.patientEmail());
    }
}
