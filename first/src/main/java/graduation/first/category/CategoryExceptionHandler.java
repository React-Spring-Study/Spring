package graduation.first.category;


import graduation.first.common.response.ErrorEntity;
import graduation.first.oauth.exception.OAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(OAuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity oAuthException(OAuthException ex) {
        log.error("OAuth Exception({}) - {}", ex.getErrorCode(), ex.getErrorMessage());
        return new ErrorEntity(ex.getErrorCode().toString(), ex.getErrorMessage());
    }
}
