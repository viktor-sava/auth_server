package ua.viktor_sava.auth_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.viktor_sava.auth_server.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
