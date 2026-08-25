package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.create.CreatePatientCommand;
import com.FilipEM000.medical_clinic.command.update.UpdatePatientCommand;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.PatientDto;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PageDto<PatientDto> getAll(Pageable pageable) {
        return patientService.getAllPatients(pageable);
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

    @Operation(summary = "Return all guest visits")
    @GetMapping("/{email}/visits")
    public List<VisitDto> getAllVisits(@PathVariable String email) {
        return patientService.getAllVisits(email);
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
    public void delete(@PathVariable String email) {
        patientService.deletePatient(email);
    }

    @Operation(summary = "Zaktualizuj pacjenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacjent poprawnie zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono pacjenta")
    })
    @PutMapping("/{email}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable String email, @RequestBody UpdatePatientCommand updatePatientCommand) {
        patientService.updatePatient(email, updatePatientCommand);
    }
}
