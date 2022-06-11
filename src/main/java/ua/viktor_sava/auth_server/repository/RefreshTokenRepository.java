package ua.viktor_sava.auth_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.viktor_sava.auth_server.model.RefreshToken;
import ua.viktor_sava.auth_server.model.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);

    void deleteByToken(String token);

    boolean existsByToken(String token);
}
