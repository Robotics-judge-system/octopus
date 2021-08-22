package ru.anarcom.octopus.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.anarcom.octopus.entity.User;
import ru.anarcom.octopus.service.UserService;

@ShellComponent
@Slf4j
public class UserCommands {

  private final UserService userService;

  @Autowired
  public UserCommands(UserService userService) {
    this.userService = userService;
  }

  @ShellMethod("Create std user")
  public String createUser(
      @ShellOption("name") String name,
      @ShellOption("email") String email,
      @ShellOption("password") String password

  ) {
    log.info(
        "creating user (std) with name = {} and mail = {} from console",
        name,
        email
    );
    User user = userService.registerUser(name, password, email);
    log.info("Created user (std) = {}", user);
    return user.toString();
  }

  @ShellMethod("Activate user")
  public String activateUser(
      @ShellOption("email") String email
  ) {
      return userService.activateUser(email).toString();
  }
}
