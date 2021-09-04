package ru.anarcom.octopus.service;


import java.util.List;
import ru.anarcom.octopus.model.User;

/**
 * Service interface for class {@link User}.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

public interface UserService {

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

    User registerUser(
        String username,
        String email,
        String name,
        String password
    );

}
