package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.create.CreatePatientCommand;
import com.FilipEM000.medical_clinic.command.update.UpdatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.dto.UserDto;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PatientService patientService;

    @Test
    void getAll_dataCorrect_patientsReturned() throws Exception {
        UserDto user = new UserDto(0L, "test_email", "test_first_name", "test_last_name");
        UserDto user2 = new UserDto(1L, "test_email2", "test_first_name2", "test_last_name2");
        PatientDto patient = new PatientDto(0L, "1", "123", LocalDate.of(1999, 5, 2), user);
        PatientDto patient2 = new PatientDto(1L, "2", "456", LocalDate.of(2003, 7, 23), user2);
        Page<PatientDto> page = new PageImpl<>(List.of(patient, patient2));
        PageDto<PatientDto> patients = new PageDto<>(page);
        when(patientService.getAllPatients(any(Pageable.class))).thenReturn(patients);

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(0))
                .andExpect(jsonPath("$.content[0].idCardNo").value("1"))
                .andExpect(jsonPath("$.content[0].phoneNumber").value("123"))
                .andExpect(jsonPath("$.content[0].birthday").value("1999-05-02"))
                .andExpect(jsonPath("$.content[0].user.email").value("test_email"))
                .andExpect(jsonPath("$.content[0].user.firstName").value("test_first_name"))
                .andExpect(jsonPath("$.content[0].user.lastName").value("test_last_name"))
                .andExpect(jsonPath("$.content[1].id").value(1))
                .andExpect(jsonPath("$.content[1].idCardNo").value("2"))
                .andExpect(jsonPath("$.content[1].phoneNumber").value("456"))
                .andExpect(jsonPath("$.content[1].birthday").value("2003-07-23"))
                .andExpect(jsonPath("$.content[1].user.email").value("test_email2"))
                .andExpect(jsonPath("$.content[1].user.firstName").value("test_first_name2"))
                .andExpect(jsonPath("$.content[1].user.lastName").value("test_last_name2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
        verify(patientService).getAllPatients(any(Pageable.class));
    }

    @Test
    void getByEmail_dataCorrect_patientReturned() throws Exception {
        UserDto user = new UserDto(0L, "test_email", "test_first_name", "test_last_name");
        PatientDto patient = new PatientDto(0L, "1", "123", LocalDate.of(2002, 12, 4), user);
        when(patientService.getPatientByEmail(any())).thenReturn(patient);

        mockMvc.perform(get("/patients/{email}", "test_email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.idCardNo").value("1"))
                .andExpect(jsonPath("$.phoneNumber").value("123"))
                .andExpect(jsonPath("$.birthday").value("2002-12-04"))
                .andExpect(jsonPath("$.user.id").value(0))
                .andExpect(jsonPath("$.user.email").value("test_email"))
                .andExpect(jsonPath("$.user.firstName").value("test_first_name"))
                .andExpect(jsonPath("$.user.lastName").value("test_last_name"));
        verify(patientService).getPatientByEmail("test_email");
    }

    @Test
    void getAllVisits_dataCorrect_visitsReturned() throws Exception {
        VisitDto visitDto = new VisitDto(
                0L,
                LocalDateTime.of(2026, 10, 2, 15, 15, 0),
                LocalDateTime.of(2026, 10, 2, 15, 30, 0),
                null,
                null);
        VisitDto visitDto2 = new VisitDto(
                1L,
                LocalDateTime.of(2026, 10, 2, 20, 15, 0),
                LocalDateTime.of(2026, 10, 2, 20, 30, 0),
                null,
                null);
        List<VisitDto> visits = List.of(visitDto, visitDto2);
        when(patientService.getAllVisits("test_email")).thenReturn(visits);

        mockMvc.perform(get("/patients/{email}/visits", "test_email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(0))
                .andExpect(jsonPath("$[0].startDate").value("2026-10-02T15:15:00"))
                .andExpect(jsonPath("$[0].endDate").value("2026-10-02T15:30:00"))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].startDate").value("2026-10-02T20:15:00"))
                .andExpect(jsonPath("$[1].endDate").value("2026-10-02T20:30:00"));
        verify(patientService).getAllVisits("test_email");
    }

    @Test
    void create_dataCorrect_patientCreated() throws Exception {
        CreatePatientCommand createPatientCommand = new CreatePatientCommand("test", "test", "test", "test", "test", "test", LocalDate.of(1999, 8, 10));
        UserDto user = new UserDto(0L, "test_email", "test_first_name", "test_last_name");
        PatientDto patient = new PatientDto(0L, "1", "123", LocalDate.of(2002, 12, 4), user);
        when(patientService.createPatient(createPatientCommand)).thenReturn(patient);

        mockMvc.perform(post("/patients")
                        .content(objectMapper.writeValueAsString(createPatientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.idCardNo").value("1"))
                .andExpect(jsonPath("$.phoneNumber").value("123"))
                .andExpect(jsonPath("$.birthday").value("2002-12-04"))
                .andExpect(jsonPath("$.user.id").value(0))
                .andExpect(jsonPath("$.user.email").value("test_email"))
                .andExpect(jsonPath("$.user.firstName").value("test_first_name"))
                .andExpect(jsonPath("$.user.lastName").value("test_last_name"));
        verify(patientService).createPatient(createPatientCommand);
    }

    @Test
    void delete_dataCorrect_patientDeleted() throws Exception {
        mockMvc.perform(delete("/patients/{email}", "test_email"))
                .andExpect(status().isNoContent());
        verify(patientService).deletePatient("test_email");
    }

    @Test
    void update_dataCorrect_patientUpdated() throws Exception {
        UpdatePatientCommand updatePatientCommand = new UpdatePatientCommand("new_phone_number", "new_id_card", LocalDate.of(2000, 12, 5));

        mockMvc.perform(put("/patients/{email}", "test_email")
                        .content(objectMapper.writeValueAsString(updatePatientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(patientService).updatePatient("test_email", updatePatientCommand);
    }
}
