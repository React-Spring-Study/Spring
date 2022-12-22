package graduation.first.post;

import graduation.first.user.User;
import graduation.first.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {

    // 하나씩 조회할 때 사용

    private Long id;
    private String title;
    private String content;
    private UserInfo writerInfo;
    private Long categoryId;

    public static PostResponseDto toDto(Post post) {
        User writer = post.getWriter();
        return new PostResponseDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                new UserInfo(writer.getId(), writer.getName(), writer.getEmail(), writer.getProfileImg()),
                post.getCategory().getId());
    }

}
