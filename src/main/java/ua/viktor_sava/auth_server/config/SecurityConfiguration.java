package ua.viktor_sava.auth_server.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.viktor_sava.auth_server.config.filter.JWTFilter;
import ua.viktor_sava.auth_server.controller.dto.ErrorDto;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JWTFilter jwtFilter;

    private final JsonMapper jsonMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http = http
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http = http
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                // Our public endpoints
                .antMatchers("/api/auth/**").permitAll()
                // Our private endpoints
                .anyRequest().authenticated();


        // Add JWT token filter
        http.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            ServletOutputStream outputStream = response.getOutputStream();
            jsonMapper.writeValue(outputStream, new ErrorDto("ForbiddenException", LocalDateTime.now()));
            outputStream.flush();
        };
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ServletOutputStream outputStream = response.getOutputStream();
            jsonMapper.writeValue(outputStream, new ErrorDto("UnauthorizedException", LocalDateTime.now()));
            outputStream.flush();
        };
    }
}
