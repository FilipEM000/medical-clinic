package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.update.ChangePasswordCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateUserCommand;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.UserDto;
import com.FilipEM000.medical_clinic.exception.UserNotFoundException;
import com.FilipEM000.medical_clinic.mapper.UserMapper;
import com.FilipEM000.medical_clinic.model.User;
import com.FilipEM000.medical_clinic.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public PageDto<UserDto> getAllUsers(Pageable pageable) {
        log.info("Fetching all users");
        Page<UserDto> page = userJpaRepository.findAll(pageable)
                .map(userMapper::mapToDto);
        log.info("Fetched {} users successfully", page.getContent().size());
        return new PageDto<>(page);
    }

    public UserDto getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        User user = findUserByEmail(email);
        log.info("Fetched user successfully");
        return userMapper.mapToDto(user);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordCommand changePasswordCommand) {
        log.info("Process of changing password for user '{}' started", email);
        User user = findUserByEmail(email);
        user.changePassword(changePasswordCommand.password());
        userJpaRepository.save(user);
        log.info("Password for user '{}' changed successfully", email);
    }

    @Transactional
    public void updateUser(String email, UpdateUserCommand updateUserCommand) {
        log.info("Process of updating user '{}' started", email);
        User user = findUserByEmail(email);
        user.update(updateUserCommand);
        userJpaRepository.save(user);
        log.info("User '{}' updated successfully", email);
    }

    private User findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
