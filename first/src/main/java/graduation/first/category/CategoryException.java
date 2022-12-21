package graduation.first.category;

import graduation.first.oauth.exception.OAuthErrorCode;
import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException{

    private CategoryErrorCode errorCode;
    private String errorMessage;

    public CategoryException(CategoryErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CategoryException(CategoryErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
