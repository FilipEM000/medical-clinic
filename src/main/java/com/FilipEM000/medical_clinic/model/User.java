package com.FilipEM000.medical_clinic.model;

import com.FilipEM000.medical_clinic.command.update.UpdateUserCommand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
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
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
