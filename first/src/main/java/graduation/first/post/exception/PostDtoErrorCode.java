package graduation.first.post.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostDtoErrorCode {

    BAD_INPUT("올바른 입력값이 아닙니다.");

    private String defaultMessage;
}
