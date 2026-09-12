package com.FilipEM000.medical_clinic.service;

import com.FilipEM000.medical_clinic.command.update.ChangePasswordCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateUserCommand;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.UserDto;
import com.FilipEM000.medical_clinic.exception.UserNotFoundException;
import com.FilipEM000.medical_clinic.mapper.UserMapper;
import com.FilipEM000.medical_clinic.model.User;
import com.FilipEM000.medical_clinic.repository.UserJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    UserService userService;
    UserJpaRepository userJpaRepository;
    UserMapper userMapper;

    @BeforeEach
    void setup() {
        this.userJpaRepository = Mockito.mock(UserJpaRepository.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.userService = new UserService(userJpaRepository, userMapper);
    }

    @Test
    void getAllUsers_dataCorrect_usersReturned() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User(0L, "email1", "123", "Adam", "Kafka", null, null);
        User user2 = new User(1L, "email2", "456", "Ola", "Kwiat", null, null);
        Page<User> users = new PageImpl<>(List.of(user, user2));
        when(userJpaRepository.findAll(pageable)).thenReturn(users);

        //when
        PageDto<UserDto> result = userService.getAllUsers(pageable);

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getTotalElements()),
                () -> Assertions.assertEquals(0L, result.getContent().getFirst().id()),
                () -> Assertions.assertEquals("email1", result.getContent().getFirst().email()),
                () -> Assertions.assertEquals("Adam", result.getContent().getFirst().firstName()),
                () -> Assertions.assertEquals("Kafka", result.getContent().getFirst().lastName()),
                () -> Assertions.assertEquals(1L, result.getContent().get(1).id()),
                () -> Assertions.assertEquals("email2", result.getContent().get(1).email()),
                () -> Assertions.assertEquals("Ola", result.getContent().get(1).firstName()),
                () -> Assertions.assertEquals("Kwiat", result.getContent().get(1).lastName())
        );
    }

    @Test
    void getUserByEmail_dataCorrect_userReturned() {
        //given
        User user = new User(0L, "email1", "123", "Adam", "Kafka", null, null);
        when(userJpaRepository.findByEmail(any())).thenReturn(Optional.of(user));

        //when
        UserDto result = userService.getUserByEmail("test");

        //then
        Assertions.assertAll(
                () -> Assertions.assertEquals(0L, result.id()),
                () -> Assertions.assertEquals("email1", result.email()),
                () -> Assertions.assertEquals("Adam", result.firstName()),
                () -> Assertions.assertEquals("Kafka", result.lastName())
        );
    }

    @Test
    void getUserByEmail_userNotFound_throwsException() {
        //given
        when(userJpaRepository.findByEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.getUserByEmail("test_email"))
                .extracting(UserNotFoundException::getMessage)
                .isEqualTo("User with email 'test_email' not found");
    }

    @Test
    void changePassword_dataCorrect_passwordChanged() {
        //given
        User user = new User(0L, "email1", "123", "Adam", "Kafka", null, null);
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand("new_password");
        when(userJpaRepository.findByEmail(any())).thenReturn(Optional.of(user));

        //when
        userService.changePassword("test", changePasswordCommand);

        //then
        Assertions.assertAll(
                () -> verify(userJpaRepository).findByEmail("test"),
                () -> Assertions.assertEquals("new_password", user.getPassword()),
                () -> verify(userJpaRepository).save(user)
        );
    }

    @Test
    void changePassword_userNotFound_throwsException() {
        //given
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand("new_password");
        when(userJpaRepository.findByEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.changePassword("test_email", changePasswordCommand))
                .extracting(UserNotFoundException::getMessage)
                .isEqualTo("User with email 'test_email' not found");
    }

    @Test
    void updateUser_dataCorrect_userUpdated() {
        //given
        User user = new User(0L, "email1", "123", "Adam", "Kafka", null, null);
        UpdateUserCommand updateUserCommand = new UpdateUserCommand("new_email", "new_first_name", "new_last_name");
        when(userJpaRepository.findByEmail(any())).thenReturn(Optional.of(user));

        //when
        userService.updateUser("test", updateUserCommand);

        //then
        Assertions.assertAll(
                () -> verify(userJpaRepository).findByEmail("test"),
                () -> Assertions.assertEquals("new_email", user.getEmail()),
                () -> Assertions.assertEquals("new_first_name", user.getFirstName()),
                () -> Assertions.assertEquals("new_last_name", user.getLastName()),
                () -> verify(userJpaRepository).save(user)
        );
    }

    @Test
    void updateUser_userNotFound_throwsException() {
        //given
        UpdateUserCommand updateUserCommand = new UpdateUserCommand("new_email", "new_first_name", "new_last_name");
        when(userJpaRepository.findByEmail(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.updateUser("test_email", updateUserCommand))
                .extracting(UserNotFoundException::getMessage)
                .isEqualTo("User with email 'test_email' not found");
    }
}
