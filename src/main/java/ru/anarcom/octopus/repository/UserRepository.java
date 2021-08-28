package ru.anarcom.octopus.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.anarcom.octopus.model.User;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link User}.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
