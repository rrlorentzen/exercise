package dev.lorentzen.branch.exercise.controller;

import dev.lorentzen.branch.exercise.model.User;
import dev.lorentzen.branch.exercise.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller for handling user-related operations.
 * Provides endpoints to retrieve user details including their repositories.
 */
@RestController
@RequestMapping(UserController.PATH)
public class UserController {

  protected static final String PATH = "/api/v1/user";

  private static final Logger log = LogManager.getLogger(UserController.class);

  private final UserService userService;

  public UserController(final UserService userService) {
    this.userService = userService;
  }

  /**
   * Retrieves user details including repositories for the specified username.
   *
   * @param username the username of the user to retrieve
   * @return a {@link Mono<User>} containing the user details and repositories
   */
  @Operation(
    summary = "Get User Details",
    description = "Retrieves the details of a user along with their repositories by username.",
    tags = {"User"},
    responses = {
      @ApiResponse(responseCode = "200", description = "User details successfully retrieved."),
      @ApiResponse(responseCode = "404", description = "User not found.")
    },
    parameters = {
      @Parameter(
        name = "username",
        description = "The GitHub username of the user whose details are to be retrieved.",
        required = true
      )
    }
  )
  @GetMapping("/{username}")
  public Mono<User> getUser(@PathVariable final String username) {
    log.info("Received request to get user details for {}.", username);
    return userService.getUser(username);
  }

}
