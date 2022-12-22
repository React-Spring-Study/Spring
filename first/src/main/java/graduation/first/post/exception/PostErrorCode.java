package graduation.first.post.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostErrorCode {
    CATEGORY_NOT_FOUND("카테고리를 찾을 수 없습니다."),
    WRITER_NOT_FOUND("작성자를 찾을 수 없습니다."),
    POST_NOT_FOUND("해당 포스트를 찾을 수 없습니다.");

    private String defaultErrorMessage;
}
