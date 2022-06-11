package ua.viktor_sava.auth_server.model;

import lombok.*;

import javax.persistence.*;

@Table(name = "role")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

}
