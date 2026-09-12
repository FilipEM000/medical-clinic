package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreateDoctorCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateDoctorCommand;
import com.FilipEM000.medical_clinic.dto.DoctorDto;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.exception.ClinicNotFoundException;
import com.FilipEM000.medical_clinic.exception.DoctorNotFoundException;
import com.FilipEM000.medical_clinic.mapper.ClinicMapper;
import com.FilipEM000.medical_clinic.mapper.DoctorMapper;
import com.FilipEM000.medical_clinic.mapper.UserMapper;
import com.FilipEM000.medical_clinic.model.Clinic;
import com.FilipEM000.medical_clinic.model.Doctor;
import com.FilipEM000.medical_clinic.repository.ClinicJpaRepository;
import com.FilipEM000.medical_clinic.repository.DoctorJpaRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {
    DoctorService doctorService;
    DoctorJpaRepository doctorJpaRepository;
    ClinicJpaRepository clinicJpaRepository;
    DoctorMapper doctorMapper;
    ClinicMapper clinicMapper;
    UserMapper userMapper;

    @BeforeEach
    void setup() {
        this.doctorJpaRepository = Mockito.mock(DoctorJpaRepository.class);
        this.clinicJpaRepository = Mockito.mock(ClinicJpaRepository.class);
        this.clinicMapper = Mappers.getMapper(ClinicMapper.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        ReflectionTestUtils.setField(doctorMapper, "clinicMapper", clinicMapper);
        ReflectionTestUtils.setField(doctorMapper, "userMapper", userMapper);
        this.doctorService = new DoctorService(doctorJpaRepository, clinicJpaRepository, doctorMapper);
    }

    @Test
    void getAllDoctors_dataCorrect_doctorsReturned() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Doctor doctor1 = new Doctor(0L, null, "cardiologist", null, null);
        Doctor doctor2 = new Doctor(1L, null, "surgeon", null, null);
        Page<Doctor> doctors = new PageImpl<>(List.of(doctor1, doctor2));
        when(doctorJpaRepository.findAll(pageable)).thenReturn(doctors);

        //when
        PageDto<DoctorDto> result = doctorService.getAllDoctors(pageable);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(0L, result.getContent().getFirst().id()),
                () -> Assertions.assertNull(result.getContent().getFirst().user()),
                () -> Assertions.assertEquals("cardiologist", result.getContent().getFirst().specialization()),
                () -> Assertions.assertNull(result.getContent().getFirst().clinics()),
                () -> Assertions.assertEquals(1L, result.getContent().get(1).id()),
                () -> Assertions.assertNull(result.getContent().get(1).user()),
                () -> Assertions.assertEquals("surgeon", result.getContent().get(1).specialization()),
                () -> Assertions.assertNull(result.getContent().get(1).clinics())
        );
    }

    @Test
    void getDoctorByEmail_dataCorrect_doctorReturned() {
        //given
        Doctor doctor = new Doctor(0L, null, "cardiologist", null, null);
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(doctor));

        //when
        DoctorDto result = doctorService.getDoctorByEmail("test");

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(0L, result.id()),
                () -> Assertions.assertNull(result.user()),
                () -> Assertions.assertEquals("cardiologist", result.specialization()),
                () -> Assertions.assertNull(result.clinics())
        );
    }

    @Test
    void getDoctorByEmail_doctorNotFound_throwsException() {
        //given
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(DoctorNotFoundException.class)
                .isThrownBy(() -> doctorService.getDoctorByEmail("test_email"))
                .extracting(DoctorNotFoundException::getMessage)
                .isEqualTo("Doctor with email 'test_email' not found");
    }


    @Test
    void createDoctor_dataCorrect_doctorCreated() {
        //given
        CreateDoctorCommand createDoctorCommand = new CreateDoctorCommand("test", "test", "test", "test", "test");
        Doctor doctor = new Doctor(0L, null, "cardiologist", null, null);
        when(doctorJpaRepository.save(any())).thenReturn(doctor);

        //when
        DoctorDto result = doctorService.createDoctor(createDoctorCommand);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(0L, result.id()),
                () -> Assertions.assertNull(result.user()),
                () -> Assertions.assertEquals("cardiologist", result.specialization()),
                () -> Assertions.assertNull(result.clinics())
        );
    }

    @Test
    void deleteDoctor_dataCorrect_doctorDeleted() {
        //given
        Doctor doctor = new Doctor(0L, null, "cardiologist", null, null);
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(doctor));

        //when
        doctorService.deleteDoctor("test");

        //then
        Assertions.assertAll(
                () -> verify(doctorJpaRepository).findByUserEmail("test"),
                () -> verify(doctorJpaRepository).delete(doctor)
        );
    }

    @Test
    void deleteDoctor_doctorNotFound_throwsException() {
        //given
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(DoctorNotFoundException.class)
                .isThrownBy(() -> doctorService.deleteDoctor("test_email"))
                .extracting(DoctorNotFoundException::getMessage)
                .isEqualTo("Doctor with email 'test_email' not found");
    }

    @Test
    void updateDoctor_dataCorrect_doctorUpdated() {
        //given
        Doctor doctor = new Doctor(0L, null, "cardiologist", null, null);
        UpdateDoctorCommand updateDoctorCommand = new UpdateDoctorCommand("new_specialization");
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(doctor));

        //when
        doctorService.updateDoctor("test", updateDoctorCommand);

        //then
        Assertions.assertAll(
                () -> verify(doctorJpaRepository).findByUserEmail("test"),
                () -> Assertions.assertEquals("new_specialization", doctor.getSpecialization()),
                () -> verify(doctorJpaRepository).save(doctor)
        );
    }

    @Test
    void updateDoctor_doctorNotFound_throwsException() {
        //given
        UpdateDoctorCommand updateDoctorCommand = new UpdateDoctorCommand("new_specialization");
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(DoctorNotFoundException.class)
                .isThrownBy(() -> doctorService.updateDoctor("test_email", updateDoctorCommand))
                .extracting(DoctorNotFoundException::getMessage)
                .isEqualTo("Doctor with email 'test_email' not found");
    }

    @Test
    void assignClinic_dataCorrect_clinicAssigned() {
        //given
        Doctor doctor = new Doctor(0L, null, "cardiologist", new ArrayList<>(), null);
        Clinic clinic = new Clinic(0L, "Clinic", "Poznan", "11-111", "street", "44", new ArrayList<>());
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(doctor));
        when(clinicJpaRepository.findByName("Clinic")).thenReturn(Optional.of(clinic));

        //when
        doctorService.assignClinic("test", "Clinic");

        //then
        Assertions.assertAll(
                () -> verify(doctorJpaRepository).findByUserEmail("test"),
                () -> Assertions.assertEquals(clinic, doctor.getClinics().getFirst()),
                () -> Assertions.assertEquals(doctor, clinic.getDoctors().getFirst()),
                () -> verify(doctorJpaRepository).save(doctor)
        );
    }

    @Test
    void assignClinic_doctorNotFound_throwsException() {
        //given
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(DoctorNotFoundException.class)
                .isThrownBy(() -> doctorService.assignClinic("test_email", "Clinic"))
                .extracting(DoctorNotFoundException::getMessage)
                .isEqualTo("Doctor with email 'test_email' not found");
    }

    @Test
    void assignClinic_clinicNotFound_throwsException() {
        //given
        Doctor doctor = new Doctor(0L, null, "cardiologist", new ArrayList<>(), null);
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(doctor));
        when(clinicJpaRepository.findByName(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ClinicNotFoundException.class)
                .isThrownBy(() -> doctorService.assignClinic("test_email", "test_name"))
                .extracting(ClinicNotFoundException::getMessage)
                .isEqualTo("Clinic with name 'test_name' not found");
    }

    @Test
    void unassignClinic_dataCorrect_clinicUnassigned() {
        //given
        Doctor doctor = new Doctor(0L, null, "cardiologist", new ArrayList<>(), null);
        Clinic clinic = new Clinic(0L, "Clinic", "Poznan", "11-111", "street", "44", new ArrayList<>());
        doctor.addClinic(clinic);
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(doctor));
        when(clinicJpaRepository.findByName("Clinic")).thenReturn(Optional.of(clinic));

        //when
        doctorService.unassignClinic("test", "Clinic");

        //then
        Assertions.assertAll(
                () -> verify(doctorJpaRepository).findByUserEmail("test"),
                () -> Assertions.assertEquals(0, doctor.getClinics().size()),
                () -> Assertions.assertEquals(0, clinic.getDoctors().size()),
                () -> verify(doctorJpaRepository).save(doctor)
        );
    }

    @Test
    void unassignClinic_doctorNotFound_throwsException() {
        //given
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(DoctorNotFoundException.class)
                .isThrownBy(() -> doctorService.unassignClinic("test_email", "Clinic"))
                .extracting(DoctorNotFoundException::getMessage)
                .isEqualTo("Doctor with email 'test_email' not found");
    }

    @Test
    void unassignClinic_clinicNotFound_throwsException() {
        //given
        Doctor doctor = new Doctor(0L, null, "cardiologist", new ArrayList<>(), null);
        when(doctorJpaRepository.findByUserEmail(any())).thenReturn(Optional.of(doctor));
        when(clinicJpaRepository.findByName(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ClinicNotFoundException.class)
                .isThrownBy(() -> doctorService.unassignClinic("test_email", "test_name"))
                .extracting(ClinicNotFoundException::getMessage)
                .isEqualTo("Clinic with name 'test_name' not found");
    }
}
