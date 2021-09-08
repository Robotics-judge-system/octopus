package ru.anarcom.octopus.service;


import java.util.List;
import ru.anarcom.octopus.model.User;

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
