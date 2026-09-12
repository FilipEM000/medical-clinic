package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.create.CreateClinicCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateClinicCommand;
import com.FilipEM000.medical_clinic.dto.ClinicDto;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.service.ClinicService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClinicControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ClinicService clinicService;

    @Test
    void getAll_dataCorrect_clinicsReturned() throws Exception {
        ClinicDto clinic = new ClinicDto(0L, "test_name", "test_city", "test_postcode", "test_street", "test_street_number");
        ClinicDto clinic2 = new ClinicDto(1L, "test_name_2", "test_city_2", "test_postcode_2", "test_street_2", "test_street_number_2");
        Page<ClinicDto> page = new PageImpl<>(List.of(clinic, clinic2));
        PageDto<ClinicDto> clinics = new PageDto<>(page);
        when(clinicService.getAllClinics(any(Pageable.class))).thenReturn(clinics);

        mockMvc.perform(get("/clinics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(0))
                .andExpect(jsonPath("$.content[0].name").value("test_name"))
                .andExpect(jsonPath("$.content[0].city").value("test_city"))
                .andExpect(jsonPath("$.content[0].postcode").value("test_postcode"))
                .andExpect(jsonPath("$.content[0].street").value("test_street"))
                .andExpect(jsonPath("$.content[0].streetNumber").value("test_street_number"))
                .andExpect(jsonPath("$.content[1].id").value(1))
                .andExpect(jsonPath("$.content[1].name").value("test_name_2"))
                .andExpect(jsonPath("$.content[1].city").value("test_city_2"))
                .andExpect(jsonPath("$.content[1].postcode").value("test_postcode_2"))
                .andExpect(jsonPath("$.content[1].street").value("test_street_2"))
                .andExpect(jsonPath("$.content[1].streetNumber").value("test_street_number_2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
        verify(clinicService).getAllClinics(any(Pageable.class));
    }

    @Test
    void getByName_dataCorrect_clinicReturned() throws Exception {
        ClinicDto clinic = new ClinicDto(0L, "test_name", "test_city", "test_postcode", "test_street", "test_street_number");
        when(clinicService.getClinicByName("test_name")).thenReturn(clinic);

        mockMvc.perform(get("/clinics/{name}", "test_name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.name").value("test_name"))
                .andExpect(jsonPath("$.city").value("test_city"))
                .andExpect(jsonPath("$.postcode").value("test_postcode"))
                .andExpect(jsonPath("$.street").value("test_street"))
                .andExpect(jsonPath("$.streetNumber").value("test_street_number"));
        verify(clinicService).getClinicByName("test_name");
    }

    @Test
    void create_dataCorrect_clinicCreated() throws Exception {
        CreateClinicCommand createClinicCommand = new CreateClinicCommand("test_name", "test_city", "test_postcode", "test_street", "test_street_number");
        ClinicDto clinic = new ClinicDto(0L, "test_name", "test_city", "test_postcode", "test_street", "test_street_number");
        when(clinicService.createClinic(createClinicCommand)).thenReturn(clinic);

        mockMvc.perform(post("/clinics")
                        .content(objectMapper.writeValueAsString(createClinicCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.name").value("test_name"))
                .andExpect(jsonPath("$.city").value("test_city"))
                .andExpect(jsonPath("$.postcode").value("test_postcode"))
                .andExpect(jsonPath("$.street").value("test_street"))
                .andExpect(jsonPath("$.streetNumber").value("test_street_number"));
        verify(clinicService).createClinic(createClinicCommand);
    }

    @Test
    void delete_dataCorrect_clinicDeleted() throws Exception {
        mockMvc.perform(delete("/clinics/{name}", "test_name"))
                .andExpect(status().isNoContent());
        verify(clinicService).deleteClinic("test_name");
    }

    @Test
    void update_dataCorrect_clinicUpdated() throws Exception {
        UpdateClinicCommand updateClinicCommand = new UpdateClinicCommand("test_name", "test_city", "test_postcode", "test_street", "test_street_number");

        mockMvc.perform(put("/clinics/{name}", "test_name")
                        .content(objectMapper.writeValueAsString(updateClinicCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(clinicService).updateClinic("test_name", updateClinicCommand);
    }
}

