package ua.viktor_sava.auth_server.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "account_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

}
