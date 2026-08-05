package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreateVisitCommand;
import com.FilipEM000.medical_clinic.command.update.AssignPatientCommand;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitJpaRepository visitRepository;
    private final DoctorJpaRepository doctorRepository;
    private final PatientJpaRepository patientRepository;
    private final VisitMapper visitMapper;

    public List<VisitDto> getAllVisits() {
        return visitRepository.findAll().stream()
                .map(visitMapper::mapToDto)
                .toList();
    }

    public VisitDto createVisit(CreateVisitCommand createVisitCommand) {
        VisitValidator.validateVisitData(createVisitCommand);

        if (visitRepository.existsByDoctorIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(createVisitCommand.doctor(), createVisitCommand.startDate(), createVisitCommand.endDate())) {
            throw new VisitAlreadyExistsException("Doctor already has visit at that time");
        }

        Doctor doctor = doctorRepository.findById(createVisitCommand.doctor())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
        Visit visit = visitMapper.mapToEntity(createVisitCommand);
        visit.setDoctor(doctor);
        Visit saved = visitRepository.save(visit);
        return visitMapper.mapToDto(saved);
    }

    public void assignPatient(Long visitId, AssignPatientCommand assignPatientCommand) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException(String.format("Visit with id %s was not found", visitId)));

        Patient patient = patientRepository.findByUserEmail(assignPatientCommand.patientEmail())
                .orElseThrow(() -> new PatientNotFoundException(
                        String.format("Patient with email %s was not found", assignPatientCommand.patientEmail())));

        if (visit.getPatient() != null) {
            throw new VisitAlreadyTakenException(String.format("Visit with id %s is already taken", visitId));
        }

        if (visit.getStartDate().isBefore(LocalDateTime.now())) {
            throw new DateInThePastException("Visit is already in the past");
        }

        visit.setPatient(patient);
        visitRepository.save(visit);
    }
}
