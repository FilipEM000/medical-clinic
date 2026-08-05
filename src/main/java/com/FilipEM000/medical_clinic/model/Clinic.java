package com.FilipEM000.medical_clinic.model;

import com.FilipEM000.medical_clinic.command.update.UpdateClinicCommand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "clinics")
@Entity
@NoArgsConstructor
public class Clinic {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    private String city;
    private String postcode;
    private String street;
    private String streetNumber;
    @ManyToMany(mappedBy = "clinics")
    private List<Doctor> doctors;

    public void update(UpdateClinicCommand updateClinicCommand) {
        this.name = updateClinicCommand.name();
        this.city = updateClinicCommand.city();
        this.postcode = updateClinicCommand.postcode();
        this.street = updateClinicCommand.street();
        this.streetNumber = updateClinicCommand.streetNumber();
    }
}
