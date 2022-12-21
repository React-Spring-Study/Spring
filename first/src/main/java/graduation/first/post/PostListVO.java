package graduation.first.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostListVO {
    List<PostResponseVO> postResponseDtoList;

    public static List<PostResponseVO> toResponseDto(List<Post> entities) {
        List<PostResponseVO> responseVOList = new ArrayList<>();
        for (Post post: entities) {
            responseVOList.add(PostResponseVO.toVo(post));
        }
        return responseVOList;
    }
}
