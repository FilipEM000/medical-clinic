package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreateVisitCommand;
import com.FilipEM000.medical_clinic.command.update.AssignPatientCommand;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.exception.*;
import com.FilipEM000.medical_clinic.mapper.*;
import com.FilipEM000.medical_clinic.model.Doctor;
import com.FilipEM000.medical_clinic.model.Patient;
import com.FilipEM000.medical_clinic.model.Visit;
import com.FilipEM000.medical_clinic.repository.DoctorJpaRepository;
import com.FilipEM000.medical_clinic.repository.PatientJpaRepository;
import com.FilipEM000.medical_clinic.repository.VisitJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VisitServiceTest {
    VisitService visitService;
    VisitJpaRepository visitJpaRepository;
    DoctorJpaRepository doctorJpaRepository;
    PatientJpaRepository patientJpaRepository;
    VisitMapper visitMapper;
    PatientMapper patientMapper;
    DoctorMapper doctorMapper;
    UserMapper userMapper;
    ClinicMapper clinicMapper;

    @BeforeEach
    void setup() {
        this.clinicMapper = Mappers.getMapper(ClinicMapper.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        ReflectionTestUtils.setField(doctorMapper, "userMapper", userMapper);
        ReflectionTestUtils.setField(doctorMapper, "clinicMapper", clinicMapper);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        ReflectionTestUtils.setField(patientMapper, "userMapper", userMapper);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        ReflectionTestUtils.setField(visitMapper, "patientMapper", patientMapper);
        ReflectionTestUtils.setField(visitMapper, "doctorMapper", doctorMapper);
        this.visitJpaRepository = Mockito.mock(VisitJpaRepository.class);
        this.doctorJpaRepository = Mockito.mock(DoctorJpaRepository.class);
        this.patientJpaRepository = Mockito.mock(PatientJpaRepository.class);
        this.visitService = new VisitService(visitJpaRepository, doctorJpaRepository, patientJpaRepository, visitMapper);
    }

    @Test
    void getAllVisits_dataCorrect_VisitsReturned() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Visit visit = new Visit(0L, null, null, null, null);
        Visit visit2 = new Visit(1L, null, null, null, null);
        Page<Visit> visits = new PageImpl<>(List.of(visit, visit2));
        when(visitJpaRepository.findAll(pageable)).thenReturn(visits);

        //when
        Page<VisitDto> result = visitService.getAllVisits(pageable);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(0L, result.getContent().getFirst().id()),
                () -> Assertions.assertNull(result.getContent().getFirst().startDate()),
                () -> Assertions.assertNull(result.getContent().getFirst().endDate()),
                () -> Assertions.assertNull(result.getContent().getFirst().patient()),
                () -> Assertions.assertNull(result.getContent().getFirst().doctor()),
                () -> Assertions.assertEquals(1L, result.getContent().get(1).id()),
                () -> Assertions.assertNull(result.getContent().get(1).startDate()),
                () -> Assertions.assertNull(result.getContent().get(1).endDate()),
                () -> Assertions.assertNull(result.getContent().get(1).patient()),
                () -> Assertions.assertNull(result.getContent().get(1).doctor())
        );
    }

    @Test
    void createVisit_dataCorrect_visitCreated() {
        //given
        LocalDateTime startDate = LocalDateTime.of(2026, 12, 24, 18, 0);
        LocalDateTime endDate = LocalDateTime.of(2026, 12, 24, 18, 15);
        CreateVisitCommand createVisitCommand = new CreateVisitCommand(startDate, endDate, 1L);
        Visit visit = new Visit(0L, startDate, endDate, null, null);
        Doctor doctor = new Doctor(0L, null, "cardiologist", null, new ArrayList<>());
        when(visitJpaRepository.existsByDoctorIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(any(), any(), any())).thenReturn(false);
        when(doctorJpaRepository.findById(any())).thenReturn(Optional.of(doctor));
        when(visitJpaRepository.save(any())).thenReturn(visit);

        //when
        VisitDto result = visitService.createVisit(createVisitCommand);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(0L, result.id()),
                () -> Assertions.assertEquals(startDate, result.startDate()),
                () -> Assertions.assertEquals(endDate, result.endDate()),
                () -> Assertions.assertNull(result.patient()),
                () -> Assertions.assertNull(result.doctor())
        );
    }

    @Test
    void createVisit_visitAlreadyExists_throwsException() {
        //given
        LocalDateTime startDate = LocalDateTime.of(2026, 12, 24, 18, 0);
        LocalDateTime endDate = LocalDateTime.of(2026, 12, 24, 18, 15);
        CreateVisitCommand createVisitCommand = new CreateVisitCommand(startDate, endDate, 1L);
        when(visitJpaRepository.existsByDoctorIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(any(), any(), any()))
                .thenReturn(true);

        //then
        assertThatExceptionOfType(VisitAlreadyExistsException.class)
                .isThrownBy(() -> visitService.createVisit(createVisitCommand))
                .extracting(VisitAlreadyExistsException::getMessage)
                .isEqualTo("Doctor already has visit at that time");
    }

    @Test
    void assignPatient_dataCorrect_patientAssigned() {
        //given
        LocalDateTime startDate = LocalDateTime.of(2026, 12, 24, 18, 0);
        LocalDateTime endDate = LocalDateTime.of(2026, 12, 24, 18, 15);
        Visit visit = new Visit(0L, startDate, endDate, null, null);
        Patient patient = new Patient(0L, "1", "123", null, null, new ArrayList<>());
        AssignPatientCommand assignPatientCommand = new AssignPatientCommand("test");
        when(visitJpaRepository.findById(any())).thenReturn(Optional.of(visit));
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(patient));

        //when
        visitService.assignPatient(0L, assignPatientCommand);

        //then
        Assertions.assertAll(
                () -> verify(visitJpaRepository).findById(0L),
                () -> verify(patientJpaRepository).findByUserEmail("test"),
                () -> Assertions.assertEquals(visit, patient.getVisits().getFirst()),
                () -> Assertions.assertEquals(patient, visit.getPatient()),
                () -> verify(visitJpaRepository).save(visit)
        );
    }

    @Test
    void assignPatient_visitNotFound_throwsException() {
        //given
        AssignPatientCommand assignPatientCommand = new AssignPatientCommand("test");
        when(visitJpaRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(VisitNotFoundException.class)
                .isThrownBy(() -> visitService.assignPatient(0L, assignPatientCommand))
                .extracting(VisitNotFoundException::getMessage)
                .isEqualTo("Visit with id '0' not found");
    }

    @Test
    void assignPatient_patientNotFound_throwsException() {
        //given
        LocalDateTime startDate = LocalDateTime.of(2026, 12, 24, 18, 0);
        LocalDateTime endDate = LocalDateTime.of(2026, 12, 24, 18, 15);
        AssignPatientCommand assignPatientCommand = new AssignPatientCommand("test_email");
        Visit visit = new Visit(0L, startDate, endDate, null, null);
        when(visitJpaRepository.findById(any())).thenReturn(Optional.of(visit));
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(PatientNotFoundException.class)
                .isThrownBy(() -> visitService.assignPatient(0L, assignPatientCommand))
                .extracting(PatientNotFoundException::getMessage)
                .isEqualTo("Patient with email 'test_email' not found");
    }

    @Test
    void assignPatient_visitAlreadyTaken_throwsException() {
        //given
        LocalDateTime startDate = LocalDateTime.of(2026, 12, 24, 18, 0);
        LocalDateTime endDate = LocalDateTime.of(2026, 12, 24, 18, 15);
        AssignPatientCommand assignPatientCommand = new AssignPatientCommand("test_email");
        Patient patient = new Patient(0L, "1", "123", null, null, new ArrayList<>());
        Visit visit = new Visit(0L, startDate, endDate, patient, null);
        when(visitJpaRepository.findById(any())).thenReturn(Optional.of(visit));
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(patient));

        //then
        assertThatExceptionOfType(VisitAlreadyTakenException.class)
                .isThrownBy(() -> visitService.assignPatient(0L, assignPatientCommand))
                .extracting(VisitAlreadyTakenException::getMessage)
                .isEqualTo("Visit with id 0 is already taken");
    }

    @Test
    void assignPatient_dateInThePast_throwsException() {
        //given
        LocalDateTime startDate = LocalDateTime.of(2025, 12, 24, 18, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 12, 24, 18, 15);
        AssignPatientCommand assignPatientCommand = new AssignPatientCommand("test_email");
        Patient patient = new Patient(0L, "1", "123", null, null, new ArrayList<>());
        Visit visit = new Visit(0L, startDate, endDate, null, null);
        when(visitJpaRepository.findById(any())).thenReturn(Optional.of(visit));
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(patient));

        //then
        assertThatExceptionOfType(DateInThePastException.class)
                .isThrownBy(() -> visitService.assignPatient(0L, assignPatientCommand))
                .extracting(DateInThePastException::getMessage)
                .isEqualTo("Visit is already in the past");
    }
}
