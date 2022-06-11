package ua.viktor_sava.auth_server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.viktor_sava.auth_server.controller.dto.UserDto;
import ua.viktor_sava.auth_server.model.User;

@Mapper
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDto userToDto(User user);

    User dtoToUser(UserDto userDto);
}
