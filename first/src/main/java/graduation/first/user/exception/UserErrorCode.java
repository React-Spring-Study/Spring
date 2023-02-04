package graduation.first.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode {
    USER_NOT_PERMITTED("권한이 없는 사용자입니다."),
    USER_NOT_FOUND("사용자를 DB에서 찾을 수 없습니다.");

    private String defaultErrorMessage;
}
