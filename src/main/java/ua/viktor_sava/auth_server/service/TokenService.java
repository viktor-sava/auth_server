package ua.viktor_sava.auth_server.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.viktor_sava.auth_server.controller.dto.auth.TokenDto;
import ua.viktor_sava.auth_server.model.RefreshToken;
import ua.viktor_sava.auth_server.model.User;
import ua.viktor_sava.auth_server.repository.RefreshTokenRepository;
import ua.viktor_sava.auth_server.service.jwt.UserPayload;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${application.refresh-token.secret}")
    private String refreshTokenSecret;

    @Value("${application.refresh-token.duration}")
    private Integer refreshTokenDuration;

    @Value("${application.access-token.secret}")
    private String accessTokenSecret;

    @Value("${application.access-token.duration}")
    private Integer accessTokenDuration;

    public TokenDto generateTokens(UserPayload user) {
        String jwtRefreshToken = getToken(user, refreshTokenDuration, refreshTokenSecret);
        String jwtAccessToken = getToken(user, accessTokenDuration, accessTokenSecret);
        return new TokenDto(jwtRefreshToken, jwtAccessToken);
    }

    public UserPayload validateRefreshToken(String token) {
        try {
            UserPayload userPayload = new UserPayload();
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(refreshTokenSecret.getBytes()))
                    .build()
                    .parse(token, userPayload);
        } catch (Exception e) {
            return null;
        }
    }

    public UserPayload validateAccessToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(accessTokenSecret.getBytes()))
                    .build()
                    .parse(token, new UserPayload());
        } catch (Exception e) {
            return null;
        }
    }

    public void saveToken(User user, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElse(RefreshToken.builder()
                        .user(user)
                        .token(token)
                        .build());
        refreshToken.setToken(token);
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public boolean existsToken(String token) {
        return refreshTokenRepository.existsByToken(token);
    }

    private String getToken(UserPayload user, Integer duration, String secret) {
        return Jwts.builder()
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .setIssuedAt(getTime())
                .setExpiration(getTime(duration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    private Date getTime() {
        return Calendar.getInstance()
                .getTime();
    }

    private Date getTime(Integer duration) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, duration);
        return instance.getTime();
    }

}
