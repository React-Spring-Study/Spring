package graduation.first.oauth.controller;

import com.amazonaws.services.kms.model.ConnectionErrorCodeType;
import graduation.first.common.config.AppProperties;
import graduation.first.common.utils.CookieUtil;
import graduation.first.oauth.entity.Role;
import graduation.first.oauth.entity.UserPrincipal;
import graduation.first.oauth.exception.AuthErrorCode;
import graduation.first.oauth.exception.AuthException;
import graduation.first.oauth.service.PrincipalOAuth2UserService;
import graduation.first.oauth.token.AuthToken;
import graduation.first.oauth.token.AuthTokenProvider;
import graduation.first.common.utils.HeaderUtil;
import graduation.first.oauth.token.TokenReissueRequest;
import graduation.first.oauth.token.TokenResponse;
import graduation.first.user.domain.User;
import graduation.first.user.domain.UserRefreshToken;
import graduation.first.user.exception.UserErrorCode;
import graduation.first.user.exception.UserException;
import graduation.first.user.repository.UserRefreshTokenRepository;
import graduation.first.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final PrincipalOAuth2UserService oAuth2UserService;
    private final UserRepository userRepository;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @PostMapping("/login/google")
    @Transactional
    public TokenResponse loginV1(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody Map<String, String> tokenMap) {
        OAuth2User oAuth2User = oAuth2UserService.getGoogleProfile(tokenMap.get("access_token"));

        log.info("OAuth2User: [name: {}, attributes: {}]", oAuth2User.getName(), oAuth2User.getAttributes());
        String userId = oAuth2User.getName();
        //TODO: 디비 저장 암호와 passwordEncoder.encode("pw1234") -> credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userId,
                        "pw1234"
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Date now = new Date();

        AuthToken accessToken = tokenProvider.createAuthToken(
                userId,
                ((UserPrincipal) authentication.getPrincipal()).getRole().getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        log.info("userId: {}, role: {}", userId, ((UserPrincipal) authentication.getPrincipal()).getRole().getCode());

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        //userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
        if (userRefreshToken == null) {
            // 없으면 새로 등록
            userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
            userRefreshTokenRepository.save(userRefreshToken);
        } else {
            // DB에 refresh token 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        return TokenResponse.of(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    @Transactional
    public TokenResponse refreshToken(@RequestBody TokenReissueRequest reissueRequest,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        // refresh token 확인
        String refreshToken = HeaderUtil.getAccessToken(request);
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        // access token 확인
        AuthToken expiredToken = tokenProvider.convertAuthToken(reissueRequest.getAccessToken());

        // expired access token 인지 확인
        Claims claims = expiredToken.getExpiredTokenClaims();
        if (claims == null) {
            throw new AuthException(AuthErrorCode.NOT_EXPIRED_TOKEN_YET);
        } else {
            log.info("claims={}", claims);
        }
        String userId = claims.getSubject();
        log.info("expired claims={}", userId);

        Role role = Role.of(claims.get("role", String.class));
        log.info("String userId = claims.getSubject(), userId={}", userId);
/**
        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse((null));

        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);
**/
        if (!authRefreshToken.validate()) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // refresh token으로 DB에서 user 정보와 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        log.info("UserRefreshToken={}", refreshToken);
        if (userRefreshToken == null) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN, "로그아웃됨. 다시 로그인 필요");
        }

        Date now = new Date();

        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                role.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // 토큰 만료기간이 3일 이하인 경우 refresh token 발급
        if (validTime <= THREE_DAYS_MSEC) {
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            // DB에 토큰 업데이트
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());
/**
            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
 **/
        }

        return TokenResponse.of(newAccessToken, authRefreshToken);
    }

    @GetMapping("/unauthorized")
    public void unauthorized() {
        throw new AuthException(AuthErrorCode.UNAUTHORIZED);
    }
}
