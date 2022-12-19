package graduation.first.user;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String userId);
    User findByProviderId(String providerId);
}
