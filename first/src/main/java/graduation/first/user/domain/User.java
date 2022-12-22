package graduation.first.user.domain;

import graduation.first.common.domain.BaseTimeEntity;
import graduation.first.oauth.entity.Provider;
import graduation.first.oauth.entity.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

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
    @Enumerated(EnumType.STRING)
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
