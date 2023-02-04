package graduation.first.user.repository;

import graduation.first.user.domain.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {

    UserRefreshToken findByUserId(String userId);
    UserRefreshToken findByUserIdAndRefreshToken(Long id, String refreshToken);
}
