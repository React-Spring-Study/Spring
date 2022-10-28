package graduation.first.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class PostSaveRequestDto {

    @NotEmpty
    private String title;

    private String content;

    @NotNull
    private Long category_id;
}
