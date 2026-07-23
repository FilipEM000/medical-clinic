package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.ChangePasswordCommand;
import com.FilipEM000.medical_clinic.command.CreatePatientCommand;
import com.FilipEM000.medical_clinic.command.UpdatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Zwróć wszystkich pacjentów")
    @ApiResponse(responseCode = "200", description = "Znaleziono pacjentów",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PatientDto.class)))
    @GetMapping
    public List<PatientDto> getAll() {
        return patientService.getAllPatients();
    }

    @Operation(summary = "Zwróć pacjenta po emailu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacjent znaleziony",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono pacjenta")

    })
    @GetMapping("/{email}")
    public PatientDto getByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
    }

    @Operation(summary = "Stwórz nowego pacjenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pacjent utworzony poprawnie",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Podano błędne dane")})
    @PostMapping
    @ResponseStatus(CREATED)
    public PatientDto create(@RequestBody CreatePatientCommand createPatientCommand) {
        return patientService.createPatient(createPatientCommand);
    }

    @Operation(summary = "Usuń pacjenta po emailu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pacjent usunięty poprawnie"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono pacjenta")
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(NO_CONTENT)
    public void deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @Operation(summary = "Zamień pacjenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacjent poprawnie zaktualizowany",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono pacjenta",
                    content = @Content)
    })
    @PutMapping("/{email}")
    public PatientDto updatePatient(@PathVariable String email, @RequestBody UpdatePatientCommand updatePatientCommand) {
        return patientService.updatePatient(email, updatePatientCommand);
    }

    @Operation(summary = "Zmień haslo pacjenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Haslo zaaktualizowane"),
            @ApiResponse(responseCode = "404", description = "Pacjent nie znaleziony",
                    content = @Content)
    })
    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/{email}/password")
    public void changePassword(@PathVariable String email, @RequestBody ChangePasswordCommand changePasswordCommand) {
        patientService.changePassword(email, changePasswordCommand.password());
    }
}
