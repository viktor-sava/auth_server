package ua.viktor_sava.auth_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.viktor_sava.auth_server.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
