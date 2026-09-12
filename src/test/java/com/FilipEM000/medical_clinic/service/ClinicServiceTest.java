package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreateClinicCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateClinicCommand;
import com.FilipEM000.medical_clinic.dto.ClinicDto;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.exception.ClinicNotFoundException;
import com.FilipEM000.medical_clinic.mapper.ClinicMapper;
import com.FilipEM000.medical_clinic.model.Clinic;
import com.FilipEM000.medical_clinic.repository.ClinicJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClinicServiceTest {
    ClinicService clinicService;
    ClinicJpaRepository clinicJpaRepository;
    ClinicMapper clinicMapper;

    @BeforeEach
    void setup() {
        this.clinicJpaRepository = Mockito.mock(ClinicJpaRepository.class);
        this.clinicMapper = Mappers.getMapper(ClinicMapper.class);
        this.clinicService = new ClinicService(clinicJpaRepository, clinicMapper);
    }

    @Test
    void getAllClinics_dataCorrect_clinicsReturned() {
        //given
        Pageable pageable = PageRequest.of(0,10);
        Clinic clinic = new Clinic(0L, "Clinic", "Poznan", "11-111", "street", "44", null);
        Clinic clinic2 = new Clinic(1L, "Hospital", "Poznan", "22-222", "wide", "63", null);
        Page<Clinic> clinics = new PageImpl<>(List.of(clinic, clinic2));
        when(clinicJpaRepository.findAll(pageable)).thenReturn(clinics);

        //when
        PageDto<ClinicDto> result = clinicService.getAllClinics(pageable);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(0L, result.getContent().getFirst().id()),
                () -> Assertions.assertEquals("Clinic", result.getContent().getFirst().name()),
                () -> Assertions.assertEquals("Poznan", result.getContent().getFirst().city()),
                () -> Assertions.assertEquals("11-111", result.getContent().getFirst().postcode()),
                () -> Assertions.assertEquals("street", result.getContent().getFirst().street()),
                () -> Assertions.assertEquals("44", result.getContent().getFirst().streetNumber()),
                () -> Assertions.assertEquals(1L, result.getContent().get(1).id()),
                () -> Assertions.assertEquals("Hospital", result.getContent().get(1).name()),
                () -> Assertions.assertEquals("Poznan", result.getContent().get(1).city()),
                () -> Assertions.assertEquals("22-222", result.getContent().get(1).postcode()),
                () -> Assertions.assertEquals("wide", result.getContent().get(1).street()),
                () -> Assertions.assertEquals("63", result.getContent().get(1).streetNumber())
        );
    }

    @Test
    void getClinicByName_dataCorrect_clinicReturned() {
        //given
        Clinic clinic = new Clinic(0L, "Clinic", "Poznan", "11-111", "street", "44", null);
        when(clinicJpaRepository.findByName(any())).thenReturn(Optional.of(clinic));

        //when
        ClinicDto result = clinicService.getClinicByName("test");

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(0L, result.id()),
                () -> Assertions.assertEquals("Clinic", result.name()),
                () -> Assertions.assertEquals("Poznan", result.city()),
                () -> Assertions.assertEquals("11-111", result.postcode()),
                () -> Assertions.assertEquals("street", result.street()),
                () -> Assertions.assertEquals("44", result.streetNumber())
        );
    }

    @Test
    void getClinicByName_clinicNotFound_throwsException() {
        //given
        when(clinicJpaRepository.findByName(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ClinicNotFoundException.class)
                .isThrownBy(() -> clinicService.getClinicByName("test_name"))
                .extracting(ClinicNotFoundException::getMessage)
                .isEqualTo("Clinic with name 'test_name' not found");
    }

    @Test
    void createClinic_dataCorrect_clinicCreated() {
        //given
        CreateClinicCommand createClinicCommand = new CreateClinicCommand("name", "city", "postcode", "street", "streetNumber");
        Clinic clinic = new Clinic(0L, "Clinic", "Poznan", "11-111", "street", "44", null);
        when(clinicJpaRepository.save(any())).thenReturn(clinic);

        //when
        ClinicDto result = clinicService.createClinic(createClinicCommand);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(0L, result.id()),
                () -> Assertions.assertEquals("Clinic", result.name()),
                () -> Assertions.assertEquals("Poznan", result.city()),
                () -> Assertions.assertEquals("11-111", result.postcode()),
                () -> Assertions.assertEquals("street", result.street()),
                () -> Assertions.assertEquals("44", result.streetNumber())
        );
    }

    @Test
    void deleteClinic_dataCorrect_clinicDeleted() {
        //given
        Clinic clinic = new Clinic(0L, "Clinic", "Poznan", "11-111", "street", "44", null);
        when(clinicJpaRepository.findByName("Clinic")).thenReturn(Optional.of(clinic));

        //when
        clinicService.deleteClinic("Clinic");

        //then
        Assertions.assertAll(
                () -> verify(clinicJpaRepository).findByName("Clinic"),
                () -> verify(clinicJpaRepository).delete(clinic)
        );
    }

    @Test
    void deleteClinic_clinicNotFound_throwsException() {
        //given
        when(clinicJpaRepository.findByName(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ClinicNotFoundException.class)
                .isThrownBy(() -> clinicService.deleteClinic("test_name"))
                .extracting(ClinicNotFoundException::getMessage)
                .isEqualTo("Clinic with name 'test_name' not found");
    }

    @Test
    void updateClinic_dataCorrect_clinicUpdated() {
        //given
        Clinic clinic = new Clinic(0L, "Clinic", "Poznan", "11-111", "street", "44", null);
        UpdateClinicCommand updateClinicCommand = new UpdateClinicCommand("new_name", "new_city", "new_postcode", "new_street", "new_street_number");
        when(clinicJpaRepository.findByName("Clinic")).thenReturn(Optional.of(clinic));

        //when
        clinicService.updateClinic("Clinic", updateClinicCommand);

        //then
        Assertions.assertAll(
                () -> verify(clinicJpaRepository).findByName("Clinic"),
                () -> Assertions.assertEquals("new_name", clinic.getName()),
                () -> Assertions.assertEquals("new_city", clinic.getCity()),
                () -> Assertions.assertEquals("new_postcode", clinic.getPostcode()),
                () -> Assertions.assertEquals("new_street", clinic.getStreet()),
                () -> Assertions.assertEquals("new_street_number", clinic.getStreetNumber()),
                () -> verify(clinicJpaRepository).save(clinic)
        );
    }

    @Test
    void updateClinic_clinicNotFound_throwsException() {
        //given
        UpdateClinicCommand updateClinicCommand = new UpdateClinicCommand("new_name", "new_city", "new_postcode", "new_street", "new_street_number");
        when(clinicJpaRepository.findByName(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ClinicNotFoundException.class)
                .isThrownBy(() -> clinicService.updateClinic("test_name", updateClinicCommand))
                .extracting(ClinicNotFoundException::getMessage)
                .isEqualTo("Clinic with name 'test_name' not found");
    }
}
