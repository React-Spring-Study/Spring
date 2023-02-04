package graduation.first.oauth.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    public static TokenResponse of(AuthToken accessToken, AuthToken refreshToken) {
        return new TokenResponse(accessToken.getToken(), refreshToken.getToken());
    }
}
