package ua.viktor_sava.auth_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.viktor_sava.auth_server.controller.dto.UserDto;
import ua.viktor_sava.auth_server.mapper.UserMapper;
import ua.viktor_sava.auth_server.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    private List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(userMapper::userToDto)
                .collect(Collectors.toList());
    }

}
