package ru.anarcom.octopus.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.anarcom.octopus.entity.Status;
import ru.anarcom.octopus.entity.User;
import ru.anarcom.octopus.exceptions.InvalidLoginOrPasswordException;
import ru.anarcom.octopus.repo.UserRepository;
import ru.anarcom.octopus.security.jwt.JwtUser;
import ru.anarcom.octopus.security.jwt.JwtUserFactory;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     *
     * @param username Username of user to search.
     * @return User if was found
     * @throws UsernameNotFoundException If user was not found (or it is deleted).
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsernameAndStatus(username, Status.ACTIVE);

        if (user == null) {
            throw new InvalidLoginOrPasswordException();
        }

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByUsername - user with username: {} successfully loaded", username);
        return jwtUser;
    }
}
