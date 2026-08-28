package com.FilipEM000.medical_clinic.controller;

import com.FilipEM000.medical_clinic.command.get.GetPageCommand;
import com.FilipEM000.medical_clinic.command.update.ChangePasswordCommand;
import com.FilipEM000.medical_clinic.command.update.UpdateUserCommand;
import com.FilipEM000.medical_clinic.dto.PageDto;
import com.FilipEM000.medical_clinic.dto.UserDto;
import com.FilipEM000.medical_clinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Zwróć wszystkich użytkowników")
    @ApiResponse(responseCode = "200", description = "znaleziono użytkowników",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)))
    @GetMapping
    public PageDto<UserDto> getAll(GetPageCommand getPageCommand) {
        log.info("Getting all users with pagination: {}", getPageCommand);
        return userService.getAllUsers(getPageCommand.toPageable());
    }

    @Operation(summary = "Zwróć użytkownika po emailu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "znaleziono użytkownika",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "nie znaleziono użytkownika")
    })
    @GetMapping("/{email}")
    public UserDto getByEmail(@PathVariable String email) {
        log.info("Getting user by email: {}", email);
        return userService.getUserByEmail(email);
    }

    @Operation(summary = "Zmień haslo użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Haslo zaaktualizowane"),
            @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony",
                    content = @Content)
    })
    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/{email}/password")
    public void changePassword(@PathVariable String email, @RequestBody ChangePasswordCommand changePasswordCommand) {
        log.info("Changing password for user with email: {}", email);
        userService.changePassword(email, changePasswordCommand);
    }

    @Operation(summary = "Zaktualizuj użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Użytkownik został zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony")
    })
    @ResponseStatus(NO_CONTENT)
    @PutMapping("/{email}")
    public void update(@PathVariable String email, @RequestBody UpdateUserCommand updateUserCommand) {
        log.info("Updating user with email: {}", email);
        userService.updateUser(email, updateUserCommand);
    }
}
