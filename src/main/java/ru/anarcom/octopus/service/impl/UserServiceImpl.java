package ru.anarcom.octopus.service.impl;

import java.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.anarcom.octopus.entity.Status;
import ru.anarcom.octopus.entity.User;
import ru.anarcom.octopus.exceptions.IncorrectPasswordException;
import ru.anarcom.octopus.repository.UserRepository;
import ru.anarcom.octopus.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
//TODO change logger messages
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Clock clock;

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        return result;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteHard(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully (hard) deleted", id);
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        user.setStatus(Status.DELETED);
        userRepository.save(user);
    }

    @Override
    public User registerUser(String username, String email, String name, String password) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreated(clock.instant());
        user.setUpdated(clock.instant());
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String name, User user) {
        if (name != null) {
            user.setName(name);
        }
        user.setUpdated(clock.instant());
        return user;
    }

    @Override
    public User changePassword(User user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(
            oldPassword,
            user.getPassword()

        )) {
            throw new IncorrectPasswordException("Password incorrect");
        }
        user.setPassword(
            passwordEncoder.encode(newPassword)
        );
        user.setUpdated(clock.instant());
        return userRepository.save(user);
    }
}
