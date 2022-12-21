package graduation.first.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseVO {

    private Long id;
    private String title;
    private String writerName;

    public static PostResponseVO toVo(Post post) {
        return new PostResponseVO(post.getId(), post.getTitle(), post.getWriter().getName());
    }
}
