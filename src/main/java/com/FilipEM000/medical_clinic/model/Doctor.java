package com.FilipEM000.medical_clinic.model;

import com.FilipEM000.medical_clinic.command.update.UpdateDoctorCommand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "doctors")
@NoArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue()
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String specialization;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Doctor_Clinic",
            joinColumns = {@JoinColumn(name ="doctor_id")},
            inverseJoinColumns = {@JoinColumn(name = "clinic_id")}
    )
    private List<Clinic> clinics;

    public void update(UpdateDoctorCommand updateDoctorCommand) {
        this.specialization = updateDoctorCommand.specialization();
    }
}
