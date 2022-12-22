package graduation.first.post.dto;

import graduation.first.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostListVO {
    //아마도 안씀
    List<PostResponseVO> postResponseDtoList;

    public static List<PostResponseVO> toResponseDto(List<Post> entities) {
        List<PostResponseVO> responseVOList = new ArrayList<>();
        for (Post post: entities) {
            responseVOList.add(PostResponseVO.toVo(post));
        }
        return responseVOList;
    }
}
