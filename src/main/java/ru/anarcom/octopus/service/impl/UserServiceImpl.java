package ru.anarcom.octopus.service.impl;

import java.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.anarcom.octopus.entity.Status;
import ru.anarcom.octopus.entity.User;
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
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result, id);
        return result;
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
        log.info("IN delete - user with id: {} successfully deleted", id);
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
}
