package graduation.first.oauth.service;

import com.google.gson.Gson;
import graduation.first.oauth.entity.Provider;
import graduation.first.oauth.entity.Role;
import graduation.first.oauth.entity.UserPrincipal;
import graduation.first.oauth.exception.AuthErrorCode;
import graduation.first.oauth.exception.AuthException;
import graduation.first.oauth.info.OAuth2UserInfo;
import graduation.first.oauth.info.OAuth2UserInfoFactory;
import graduation.first.user.domain.User;
import graduation.first.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final Gson gson;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch(AuthenticationException ex) {
            throw ex;
        } catch(Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    @Transactional
    public OAuth2User getGoogleProfile(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map profile = gson.fromJson(response.getBody(), HashMap.class);
                return this.process(Provider.GOOGLE, profile);
            }
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException("exception!!");
        }
        throw new RuntimeException("!!!");
    }

    private OAuth2User process(Provider provider, Map<String, Object> attributes) {
        // TODO:
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, attributes);
        User savedUser = userRepository.findByUserId(userInfo.getId());

        if (savedUser != null) {
            if (savedUser.getProvider() != provider)
                throw new AuthException(AuthErrorCode.AUTH_PROVIDER_MISS_MATCH);
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, provider);
        }

        return UserPrincipal.create(savedUser, attributes);
    }

    private OAuth2User process(OAuth2UserRequest request, OAuth2User user) {
        Provider provider = Provider.valueOf(request.getClientRegistration().getClientId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, user.getAttributes());
        User savedUser = userRepository.findByUserId(userInfo.getId());

        if (savedUser != null) {
            if (savedUser.getProvider() != provider)
                throw new AuthException(AuthErrorCode.AUTH_PROVIDER_MISS_MATCH);
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, provider);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, Provider provider) {

        User newUser = User.builder()
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("pw1234"))
                .emailVerifiedYn("Y")
                .profileImg(userInfo.getImgUrl())
                .provider(provider)
                .role(Role.USER)
                .userId(userInfo.getId())
                .build();

        return userRepository.save(newUser);
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (user.getName() != null && !user.getName().equals(userInfo.getName())) {
            //TODO: change user's name
        }
        if (user.getProfileImg() != null && !user.getProfileImg().equals(userInfo.getImgUrl())) {
            //TODO: change user's profileImgUrl
        }

        return user;
    }
}
