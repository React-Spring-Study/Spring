package graduation.first.post.dto;

import graduation.first.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class PostUpdateRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private String categoryName;
}
