package graduation.first.oauth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuthErrorCode {

    O_AUTH_PROVIDER_MISS_MATCH("소셜 로그인 provider정보가 일치하지 않습니다.");

    private String defaultErrorMessage;
}
