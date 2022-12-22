package graduation.first.user.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private UserErrorCode errorCode;
    private String errorMessage;

    public UserException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public UserException(UserErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
