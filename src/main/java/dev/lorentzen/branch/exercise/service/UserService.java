package dev.lorentzen.branch.exercise.service;

import dev.lorentzen.branch.exercise.converter.UserConverter;
import dev.lorentzen.branch.exercise.model.User;
import dev.lorentzen.branch.exercise.repository.UserRepository;
import dev.lorentzen.branch.exercise.repository.UserRepositoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Service for handling operations related to GitHub users.
 * This class uses {@link UserRepository} and {@link UserRepositoryRepository}
 * to fetch user details and repository information from the GitHub API.
 */
@Component
public class UserService {

  private static final Logger log = LogManager.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final UserRepositoryRepository userRepositoryRepository;

  public UserService(final UserRepository userRepository,
                     final UserRepositoryRepository userRepositoryRepository) {
    this.userRepository = userRepository;
    this.userRepositoryRepository = userRepositoryRepository;
  }

  /**
   * Retrieves user details including the user's repositories.
   *
   * @param username the username of the user to retrieve
   * @return a User instance containing the consolidated data for the specified username
   */
  public Mono<User> getUser(final String username) {
    log.info("Getting user details for {}.", username);
    return userRepository.get(username).flatMap(user ->
      userRepositoryRepository.get(username)
        .map(repos -> UserConverter.toUser(username, user, repos))
    );
  }

}
