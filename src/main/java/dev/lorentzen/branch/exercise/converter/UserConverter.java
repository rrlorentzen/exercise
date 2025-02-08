package dev.lorentzen.branch.exercise.converter;

import dev.lorentzen.branch.exercise.model.Repository;
import dev.lorentzen.branch.exercise.model.User;
import dev.lorentzen.branch.exercise.model.response.github.UserRepositoriesResponse;
import dev.lorentzen.branch.exercise.model.response.github.UserResponse;

/**
 * Utility class responsible for converting User objects to different representations or formats.
 * This class provides static methods to handle the conversion operations.
 * It cannot be instantiated.
 */
public final class UserConverter {

  private UserConverter() {}

  /**
   * Converts the response data from the GitHub API into a User instance.
   *
   * @param username     the username of the user
   * @param user         the GitHub API response containing user details
   * @param repositories the GitHub API response containing a list of the user's repositories
   * @return a User instance representing the consolidated user information
   */
  public static User toUser(final String username,
                            final UserResponse user,
                            final UserRepositoriesResponse repositories) {
    return new User(
      username,
      user.name(),
      user.avatarUrl(),
      user.location(),
      user.email(),
      user.url(),
      user.createdAt(),
      repositories.repositories().stream()
        .map(repo -> new Repository(repo.name(), repo.url()))
        .toList()
    );
  }

}
