package graduation.first.common.config;


import graduation.first.oauth.token.AuthTokenProvider;
import graduation.first.oauth.token.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final AuthTokenProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        CorsFilter corsFilter = new CorsFilter();
        TokenAuthenticationFilter customFilter = new TokenAuthenticationFilter(tokenProvider);
        http.addFilterBefore(corsFilter, TokenAuthenticationFilter.class);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
