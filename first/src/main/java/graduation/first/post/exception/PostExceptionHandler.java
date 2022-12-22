package graduation.first.post.exception;

import graduation.first.common.response.ErrorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class PostExceptionHandler {

    @ExceptionHandler(PostException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity postException(PostException ex) {
        log.error("Post Exception({}) - {}", ex.getErrorCode(), ex.getErrorMessage());
        return new ErrorEntity(ex.getErrorCode().toString(), ex.getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity postRequestException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        log.error("Dto Validation Exception({}) - {}", PostDtoErrorCode.BAD_INPUT, PostDtoErrorCode.BAD_INPUT.getDefaultMessage());
        return new ErrorEntity(PostDtoErrorCode.BAD_INPUT.toString(),
                PostDtoErrorCode.BAD_INPUT.getDefaultMessage(),
                errors);
    }
}
