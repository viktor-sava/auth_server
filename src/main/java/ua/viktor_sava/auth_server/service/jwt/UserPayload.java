package ua.viktor_sava.auth_server.service.jwt;

import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.viktor_sava.auth_server.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPayload implements JwtHandler<UserPayload> {

    private Long id;

    private String email;

    public UserPayload(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }

    @Override
    public UserPayload onPlaintextJwt(Jwt<Header, String> jwt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserPayload onClaimsJwt(Jwt<Header, Claims> jwt) {
        Claims body = jwt.getBody();
        return new UserPayload(body.get("id", Long.class), body.get("email", String.class));
    }

    @Override
    public UserPayload onPlaintextJws(Jws<String> jws) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserPayload onClaimsJws(Jws<Claims> jws) {
        Claims body = jws.getBody();
        return new UserPayload(body.get("id", Long.class), body.get("email", String.class));
    }
}
