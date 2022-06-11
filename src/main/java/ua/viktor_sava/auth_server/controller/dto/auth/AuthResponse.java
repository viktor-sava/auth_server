package ua.viktor_sava.auth_server.controller.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ua.viktor_sava.auth_server.controller.dto.UserDto;

@Builder
@Data
@AllArgsConstructor
public class AuthResponse {

    private UserDto user;

    private String refreshToken;

    private String accessToken;

}
