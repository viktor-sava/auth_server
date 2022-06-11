package ua.viktor_sava.auth_server.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import ua.viktor_sava.auth_server.controller.dto.auth.AuthResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestControllerAdvice
public class RefreshTokenCookieAdapter implements ResponseBodyAdvice<AuthResponse> {

    @Value("${application.refresh-token.duration}")
    private Integer refreshTokenDuration;

    private void addRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(refreshTokenDuration * 60);
        response.addCookie(cookie);
    }

    private void removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getContainingClass()
                .isAnnotationPresent(TokenCookie.class) && returnType.getMethod() != null;
    }

    @Override
    public AuthResponse beforeBodyWrite(AuthResponse body, MethodParameter returnType, MediaType selectedContentType,
                                        Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                        ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        TokenCookie tokenCookie = returnType.getContainingClass().getAnnotation(TokenCookie.class);
        String name = returnType.getMethod().getName();
        if (Arrays.asList(tokenCookie.add()).contains(name)) {
            addRefreshTokenCookie(httpServletResponse, body.getRefreshToken());
        }
        if (Arrays.asList(tokenCookie.remove()).contains(name)) {
            removeRefreshTokenCookie(httpServletResponse);
        }
        return body;
    }
}
