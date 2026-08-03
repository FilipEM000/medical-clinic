package com.FilipEM000.medical_clinic.command.update;

import java.time.LocalDate;

public record UpdatePatientCommand(
        String phoneNumber,
        String idCardNo,
        LocalDate birthDay) {
}