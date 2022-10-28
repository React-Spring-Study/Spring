package graduation.first.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorEntity {

    private String errorCode;
    private String errorMessage;
}
