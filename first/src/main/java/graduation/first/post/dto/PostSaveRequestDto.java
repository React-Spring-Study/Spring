package graduation.first.post.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSaveRequestDto {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotNull
    private Long categoryId;

//TODO:    @NotNull
//    private Long writerId;
}
