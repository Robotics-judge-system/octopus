package ru.anarcom.octopus.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import ru.anarcom.octopus.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@Slf4j
public class UserCommands {

  private final UserService userService;

  @Autowired
  public UserCommands(UserService userService) {
    this.userService = userService;
  }

  //name: String, email: String, password:String
  @ShellMethod("Create std user")
  public String createUser(
      @ShellOption("name") String name,
      @ShellOption("password") String password,
      @ShellOption("email") String email
  ) {
    log.info(
        "creating user (std) with name = {} and mail = {} from console",
        name,
        email
    );
    return "OK";
  }

}
