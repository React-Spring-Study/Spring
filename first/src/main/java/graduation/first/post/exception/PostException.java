package graduation.first.post.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException{
    private PostErrorCode errorCode;
    private String errorMessage;

    public PostException(PostErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public PostException(PostErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
