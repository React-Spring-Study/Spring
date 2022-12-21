package graduation.first.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {

    private Long id;
    private String name;
    private String email;
    private String profileImg;

    public static UserInfo toResponseDto(User user) {
        return new UserInfo(user.getId(), user.getName(), user.getEmail(), user.getProfileImg());
    }
}
