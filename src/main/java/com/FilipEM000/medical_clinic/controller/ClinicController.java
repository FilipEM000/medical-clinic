package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.create.CreateClinicCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateClinicCommand;
import com.FilipEM000.medical_clinic.dto.ClinicDto;
import com.FilipEM000.medical_clinic.service.ClinicService;
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
@RequestMapping("/clinics")
@RequiredArgsConstructor
public class ClinicController {
    private final ClinicService clinicService;

    @Operation(description = "Zwróć wszystkie kliniki")
    @ApiResponse(responseCode = "200", description = "Znaleziono kliniki",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClinicDto.class)))
    @GetMapping
    public List<ClinicDto> getAll() {
        return clinicService.getAllClinics();
    }

    @Operation(description = "Zwróć klinikę po nazwie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Klinika znaleziona",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDto.class))),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono kliniki")
    })
    @GetMapping("/{name}")
    public ClinicDto getByName(@PathVariable String name) {
        return clinicService.getClinicByName(name);
    }

    @Operation(description = "Stwórz nową klinikę")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Klinika utworzona",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicDto.class))),
            @ApiResponse(responseCode = "400", description = "Podano błędne dane")
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public ClinicDto create(@RequestBody CreateClinicCommand createClinicCommand) {
        return clinicService.createClinic(createClinicCommand);
    }

    @Operation(description = "Usuń klinikę")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Klinika usunięta"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono kliniki")
    })
    @DeleteMapping("/{name}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable String name) {
        clinicService.deleteClinic(name);
    }

    @Operation(summary = "Zaktualizuj klinikę")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Klinika pomyślnie zaktualizowana"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono kliniki")
    })
    @PutMapping("/{name}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable String name, @RequestBody UpdateClinicCommand updateClinicCommand) {
        clinicService.updateClinic(name, updateClinicCommand);
    }
}
