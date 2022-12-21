package graduation.first.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListVO {
    List<PostResponseDto> postResponseDtoList;
}
