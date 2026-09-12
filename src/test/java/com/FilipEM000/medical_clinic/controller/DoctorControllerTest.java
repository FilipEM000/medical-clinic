package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.create.CreateDoctorCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateDoctorCommand;
import com.FilipEM000.medical_clinic.dto.DoctorDto;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.UserDto;
import com.FilipEM000.medical_clinic.service.DoctorService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    DoctorService doctorService;

    @Test
    void getAll_dataCorrect_doctorsReturned() throws Exception {
        UserDto user = new UserDto(0L, "test_email", "test_first_name", "test_last_name");
        UserDto user2 = new UserDto(1L, "test_email_2", "test_first_name_2", "test_last_name_2");
        DoctorDto doctor = new DoctorDto(0L, "test_specialization", user, new ArrayList<>());
        DoctorDto doctor2 = new DoctorDto(1L, "test_specialization_2", user2, new ArrayList<>());
        Page<DoctorDto> page = new PageImpl<>(List.of(doctor, doctor2));
        PageDto<DoctorDto> doctors = new PageDto<>(page);
        when(doctorService.getAllDoctors(any(Pageable.class))).thenReturn(doctors);

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(0))
                .andExpect(jsonPath("$.content[0].specialization").value("test_specialization"))
                .andExpect(jsonPath("$.content[0].user.id").value(0))
                .andExpect(jsonPath("$.content[0].user.email").value("test_email"))
                .andExpect(jsonPath("$.content[0].user.firstName").value("test_first_name"))
                .andExpect(jsonPath("$.content[0].user.lastName").value("test_last_name"))
                .andExpect(jsonPath("$.content[1].id").value(1))
                .andExpect(jsonPath("$.content[1].specialization").value("test_specialization_2"))
                .andExpect(jsonPath("$.content[1].user.id").value(1))
                .andExpect(jsonPath("$.content[1].user.email").value("test_email_2"))
                .andExpect(jsonPath("$.content[1].user.firstName").value("test_first_name_2"))
                .andExpect(jsonPath("$.content[1].user.lastName").value("test_last_name_2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
        verify(doctorService).getAllDoctors(any(Pageable.class));
    }

    @Test
    void getByEmail_dataCorrect_doctorReturned() throws Exception {
        UserDto user = new UserDto(0L, "test_email", "test_first_name", "test_last_name");
        DoctorDto doctor = new DoctorDto(0L, "test_specialization", user, new ArrayList<>());
        when(doctorService.getDoctorByEmail("test_email")).thenReturn(doctor);

        mockMvc.perform(get("/doctors/{email}", "test_email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.specialization").value("test_specialization"))
                .andExpect(jsonPath("$.user.id").value(0))
                .andExpect(jsonPath("$.user.email").value("test_email"))
                .andExpect(jsonPath("$.user.firstName").value("test_first_name"))
                .andExpect(jsonPath("$.user.lastName").value("test_last_name"));
        verify(doctorService).getDoctorByEmail("test_email");
    }

    @Test
    void create_dataCorrect_doctorCreated() throws Exception {
        CreateDoctorCommand createDoctorCommand = new CreateDoctorCommand(null, null, null, null, null);
        UserDto user = new UserDto(0L, "test_email", "test_first_name", "test_last_name");
        DoctorDto doctor = new DoctorDto(0L, "test_specialization", user, new ArrayList<>());
        when(doctorService.createDoctor(createDoctorCommand)).thenReturn(doctor);

        mockMvc.perform(post("/doctors")
                        .content(objectMapper.writeValueAsString(createDoctorCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.specialization").value("test_specialization"))
                .andExpect(jsonPath("$.user.id").value(0))
                .andExpect(jsonPath("$.user.email").value("test_email"))
                .andExpect(jsonPath("$.user.firstName").value("test_first_name"))
                .andExpect(jsonPath("$.user.lastName").value("test_last_name"));
        verify(doctorService).createDoctor(createDoctorCommand);
    }

    @Test
    void delete_dataCorrect_doctorDeleted() throws Exception {
        mockMvc.perform(delete("/doctors/{email}", "test_email"))
                .andExpect(status().isNoContent());
        verify(doctorService).deleteDoctor("test_email");
    }

    @Test
    void update_dataCorrect_doctorUpdated() throws Exception {
        UpdateDoctorCommand updateDoctorCommand = new UpdateDoctorCommand("test_specialization");

        mockMvc.perform(put("/doctors/{email}", "test_email")
                        .content(objectMapper.writeValueAsString(updateDoctorCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(doctorService).updateDoctor("test_email", updateDoctorCommand);
    }

    @Test
    void assignClinic_dataCorrect_clinicAssigned() throws Exception {
        mockMvc.perform(post("/doctors/{email}/clinics/{clinicName}", "test_email", "test_clinic_name"))
                .andExpect(status().isNoContent());
        verify(doctorService).assignClinic("test_email", "test_clinic_name");
    }

    @Test
    void unassignClinic_dataCorrect_clinicAssigned() throws Exception {
        mockMvc.perform(delete("/doctors/{email}/clinics/{clinicName}", "test_email", "test_clinic_name"))
                .andExpect(status().isNoContent());
        verify(doctorService).unassignClinic("test_email", "test_clinic_name");
    }
}
