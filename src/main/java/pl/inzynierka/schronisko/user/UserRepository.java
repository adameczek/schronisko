package pl.inzynierka.schronisko.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByUsername(String username);
    Optional<User> findFirstByUsernameOrEmail(String username, String email);
    Optional<User> findFirstByEmail(String email);
}
