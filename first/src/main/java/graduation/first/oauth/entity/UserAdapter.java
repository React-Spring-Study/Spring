package graduation.first.oauth.entity;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.io.Serializable;
import java.util.List;

@Getter
public class UserAdapter extends User implements Serializable {
    private graduation.first.user.domain.User user;

    public UserAdapter(graduation.first.user.domain.User user) {
        super(user.getUserId(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().toString())));
        this.user = user;
    }
}
