package com.FilipEM000.medical_clinic.model;

import com.FilipEM000.medical_clinic.dto.UpdatePatientCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class Patient {
    private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;

    public void update(UpdatePatientCommand updatePatientCommand) {
        setFirstName(updatePatientCommand.firstName());
        setLastName(updatePatientCommand.lastName());
        setPassword(updatePatientCommand.password());
    }

    public void changePassword(String password) {
        setPassword(password);
    }
}
