package graduation.first.user;

import graduation.first.oauth.entity.Provider;
import graduation.first.oauth.entity.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    @NotNull
    private String emailVerifiedYn;
    private String profileImg;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Provider provider;

    @Builder
    public User (String userId, String name, String email, String emailVerifiedYn, String profileImg, Role role, Provider provider) {
        this.userId = userId;
        this.name = name;
        this.email =  email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        this.profileImg = profileImg != null ? profileImg : "";
        this.role = role;
        this.provider = provider;
    }
}
