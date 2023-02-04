package graduation.first.oauth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private AuthErrorCode errorCode;
    private String errorMessage;

    public AuthException(AuthErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }

    public AuthException(AuthErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AuthException(AuthErrorCode errorCode, Throwable cause) {
        super(errorCode.getDefaultErrorMessage(), cause);
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultErrorMessage();
    }

    public AuthException(AuthErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
    }
}
