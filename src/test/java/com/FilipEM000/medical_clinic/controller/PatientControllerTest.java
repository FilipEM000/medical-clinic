package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PatientService patientService;

    @Test
    void getAll_dataCorrect_patientsReturned() throws Exception {
        PatientDto patient = new PatientDto(0L, "1", "123", null, null);
        PatientDto patient2 = new PatientDto(1L, "2", "456", null, null);
        List<PatientDto> patients = List.of(patient, patient2);
        when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
