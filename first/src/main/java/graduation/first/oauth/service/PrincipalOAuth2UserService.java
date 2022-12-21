package graduation.first.oauth.service;

import graduation.first.oauth.entity.Provider;
import graduation.first.oauth.entity.Role;
import graduation.first.oauth.entity.UserPrincipal;
import graduation.first.oauth.exception.OAuthErrorCode;
import graduation.first.oauth.exception.OAuthException;
import graduation.first.oauth.info.OAuth2UserInfo;
import graduation.first.oauth.info.OAuth2UserInfoFactory;
import graduation.first.user.User;
import graduation.first.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

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

    private OAuth2User process(OAuth2UserRequest request, OAuth2User user) {
        Provider provider = Provider.valueOf(request.getClientRegistration().getClientId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, user.getAttributes());
        User savedUser = userRepository.findByUserId(userInfo.getId());

        if (savedUser != null) {
            if (savedUser.getProvider() != provider)
                throw new OAuthException(OAuthErrorCode.O_AUTH_PROVIDER_MISS_MATCH);
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
                .emailVerifiedYn("Y")
                .profileImg(userInfo.getImgUrl())
                .provider(provider)
                .role(Role.USER)
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
