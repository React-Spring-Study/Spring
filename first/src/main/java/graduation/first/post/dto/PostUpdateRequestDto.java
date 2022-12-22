package graduation.first.post.dto;

import graduation.first.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUpdateRequestDto {

    private String title;
    private String content;
    private String categoryName;
}
