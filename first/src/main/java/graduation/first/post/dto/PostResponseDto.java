package graduation.first.post.dto;

import graduation.first.post.Post;
import graduation.first.user.domain.User;
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
    private String categoryName;

    public static PostResponseDto toDto(Post post) {
        User writer = post.getWriter();
        return new PostResponseDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                new UserInfo(writer.getId(), writer.getName(), writer.getEmail(), writer.getProfileImg()),
                post.getCategory().getName());
    }

}
