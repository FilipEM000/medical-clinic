package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.create.CreateVisitCommand;
import com.FilipEM000.medical_clinic.command.update.AssignPatientCommand;
import com.FilipEM000.medical_clinic.dto.VisitDto;
import com.FilipEM000.medical_clinic.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @Operation(description = "Return all visits")
    @GetMapping
    public Page<VisitDto> getAll(Pageable pageable) {
        return visitService.getAllVisits(pageable);
    }

    @Operation(description = "Create new visit")
    @PostMapping
    @ResponseStatus(CREATED)
    public VisitDto create(@RequestBody CreateVisitCommand createVisitCommand) {
        return visitService.createVisit(createVisitCommand);
    }

    @Operation(description = "Assign patient to visit")
    @PatchMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void assignPatient(@PathVariable Long id, @RequestBody AssignPatientCommand assignPatientCommand) {
        visitService.assignPatient(id, assignPatientCommand);
    }
}
