package graduation.first.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUpdateRequestDto {

    private String title;
    private String content;
    private String categoryName;
}
