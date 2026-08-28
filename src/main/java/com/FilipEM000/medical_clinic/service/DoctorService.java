package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.create.CreateDoctorCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateDoctorCommand;
import com.FilipEM000.medical_clinic.dto.DoctorDto;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.exception.ClinicNotFoundException;
import com.FilipEM000.medical_clinic.exception.DoctorNotFoundException;
import com.FilipEM000.medical_clinic.mapper.DoctorMapper;
import com.FilipEM000.medical_clinic.model.Clinic;
import com.FilipEM000.medical_clinic.model.Doctor;
import com.FilipEM000.medical_clinic.repository.ClinicJpaRepository;
import com.FilipEM000.medical_clinic.repository.DoctorJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorJpaRepository doctorRepository;
    private final ClinicJpaRepository clinicRepository;
    private final DoctorMapper doctorMapper;


    public PageDto<DoctorDto> getAllDoctors(Pageable pageable) {
        log.info("Fetching all doctors");
        Page<DoctorDto> page = doctorRepository.findAll(pageable)
                .map(doctorMapper::mapToDto);
        log.info("Fetched {} doctors successfully", page.getContent().size());
        return new PageDto<>(page);
    }

    public DoctorDto getDoctorByEmail(String email) {
        log.info("Fetching doctor by email: {}", email);
        Doctor doctor = findDoctorByEmail(email);
        log.info("Fetched successfully");
        return doctorMapper.mapToDto(doctor);
    }

    @Transactional
    public DoctorDto createDoctor(CreateDoctorCommand createDoctorCommand) {
        log.info("Process of creating doctor started");
        Doctor doctor = doctorMapper.mapToEntity(createDoctorCommand);
        Doctor saved = doctorRepository.save(doctor);
        log.info("Process of creating doctor completed successfully");
        return doctorMapper.mapToDto(saved);
    }

    public void deleteDoctor(String email) {
        log.info("Process of deleting doctor '{}' started", email);
        Doctor doctor = findDoctorByEmail(email);
        doctorRepository.delete(doctor);
        log.info("Doctor '{}' deleted successfully", email);
    }

    @Transactional
    public void updateDoctor(String email, UpdateDoctorCommand updateDoctorCommand) {
        log.info("Process of updating doctor '{}' started", email);
        Doctor doctor = findDoctorByEmail(email);
        doctor.update(updateDoctorCommand);
        doctorRepository.save(doctor);
        log.info("Doctor '{}' updated successfully", email);
    }

    @Transactional
    public void assignClinic(String doctorEmail, String name) {
        log.info("Process of assigning doctor '{}' to clinic '{}' started", doctorEmail, name);
        Doctor doctor = findDoctorByEmail(doctorEmail);
        Clinic clinic = clinicRepository.findByName(name)
                .orElseThrow(() -> new ClinicNotFoundException(name));
        doctor.addClinic(clinic);
        doctorRepository.save(doctor);
        log.info("Doctor '{}' assigned to clinic '{}' successfully", doctorEmail, name);
    }

    @Transactional
    public void unassignClinic(String doctorEmail, String name) {
        log.info("Process of unassigning doctor '{}' from clinic '{}' started", doctorEmail, name);
        Doctor doctor = findDoctorByEmail(doctorEmail);
        Clinic clinic = clinicRepository.findByName(name)
                .orElseThrow(() -> new ClinicNotFoundException(name));
        doctor.removeClinic(clinic);
        doctorRepository.save(doctor);
        log.info("Doctor '{}' unassigned from clinic '{}' successfully", doctorEmail, name);
    }

    private Doctor findDoctorByEmail(String email) {
        return doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException(email));
    }
}
