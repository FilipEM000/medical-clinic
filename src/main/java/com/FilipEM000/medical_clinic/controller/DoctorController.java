package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.create.CreateDoctorCommand;
import com.FilipEM000.medical_clinic.command.get.GetPageCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateDoctorCommand;
import com.FilipEM000.medical_clinic.dto.DoctorDto;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Zwróć wszystkich doktorów")
    @ApiResponse(responseCode = "200", description = "Znaleziono doktorów",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DoctorDto.class)))
    @GetMapping
    public PageDto<DoctorDto> getAll(GetPageCommand getPageCommand) {
        log.info("Getting all doctors with pagination: {}", getPageCommand);
        return doctorService.getAllDoctors(getPageCommand.toPageable());
    }

    @Operation(summary = "Zwróć doktora po emailu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doktor znaleziony",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono doktora")
    })
    @GetMapping("/{email}")
    public DoctorDto getByEmail(@PathVariable String email) {
        log.info("Getting doctor by email: {}", email);
        return doctorService.getDoctorByEmail(email);
    }

    @Operation(description = "Stwórz nowego doktora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doktor utworzony",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))),
            @ApiResponse(responseCode = "400", description = "Podano błędne dane")
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public DoctorDto create(@RequestBody CreateDoctorCommand createDoctorCommand) {
        log.info("Creating new doctor: {}", createDoctorCommand);
        return doctorService.createDoctor(createDoctorCommand);
    }

    @Operation(description = "Usuń doktora po emailu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doktor usunięty"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono doktora")
    })
    @DeleteMapping("/{email}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable String email) {
        log.info("Deleting doctor with email: {}", email);
        doctorService.deleteDoctor(email);
    }

    @Operation(summary = "Zaktualizuj doktora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doktor pomyślnie zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono doktora")
    })
    @PutMapping("/{email}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable String email, @RequestBody UpdateDoctorCommand updateDoctorCommand) {
        log.info("Updating doctor with email: {}", email);
        doctorService.updateDoctor(email, updateDoctorCommand);
    }

    @Operation(description = "Przypisz doktora do kliniki")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doktor został pomyślnie dopisany do kliniki"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono doktora lub kliniki")
    })
    @PostMapping("/{email}/clinics/{clinicName}")
    @ResponseStatus(NO_CONTENT)
    public void assignClinic(@PathVariable String email, @PathVariable String clinicName) {
        log.info("Assigning doctor '{}' to clinic '{}'", email, clinicName);
        doctorService.assignClinic(email, clinicName);
    }

    @Operation(description = "Usuń przypisanie doktora do kliniki")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doktor został pomyślnie wypisany z kliniki"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono doktora lub kliniki")
    })
    @DeleteMapping("/{email}/clinics/{clinicName}")
    @ResponseStatus(NO_CONTENT)
    public void unassignClinic(@PathVariable String email, @PathVariable String clinicName) {
        log.info("Unassigning doctor '{}' from clinic '{}'", email, clinicName);
        doctorService.unassignClinic(email, clinicName);
    }
}
