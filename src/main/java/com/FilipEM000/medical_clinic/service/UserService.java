package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.update.ChangePasswordCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateUserCommand;
import com.FilipEM000.medical_clinic.dto.UserDto;
import com.FilipEM000.medical_clinic.exception.UserNotFoundException;
import com.FilipEM000.medical_clinic.mapper.UserMapper;
import com.FilipEM000.medical_clinic.model.User;
import com.FilipEM000.medical_clinic.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userJpaRepository.findAll(pageable)
                .map(userMapper::mapToDto);
    }

    public UserDto getUserByEmail(String email) {
        User user = findUserByEmail(email);
        return userMapper.mapToDto(user);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordCommand changePasswordCommand) {
        User user = findUserByEmail(email);
        user.changePassword(changePasswordCommand.password());
        userJpaRepository.save(user);
    }

    @Transactional
    public void updateUser(String email, UpdateUserCommand updateUserCommand) {
        User user = findUserByEmail(email);
        user.update(updateUserCommand);
        userJpaRepository.save(user);
    }

    private User findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
