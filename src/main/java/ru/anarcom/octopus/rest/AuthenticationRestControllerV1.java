package ru.anarcom.octopus.rest;


import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anarcom.octopus.dto.AuthenticationRequestDto;
import ru.anarcom.octopus.dto.RefreshTokenDto;
import ru.anarcom.octopus.model.User;
import ru.anarcom.octopus.security.jwt.JwtTokenProvider;
import ru.anarcom.octopus.service.AuthService;
import ru.anarcom.octopus.service.UserService;

/**
 * REST controller for authentication requests (login)
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/api/v1/auth/")
@RequiredArgsConstructor
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final AuthService authService;

    @GetMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            var pair = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            // TODO: Завести DTO для ответа
            response.put("username", username);
            response.put("token", pair.component1());
            response.put("expires_after_sec", pair.component2());
            response.put("refresh_token", authService.getNewRefreshTokenForUser(user));
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
    @GetMapping("refresh")
    public ResponseEntity<?> getNewTokenFromRefresh(
        @RequestBody RefreshTokenDto refreshTokenDto
    ){
        try {
            var token = refreshTokenDto.getRefresh();
            User user = authService.getUserByRefreshToken(token);
            var pair = jwtTokenProvider.createToken(
                user.getUsername(),
                user.getRoles()
            );

            Map<Object, Object> response = new HashMap<>();
            // TODO: Завести DTO для ответа
            response.put("username", user.getUsername());
            response.put("token", pair.component1());
            response.put("expires_after_sec", pair.component2());
            response.put("refresh_token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
