package graduation.first.oauth.exception;

import lombok.Getter;

@Getter
public class OAuthException extends RuntimeException {

    private OAuthErrorCode errorCode;
    private String errorMessage;

    public OAuthException(OAuthErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public OAuthException(OAuthErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
