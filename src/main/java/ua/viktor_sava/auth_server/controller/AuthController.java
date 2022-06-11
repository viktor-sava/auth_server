package ua.viktor_sava.auth_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.viktor_sava.auth_server.config.interceptor.TokenCookie;
import ua.viktor_sava.auth_server.controller.dto.UserDto;
import ua.viktor_sava.auth_server.controller.dto.auth.AuthResponse;
import ua.viktor_sava.auth_server.controller.dto.auth.TokenDto;
import ua.viktor_sava.auth_server.mapper.UserMapper;
import ua.viktor_sava.auth_server.service.UserService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@TokenCookie(add = {"register", "login", "refreshToken"}, remove = {"logout"})
public class AuthController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("register")
    public AuthResponse register(@RequestBody UserDto userDto, HttpServletResponse response) {
        return userService.register(userMapper.dtoToUser(userDto));
    }

    @PostMapping("login")
    public AuthResponse login(@RequestBody UserDto userDto) {
        return userService.login(userDto.getEmail(), userDto.getPassword());
    }

    @PostMapping("logout")
    public void logout(@RequestBody TokenDto tokenDto) {
        userService.logout(tokenDto.getRefreshToken());
    }

    @PostMapping("refresh")
    public AuthResponse refreshToken(@RequestBody TokenDto tokenDto) {
        return userService.refresh(tokenDto.getRefreshToken());
    }

}
