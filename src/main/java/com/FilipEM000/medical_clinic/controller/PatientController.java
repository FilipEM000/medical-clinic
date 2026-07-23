package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.ChangePasswordCommand;
import com.FilipEM000.medical_clinic.command.CreatePatientCommand;
import com.FilipEM000.medical_clinic.command.UpdatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.model.Patient;
import com.FilipEM000.medical_clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public List<PatientDto> getAll() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{email}")
    public PatientDto getByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public PatientDto create(@RequestBody CreatePatientCommand createPatientCommand) {
        return patientService.createPatient(createPatientCommand);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(NO_CONTENT)
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @PutMapping("/{email}")
    public PatientDto updatePatient(@PathVariable String email, @RequestBody UpdatePatientCommand updatePatientCommand) {
        return patientService.updatePatient(email, updatePatientCommand);
    }

    @PatchMapping("/{email}/password")
    public PatientDto changePassword(@PathVariable String email, @RequestBody ChangePasswordCommand changePasswordCommand) {
        return patientService.changePassword(email, changePasswordCommand.password());
    }
}
