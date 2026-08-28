package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.update.ChangePasswordCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateUserCommand;
import com.FilipEM000.medical_clinic.dto.DoctorDto;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.UserDto;
import com.FilipEM000.medical_clinic.model.User;
import com.FilipEM000.medical_clinic.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    @Test
    void getAll_dataCorrect_usersReturned() throws Exception {
        UserDto user = new UserDto(0L, "test_email", "test_first_name", "test_last_name");
        UserDto user2 = new UserDto(1L, "test_email_2", "test_first_name_2", "test_last_name_2");
        Page<UserDto> page = new PageImpl<>(List.of(user, user2));
        PageDto<UserDto> users = new PageDto<>(page);

        when(userService.getAllUsers(any(Pageable.class))).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(0))
                .andExpect(jsonPath("$.content[0].email").value("test_email"))
                .andExpect(jsonPath("$.content[0].firstName").value("test_first_name"))
                .andExpect(jsonPath("$.content[0].lastName").value("test_last_name"))
                .andExpect(jsonPath("$.content[1].id").value(1))
                .andExpect(jsonPath("$.content[1].email").value("test_email_2"))
                .andExpect(jsonPath("$.content[1].firstName").value("test_first_name_2"))
                .andExpect(jsonPath("$.content[1].lastName").value("test_last_name_2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
        verify(userService).getAllUsers(any(Pageable.class));
    }

    @Test
    void getByEmail_dataCorrect_userReturned() throws Exception {
        UserDto user = new UserDto(0L, "test_email", "test_first_name", "test_last_name");
        when(userService.getUserByEmail("test_email")).thenReturn(user);

        mockMvc.perform(get("/users/{email}", "test_email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.email").value("test_email"))
                .andExpect(jsonPath("$.firstName").value("test_first_name"))
                .andExpect(jsonPath("$.lastName").value("test_last_name"));
        verify(userService).getUserByEmail("test_email");
    }

    @Test
    void changePassword_dataCorrect_passwordChanged() throws Exception {
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand("test_password");

        mockMvc.perform(patch("/users/{email}/password", "test_email")
                        .content(objectMapper.writeValueAsString(changePasswordCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(userService).changePassword("test_email", changePasswordCommand);
    }

    @Test
    void update_dataCorrect_userUpdated() throws Exception {
        UpdateUserCommand updateUserCommand = new UpdateUserCommand("test_email", "test+first_name", "test_last_name");

        mockMvc.perform(put("/users/{email}", "test_email")
                        .content(objectMapper.writeValueAsString(updateUserCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(userService).updateUser("test_email", updateUserCommand);
    }
}
