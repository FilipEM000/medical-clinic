package com.FilipEM000.medical_clinic.validator;

import com.FilipEM000.medical_clinic.command.create.CreateVisitCommand;
import com.FilipEM000.medical_clinic.exception.DateInThePastException;
import com.FilipEM000.medical_clinic.exception.InvalidDateException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VisitValidator {
    public static void validateVisitData(CreateVisitCommand createVisitCommand) {
        LocalDateTime startDate = createVisitCommand.startDate();
        LocalDateTime endDate = createVisitCommand.endDate();

        if (startDate == null || endDate == null) {
            throw new InvalidDateException("Date cannot be null");
        }

        if (startDate.isBefore(LocalDateTime.now())) {
            throw new DateInThePastException("Date is already in the past");
        }

        int minute = startDate.getMinute();
        if(minute % 15 != 0) {
            throw new InvalidDateException("Date should be in the quarter of an hour");
        }

        if (endDate.isBefore(startDate)) {
            throw new InvalidDateException("End date cannot be before start date");
        }
    }
}
