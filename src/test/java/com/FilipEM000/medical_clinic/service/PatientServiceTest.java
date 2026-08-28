package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreatePatientCommand;
import com.FilipEM000.medical_clinic.command.update.UpdatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.exception.PatientNotFoundException;
import com.FilipEM000.medical_clinic.mapper.DoctorMapper;
import com.FilipEM000.medical_clinic.mapper.PatientMapper;
import com.FilipEM000.medical_clinic.mapper.UserMapper;
import com.FilipEM000.medical_clinic.mapper.VisitMapper;
import com.FilipEM000.medical_clinic.model.Patient;
import com.FilipEM000.medical_clinic.model.Visit;
import com.FilipEM000.medical_clinic.repository.PatientJpaRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceTest {

    PatientService patientService;
    PatientJpaRepository patientJpaRepository;
    PatientMapper patientMapper;
    VisitMapper visitMapper;
    UserMapper userMapper;

    @BeforeEach
    void setup() {
        this.patientJpaRepository = Mockito.mock(PatientJpaRepository.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        ReflectionTestUtils.setField(patientMapper, "userMapper", userMapper);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        ReflectionTestUtils.setField(visitMapper, "patientMapper", patientMapper);
        ReflectionTestUtils.setField(visitMapper, "doctorMapper", Mappers.getMapper(DoctorMapper.class));
        patientService = new PatientService(patientJpaRepository, patientMapper, visitMapper);
    }

    @Test
    void getAllPatients_dataCorrect_patientsReturned() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Patient patient = new Patient(0L, "1", "123", null, null, null);
        Patient patient2 = new Patient(1L, "2", "456", null, null, null);
        Page<Patient> patients = new PageImpl<>(List.of(patient, patient2));
        when(patientJpaRepository.findAll(pageable)).thenReturn(patients);

        //when
        PageDto<PatientDto> result = patientService.getAllPatients(pageable);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(0L, result.getContent().getFirst().id()),
                () -> Assertions.assertEquals("1", result.getContent().getFirst().idCardNo()),
                () -> Assertions.assertEquals("123", result.getContent().getFirst().phoneNumber()),
                () -> Assertions.assertNull(result.getContent().getFirst().birthday()),
                () -> Assertions.assertNull(result.getContent().getFirst().user()),
                () -> Assertions.assertEquals(1L, result.getContent().get(1).id()),
                () -> Assertions.assertEquals("2", result.getContent().get(1).idCardNo()),
                () -> Assertions.assertEquals("456", result.getContent().get(1).phoneNumber()),
                () -> Assertions.assertNull(result.getContent().get(1).birthday()),
                () -> Assertions.assertNull(result.getContent().get(1).user())
        );
    }

    @Test
    void getPatientByEmail_dataCorrect_patientReturned() {
        //given
        Patient patient = new Patient(0L, "1", "123", null, null, null);
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(patient));

        //when
        PatientDto result = patientService.getPatientByEmail("1");

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(0L, result.id()),
                () -> Assertions.assertEquals("1", result.idCardNo()),
                () -> Assertions.assertEquals("123", result.phoneNumber()),
                () -> Assertions.assertNull(result.birthday()),
                () -> Assertions.assertNull(result.user())
        );
    }

    @Test
    void getPatientByEmail_patientNotFound_throwsException() {
        //given
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(PatientNotFoundException.class)
                .isThrownBy(() -> patientService.getPatientByEmail("test_email"))
                .extracting(PatientNotFoundException::getMessage)
                .isEqualTo("Patient with email 'test_email' not found");
    }

    @Test
    void createPatient_dataCorrect_patientCreated() {
        //given
        CreatePatientCommand createPatientCommand = new CreatePatientCommand("f@wp.pl", "1qaz", "Adam", "Bąk", "1", "123", null);
        Patient patient = new Patient(0L, "1", "123", null, null, null);
        when(patientJpaRepository.save(any())).thenReturn(patient);

        //when
        PatientDto result = patientService.createPatient(createPatientCommand);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(0L, result.id()),
                () -> Assertions.assertEquals("1", result.idCardNo()),
                () -> Assertions.assertEquals("123", result.phoneNumber()),
                () -> Assertions.assertNull(result.birthday()),
                () -> Assertions.assertNull(result.user())
        );
    }

    @Test
    void deletePatient_dataCorrect_patientDeleted() {
        //given
        Patient patient = new Patient(0L, "1", "123", null, null, null);
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(patient));

        //when
        patientService.deletePatient("test_email");

        //then
        Assertions.assertAll(
                () -> verify(patientJpaRepository).findByUserEmail("test_email"),
                () -> verify(patientJpaRepository).delete(patient)
        );
    }

    @Test
    void deletePatient_patientNotFound_throwsException() {
        //given
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(PatientNotFoundException.class)
                .isThrownBy(() -> patientService.deletePatient("test_email"))
                .extracting(PatientNotFoundException::getMessage)
                .isEqualTo("Patient with email 'test_email' not found");
    }

    @Test
    void updatePatient_dataCorrect_patientUpdated() {
        //given
        Patient patient = new Patient(0L, "1", "123", null, null, null);
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("new_phone_number", "new_id_card", LocalDate.of(2000, 12, 5));
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(patient));

        //when
        patientService.updatePatient("test", updatePatientCommand);

        //then
        Assertions.assertAll(
                () -> verify(patientJpaRepository).findByUserEmail("test"),
                () -> Assertions.assertEquals("new_phone_number", patient.getPhoneNumber()),
                () -> Assertions.assertEquals("new_id_card", patient.getIdCardNo()),
                () -> Assertions.assertEquals(LocalDate.of(2000, 12, 5), patient.getBirthday()),
                () -> verify(patientJpaRepository).save(patient)
        );
    }

    @Test
    void updatePatient_patientNotFound_throwsException() {
        //given
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("new_phone_number", "new_id_card", LocalDate.of(2000, 12, 5));
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(PatientNotFoundException.class)
                .isThrownBy(() -> patientService.updatePatient("test_email", updatePatientCommand))
                .extracting(PatientNotFoundException::getMessage)
                .isEqualTo("Patient with email 'test_email' not found");
    }

    @Test
    void getAllVisits_dataCorrect_visitsReturned() {
        //given
        Visit visit = new Visit(0L, null, null, null, null);
        Visit visit2 = new Visit(1L, null, null, null, null);
        List<Visit> visits = List.of(visit, visit2);
        Patient patient = new Patient(0L, "1", "123", null, null, visits);
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(patient));

        //when
        List<VisitDto> result = patientService.getAllVisits("test");

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.size()),
                () -> Assertions.assertEquals(0L, result.getFirst().id()),
                () -> Assertions.assertNull(result.getFirst().startDate()),
                () -> Assertions.assertNull(result.getFirst().endDate()),
                () -> Assertions.assertNull(result.getFirst().patient()),
                () -> Assertions.assertNull(result.getFirst().doctor()),
                () -> Assertions.assertEquals(1L, result.get(1).id()),
                () -> Assertions.assertNull(result.get(1).startDate()),
                () -> Assertions.assertNull(result.get(1).endDate()),
                () -> Assertions.assertNull(result.get(1).patient()),
                () -> Assertions.assertNull(result.get(1).doctor())
        );
    }

    @Test
    void getAllVisits_patientNotFound_throwsException() {
        //given
        when(patientJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(PatientNotFoundException.class)
                .isThrownBy(() -> patientService.getAllVisits("test_email"))
                .extracting(PatientNotFoundException::getMessage)
                .isEqualTo("Patient with email 'test_email' not found");
    }
}
