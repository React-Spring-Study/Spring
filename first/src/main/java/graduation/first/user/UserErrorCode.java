package graduation.first.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode {
    USER_NOT_PERMITTED("권한이 없는 사용자입니다.");

    private String defaultErrorMessage;
}
