package graduation.first.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String profileImg;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User (String name, String email, String profileImg, Role role) {
        this.name = name;
        this.email = email;
        this.profileImg = profileImg;
        this.role = role;
    }
}
