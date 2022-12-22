package graduation.first.post.dto;

import graduation.first.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class PostResponseVO {
    //게시물 리스트 조회시 사용

    private Long id;
    private String title;
    private String writerName;

    public static PostResponseVO toVo(Post post) {
        return new PostResponseVO(post.getId(), post.getTitle(), post.getWriter().getName());
    }

    public static Page<PostResponseVO> toVoList(Page<Post> entities) {
        return entities.map(m -> toVo(m));
    }
}