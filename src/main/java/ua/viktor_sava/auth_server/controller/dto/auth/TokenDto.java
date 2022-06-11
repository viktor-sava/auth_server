package ua.viktor_sava.auth_server.controller.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {

    private String refreshToken;

    private String accessToken;

}
