package ru.anarcom.octopus.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.anarcom.octopus.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
