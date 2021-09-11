package ru.anarcom.octopus.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anarcom.octopus.dto.user.UserDto;
import ru.anarcom.octopus.entity.User;
import ru.anarcom.octopus.service.UserService;

@RestController
@RequestMapping(value = "/api/v1/admin/")
@RequiredArgsConstructor
public class AdminRestControllerV1 {

    private final UserService userService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
