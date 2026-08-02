package com.FilipEM000.medical_clinic.mapper;

import com.FilipEM000.medical_clinic.dto.UserDto;
import com.FilipEM000.medical_clinic.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto mapToDto(User user);

    User mapToEntity(UserDto userDto);
}
