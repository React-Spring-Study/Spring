package graduation.first.user;

import graduation.first.oauth.entity.Provider;
import graduation.first.oauth.entity.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="user_id", unique = true)
    private String userId;
    private String name;
    private String email;
    private String profileImg;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Provider provider;
    private String providerId;

    @Builder
    public User (String name, String email, String profileImg, Role role, Provider provider, String providerId) {
        this.name = name;
        this.email = email;
        this.profileImg = profileImg;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
