package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreateDoctorCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateDoctorCommand;
import com.FilipEM000.medical_clinic.dto.DoctorDto;
import com.FilipEM000.medical_clinic.exception.ClinicNotFoundException;
import com.FilipEM000.medical_clinic.exception.DoctorNotFoundException;
import com.FilipEM000.medical_clinic.mapper.DoctorMapper;
import com.FilipEM000.medical_clinic.model.Clinic;
import com.FilipEM000.medical_clinic.model.Doctor;
import com.FilipEM000.medical_clinic.repository.ClinicJpaRepository;
import com.FilipEM000.medical_clinic.repository.DoctorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorJpaRepository doctorRepository;
    private final ClinicJpaRepository clinicRepository;
    private final DoctorMapper doctorMapper;


    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::mapToDto)
                .toList();
    }

    public DoctorDto getDoctorByEmail(String email) {
        Doctor doctor = findDoctorByEmail(email);
        return doctorMapper.mapToDto(doctor);
    }

    public DoctorDto createDoctor(CreateDoctorCommand createDoctorCommand) {
        Doctor doctor = doctorMapper.mapToEntity(createDoctorCommand);
        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.mapToDto(saved);
    }

    public void deleteDoctor(String email) {
        Doctor doctor = findDoctorByEmail(email);
        doctorRepository.delete(doctor);
    }

    public void updateDoctor(String email, UpdateDoctorCommand updateDoctorCommand) {
        Doctor doctor = findDoctorByEmail(email);
        doctor.update(updateDoctorCommand);
        doctorRepository.save(doctor);
    }

    public void assignClinic(String doctorEmail, String name) {
        Doctor doctor = findDoctorByEmail(doctorEmail);
        Clinic clinic = clinicRepository.findByName(name)
                .orElseThrow(() -> new ClinicNotFoundException(String.format("Nie znalzeiono kliniki o nazwie %s", name)));
        doctor.getClinics().add(clinic);
        doctorRepository.save(doctor);
    }

    public void unassignClinic(String doctorEmail, String name) {
        Doctor doctor = findDoctorByEmail(doctorEmail);
        Clinic clinic = clinicRepository.findByName(name)
                .orElseThrow(() -> new ClinicNotFoundException(String.format("Nie znalzeiono kliniki o nazwie %s", name)));
        doctor.getClinics().remove(clinic);
        doctorRepository.save(doctor);
    }

    private Doctor findDoctorByEmail(String email) {
        return doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException(String.format("Nie znaleziono doktora o emailu %s", email)));
    }
}
