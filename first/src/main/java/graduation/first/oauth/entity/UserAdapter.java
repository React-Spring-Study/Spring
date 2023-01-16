package graduation.first.oauth.entity;

import graduation.first.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collections;

@Getter
public class UserAdapter extends UserPrincipal implements Serializable {

    public UserAdapter(User user) {
        super(user.getUserId(),
                user.getPassword(),
                user.getProvider(),
                user.getRole(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getCode())));
    }
}
