package ifi.realworld.user.domain.repository;

import ifi.realworld.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndUsername(String email, String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}
