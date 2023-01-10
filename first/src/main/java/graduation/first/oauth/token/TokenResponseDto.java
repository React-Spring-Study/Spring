package graduation.first.oauth.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {
    private String token;

    public static TokenResponseDto toDto(AuthToken token) {
        return new TokenResponseDto(token.getToken());
    }
}
