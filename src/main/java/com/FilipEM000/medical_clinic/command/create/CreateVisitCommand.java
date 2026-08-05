package com.FilipEM000.medical_clinic.command.create;

import java.time.LocalDateTime;

public record CreateVisitCommand(
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long doctor
) {
}
