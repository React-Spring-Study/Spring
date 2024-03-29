package graduation.first.oauth.handler;

import graduation.first.common.config.AppProperties;
import graduation.first.common.utils.CookieUtil;
import graduation.first.oauth.entity.Provider;
import graduation.first.oauth.entity.Role;
import graduation.first.oauth.info.OAuth2UserInfo;
import graduation.first.oauth.info.OAuth2UserInfoFactory;
import graduation.first.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import graduation.first.oauth.token.AuthToken;
import graduation.first.oauth.token.AuthTokenProvider;
import graduation.first.user.domain.UserRefreshToken;
import graduation.first.user.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static graduation.first.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static graduation.first.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if(response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOAuth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                            URI authorizedUri = URI.create(authorizedRedirectUri);
                            if (authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                                    && authorizedUri.getPort() == clientRedirectUri.getPort()) {
                                return true;
                            }
                            return false;
                });
    }

    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("An unauthorized redirect uri. Cannot proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        Provider provider = Provider.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, user.getAttributes());
        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();

        Role role = hasAuthority(authorities, Role.ADMIN.getCode()) ? Role.ADMIN : Role.USER;

        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                userInfo.getId(),
                role.getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));

        /**Refresh Token 설정*/
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        /** 액세스 토큰이랑 리프레시 토큰에 들어갈 정보, DB 저장 */
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userInfo.getId());
        if(userRefreshToken != null) {
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        } else {
            userRefreshToken = new UserRefreshToken(userInfo.getId(), refreshToken.getToken());
            userRefreshTokenRepository.save(userRefreshToken);
        }

        int cookieMaxAge = (int) refreshTokenExpiry/60;

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken.getToken())
                .build().toUriString();
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {

        if (authority == null){
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if(authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
