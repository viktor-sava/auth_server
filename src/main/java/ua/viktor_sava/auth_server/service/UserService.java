package ua.viktor_sava.auth_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.viktor_sava.auth_server.controller.dto.auth.AuthResponse;
import ua.viktor_sava.auth_server.controller.dto.auth.TokenDto;
import ua.viktor_sava.auth_server.exception.EntityExistsException;
import ua.viktor_sava.auth_server.exception.EntityNotFoundException;
import ua.viktor_sava.auth_server.exception.TokenNotValidException;
import ua.viktor_sava.auth_server.mapper.UserMapper;
import ua.viktor_sava.auth_server.model.User;
import ua.viktor_sava.auth_server.repository.UserRepository;
import ua.viktor_sava.auth_server.service.jwt.UserPayload;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final TokenService tokenService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EntityExistsException("User with email {} already exists", user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        TokenDto tokenDto = tokenService.generateTokens(new UserPayload(user.getId(), user.getEmail()));
        tokenService.saveToken(user, tokenDto.getRefreshToken());

        return AuthResponse.builder()
                .user(userMapper.userToDto(user))
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }

    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email {} and password is not found", email));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new EntityNotFoundException("User with email {} and password is not found", email);
        }
        TokenDto tokenDto = tokenService.generateTokens(new UserPayload(user.getId(), user.getEmail()));
        tokenService.saveToken(user, tokenDto.getRefreshToken());

        return AuthResponse.builder()
                .user(userMapper.userToDto(user))
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }

    public void logout(String refreshToken) {
        tokenService.deleteToken(refreshToken);
    }

    public AuthResponse refresh(String refreshToken) {
        UserPayload userPayload = tokenService.validateRefreshToken(refreshToken);
        if (!tokenService.existsToken(refreshToken) || userPayload == null) {
            throw new TokenNotValidException("Refresh token is not valid");
        }
        User user = userRepository.findById(userPayload.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with email {} and password is not found", userPayload.getEmail()));
        TokenDto tokenDto = tokenService.generateTokens(new UserPayload(user));
        return AuthResponse.builder()
                .user(userMapper.userToDto(user))
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email {} and password is not found", email));
    }
}
