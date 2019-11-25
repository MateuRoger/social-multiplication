package microservices.book.multiplication.controller;

import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class provides a REST API for all the requests about users.
 */
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  public User getUserById(@PathVariable("userId") final Long userId) {
    return this.userService.getUserById(userId);
  }
}
