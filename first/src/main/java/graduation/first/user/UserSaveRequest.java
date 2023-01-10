package graduation.first.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSaveRequest {

    private Long id;
    private String name;
    private String imgUrl;
}
