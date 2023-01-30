package graduation.first.post.dto;

import graduation.first.post.domain.Post;
import graduation.first.user.domain.User;
import graduation.first.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    public PostResponseDto(Long id, String title, String content, UserInfo writerInfo, String categoryName, LocalDateTime createdDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writerInfo = writerInfo;
        this.categoryName = categoryName;
        this.createdDate = createdDate;
    }

    // 하나씩 조회할 때 사용

    private Long id;
    private String title;
    private String content;
    private UserInfo writerInfo;
    private String categoryName;
    private LocalDateTime createdDate;
    @Setter
    private List<UploadFileResponse> files;

    public static PostResponseDto toDto(Post post) {
        User writer = post.getWriter();
        return new PostResponseDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                new UserInfo(writer.getId(), writer.getName(), writer.getEmail(), writer.getProfileImg()),
                post.getCategory().getName(),
                post.getCreatedDate());
    }

}
