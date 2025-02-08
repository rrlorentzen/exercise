package dev.lorentzen.branch.exercise;

import dev.lorentzen.branch.exercise.model.response.github.UserRepositoriesResponse;
import dev.lorentzen.branch.exercise.model.response.github.UserRepositoryResponse;
import dev.lorentzen.branch.exercise.model.response.github.UserResponse;

import java.time.Instant;
import java.util.Arrays;

/**
 * Provides static test data for use in tests. This class contains predefined methods to
 * generate static data for UserResponse and UserRepositoriesResponse objects.
 * <br/>
 * It is intended to be used for mocking or verifying test cases.
 */
public final class TestData {

  private TestData() {}

  /**
   * Creates a static instance of the {@link UserResponse} record with preset
   * data including name, avatar URL, location, email, URL, and account creation timestamp.
   *
   * @return A {@link UserResponse} object representing predefined user details.
   */
  public static UserResponse userResponse() {
    return new UserResponse(
      "Some Person",
      "example.com/avatar/some-person",
      "Some Place",
      "some-person@example.com",
      "some@person.dev",
      Instant.now()
    );
  }

  /**
   * Creates a static instance of the {@link UserRepositoriesResponse} record
   * with predefined data containing a list of repositories.
   *
   * @return A {@link UserRepositoriesResponse} object comprising predefined user repository details.
   */
  public static UserRepositoriesResponse userRepositoriesResponse() {
    return new UserRepositoriesResponse(Arrays.asList(userRepositoryResponses()));
  }

  /**
   * Generates a static array of predefined {@link UserRepositoryResponse} objects.
   * Each object in the array represents a user repository with preset values
   * for its name and URL.
   *
   * @return An array of {@link UserRepositoryResponse} objects containing predefined repository details.
   */
  public static UserRepositoryResponse[] userRepositoryResponses() {
    return new UserRepositoryResponse[]{
      new UserRepositoryResponse(
        "some-repository",
        "git.example.com/some-repository"
      ),
      new UserRepositoryResponse(
        "another-repository",
        "git.example.com/another-repository"
      )
    };
  }

}
