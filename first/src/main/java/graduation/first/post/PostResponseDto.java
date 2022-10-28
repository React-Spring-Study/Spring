package graduation.first.post;

import graduation.first.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private UserInfo writerInfo;
    private Long categoryId;

}
