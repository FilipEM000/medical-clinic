package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.UpdateUserCommand;
import com.FilipEM000.medical_clinic.dto.UserDto;
import com.FilipEM000.medical_clinic.exception.UserNotFoundException;
import com.FilipEM000.medical_clinic.mapper.UserMapper;
import com.FilipEM000.medical_clinic.model.User;
import com.FilipEM000.medical_clinic.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userJpaRepository.findAll().stream()
                .map(userMapper::mapToDto)
                .toList();
    }

    public UserDto getUserByEmail(String email) {
        User user = findUserByEmail(email);
        return userMapper.mapToDto(user);
    }

    public UserDto changePassword(String email, String password) {
        User user = findUserByEmail(email);
        user.changePassword(password);
        userJpaRepository.save(user);
        return userMapper.mapToDto(user);
    }

    public UserDto updateUser(String email, UpdateUserCommand updateUserCommand) {
        User user = findUserByEmail(email);
        user.update(updateUserCommand);
        userJpaRepository.save(user);
        return userMapper.mapToDto(user);
    }

    private User findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("Nie znaleziono użytkownika o emailu %s", email)));
    }
}
