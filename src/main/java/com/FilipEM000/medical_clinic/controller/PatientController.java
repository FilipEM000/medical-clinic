package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.dto.PatientCreateDto;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.dto.PatientUpdateDto;
import com.FilipEM000.medical_clinic.model.Patient;
import com.FilipEM000.medical_clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/search")
    public PatientDto getPatentByEmail(@RequestParam String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    public PatientDto createNewPatient(@RequestBody PatientCreateDto dto) {
        return patientService.createNewPatient(dto);
    }

    @DeleteMapping("/search")
    public void deletePatient(@RequestParam String email) {
        patientService.deletePatient(email);
    }

    @PutMapping("/search")
    public PatientDto updatePatient(@RequestParam String email, @RequestBody PatientUpdateDto dto) {
        return patientService.updatePatient(email, dto);
    }
}
