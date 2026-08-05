package com.FilipEM000.medical_clinic.model;

import com.FilipEM000.medical_clinic.command.update.UpdatePatientCommand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue
    private Long id;
    private String idCardNo;
    private String phoneNumber;
    private LocalDate birthday;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "patient")
    private List<Visit> visits;

    public void update(UpdatePatientCommand updatePatientCommand) {
        this.idCardNo = updatePatientCommand.idCardNo();
        this.phoneNumber = updatePatientCommand.phoneNumber();
        this.birthday = updatePatientCommand.birthday();
    }
}
