package dev.lorentzen.branch.exercise.converter;

import dev.lorentzen.branch.exercise.TestData;
import dev.lorentzen.branch.exercise.UnitTest;
import dev.lorentzen.branch.exercise.model.Repository;
import dev.lorentzen.branch.exercise.model.User;
import dev.lorentzen.branch.exercise.model.response.github.UserRepositoriesResponse;
import dev.lorentzen.branch.exercise.model.response.github.UserResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link UserConverter}.
 */
public class UserConverterTest extends UnitTest {

  private static final String USERNAME = "test-user";

  /**
   * Test method for {@link UserConverter#toUser(String, UserResponse, UserRepositoriesResponse)}.
   * Verify that a user and their repositories are converted to an {@link User}.
   */
  @Test
  public void toUser_shouldConvertUserAndRepositoriesCorrectly() {
    final UserResponse userResponse = TestData.userResponse();
    final UserRepositoriesResponse userRepositoriesResponse = TestData.userRepositoriesResponse();

    final User result = UserConverter.toUser(USERNAME, userResponse, userRepositoriesResponse);
    assertNotNull(result);

    assertEquals(USERNAME, result.username());
    assertEquals(userResponse.name(), result.displayName());
    assertEquals(userResponse.avatarUrl(), result.avatar());
    assertEquals(userResponse.location(), result.geoLocation());
    assertEquals(userResponse.email(), result.email());
    assertEquals(userResponse.url(), result.url());
    assertEquals(userResponse.createdAt(), result.createdAt());

    assertEquals(userRepositoriesResponse.repositories().size(), result.repos().size());

    final Repository repo1 = result.repos().getFirst();
    assertEquals(userRepositoriesResponse.repositories().getFirst().name(), repo1.name());
    assertEquals(userRepositoriesResponse.repositories().getFirst().url(), repo1.url());

    final Repository repo2 = result.repos().get(1);
    assertEquals(userRepositoriesResponse.repositories().get(1).name(), repo2.name());
    assertEquals(userRepositoriesResponse.repositories().get(1).url(), repo2.url());
  }

}
