package graduation.first.oauth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.List;

@Getter
public class UserAdapter extends User implements Serializable {
    private graduation.first.user.domain.User user;

    public UserAdapter(graduation.first.user.domain.User user) {
        super(user.getUserId(),
                PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("{bcrypt}pw1234"),
                List.of(new SimpleGrantedAuthority(user.getRole().toString())));
        this.user = user;
    }
}
