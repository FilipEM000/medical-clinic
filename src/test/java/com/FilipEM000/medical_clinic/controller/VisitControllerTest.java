package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.create.CreateVisitCommand;
import com.FilipEM000.medical_clinic.command.update.AssignPatientCommand;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.service.VisitService;
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
public class VisitControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    VisitService visitService;

    @Test
    void getAll_dataCorrect_visitsReturned() throws Exception {
        VisitDto visit = new VisitDto(0L, LocalDateTime.of(2026, 12, 5, 15, 15, 0), LocalDateTime.of(2026, 12, 5, 15, 30, 0), null, null);
        VisitDto visit2 = new VisitDto(1L, LocalDateTime.of(2027, 12, 5, 15, 15, 0), LocalDateTime.of(2027, 12, 5, 15, 30, 0), null, null);
        Page<VisitDto> visits = new PageImpl<>(List.of(visit, visit2));
        when(visitService.getAllVisits(any(Pageable.class))).thenReturn(visits);

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(0))
                .andExpect(jsonPath("$.content[0].startDate").value("2026-12-05T15:15:00"))
                .andExpect(jsonPath("$.content[0].endDate").value("2026-12-05T15:30:00"))
                .andExpect(jsonPath("$.content[1].id").value(1))
                .andExpect(jsonPath("$.content[1].startDate").value("2027-12-05T15:15:00"))
                .andExpect(jsonPath("$.content[1].endDate").value("2027-12-05T15:30:00"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
        verify(visitService).getAllVisits(any(Pageable.class));
    }

    @Test
    void create_dataCorrect_visitReturned() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand(null, null, null);

        mockMvc.perform(post("/visits")
                        .content(objectMapper.writeValueAsString(createVisitCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(visitService).createVisit(createVisitCommand);
    }

    @Test
    void assignPatient_dataCreate_patientAssigned() throws Exception {
        AssignPatientCommand assignPatientCommand = new AssignPatientCommand("test_email");

        mockMvc.perform(patch("/visits/{id}", 0L)
                        .content(objectMapper.writeValueAsString(assignPatientCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(visitService).assignPatient(0L, assignPatientCommand);
    }
}