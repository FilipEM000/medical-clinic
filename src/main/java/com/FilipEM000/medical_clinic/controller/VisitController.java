package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.create.CreateVisitCommand;
import com.FilipEM000.medical_clinic.command.get.GetPageCommand;
import com.FilipEM000.medical_clinic.command.update.AssignPatientCommand;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @Operation(description = "Return all visits")
    @GetMapping
    public PageDto<VisitDto> getAll(GetPageCommand getPageCommand) {
        log.info("Getting all visits with pagination: {}", getPageCommand);
        return visitService.getAllVisits(getPageCommand.toPageable());
    }

    @Operation(description = "Create new visit")
    @PostMapping
    @ResponseStatus(CREATED)
    public VisitDto create(@RequestBody CreateVisitCommand createVisitCommand) {
        log.info("Creating new visit: {}", createVisitCommand);
        return visitService.createVisit(createVisitCommand);
    }

    @Operation(description = "Assign patient to visit")
    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void assignPatient(@PathVariable Long id, @RequestBody AssignPatientCommand assignPatientCommand) {
        log.info("Assigning patient to visit with id: {}", id);
        visitService.assignPatient(id, assignPatientCommand);
    }
}
