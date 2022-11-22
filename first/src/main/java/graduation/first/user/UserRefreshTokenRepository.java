package graduation.first.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {

    UserRefreshToken findByUserEmail(String email);
    UserRefreshToken findByUserEmailAndRefreshToken();
}
