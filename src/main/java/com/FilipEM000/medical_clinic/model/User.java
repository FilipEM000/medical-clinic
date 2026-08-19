package com.FilipEM000.medical_clinic.model;

import com.FilipEM000.medical_clinic.command.update.UpdateUserCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @OneToOne(mappedBy = "user")
    private Patient patient;

    @OneToOne(mappedBy = "user")
    private Doctor doctor;

    public void update(UpdateUserCommand updateUserCommand) {
        this.firstName = updateUserCommand.firstName();
        this.lastName = updateUserCommand.lastName();
        this.email = updateUserCommand.email();
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void assignDoctor(Doctor doctor) {
        setDoctor(doctor);
        doctor.setUser(this);
    }

    public void assignPatient(Patient patient) {
        setPatient(patient);
        patient.setUser(this);
    }
}
