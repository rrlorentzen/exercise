package dev.lorentzen.branch.exercise.service;

import dev.lorentzen.branch.exercise.TestData;
import dev.lorentzen.branch.exercise.converter.UserConverter;
import dev.lorentzen.branch.exercise.error.NotFoundError;
import dev.lorentzen.branch.exercise.model.User;
import dev.lorentzen.branch.exercise.model.response.github.UserRepositoriesResponse;
import dev.lorentzen.branch.exercise.model.response.github.UserResponse;
import dev.lorentzen.branch.exercise.repository.UserRepository;
import dev.lorentzen.branch.exercise.repository.UserRepositoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserService}.
 */
public class UserServiceTest {

  private static final String USERNAME = "test-user";

  private final UserRepository userRepository = mock(UserRepository.class);
  private final UserRepositoryRepository userRepositoryRepository = mock(UserRepositoryRepository.class);

  private final UserService userService = new UserService(userRepository, userRepositoryRepository);

  @AfterEach
  public void afterEach() {
    reset(userRepository, userRepositoryRepository);
  }

  /**
   * Test method for {{@link UserService#getUser(String)}.
   * Verify that a user and their repositories are returned when they exist.
   */
  @Test
  public void getUser_shouldReturnUserWithRepositories_whenUserAndReposExist() {
    final UserResponse userResponse = TestData.userResponse();
    final UserRepositoriesResponse userRepositoriesResponse = TestData.userRepositoriesResponse();

    when(userRepository.get(USERNAME)).thenReturn(Mono.just(userResponse));
    when(userRepositoryRepository.get(USERNAME)).thenReturn(Mono.just(userRepositoriesResponse));

    final User result = userService.getUser(USERNAME).block();
    assertNotNull(result);
    assertEquals(result, UserConverter.toUser(USERNAME, userResponse, userRepositoriesResponse));

    verify(userRepository).get(USERNAME);
    verify(userRepositoryRepository).get(USERNAME);
    noMoreInteractions();
  }

  /**
   * Test method for {{@link UserService#getUser(String)}.
   * Verify that a user is returned when they have no repositories.
   */
  @Test
  public void getUser_shouldHandleEmptyData_whenUserHasNoRepositories() {
    final UserResponse userResponse = TestData.userResponse();
    final UserRepositoriesResponse userRepositoriesResponse = new UserRepositoriesResponse(Collections.emptyList());

    when(userRepository.get(USERNAME)).thenReturn(Mono.just(userResponse));
    when(userRepositoryRepository.get(USERNAME)).thenReturn(Mono.just(userRepositoriesResponse));

    final User result = userService.getUser(USERNAME).block();
    assertNotNull(result);
    assertTrue(result.repos().isEmpty());
    assertEquals(result, UserConverter.toUser(USERNAME, userResponse, userRepositoriesResponse));

    verify(userRepository).get(USERNAME);
    verify(userRepositoryRepository).get(USERNAME);
    noMoreInteractions();
  }

  /**
   * Test method for {{@link UserService#getUser(String)}.
   * Verify:
   * - a {@link NotFoundError} is thrown when the user doesn't exist
   * - that there is no attempt made to get repositories if the user doesn't exist
   */
  @Test
  public void getUser_shouldThrowError_whenUserDoesNotExist() {
    when(userRepository.get(USERNAME)).thenReturn(Mono.error(new NotFoundError("example.com")));

    assertThrows(NotFoundError.class, () -> userService.getUser(USERNAME).block());

    verify(userRepository).get(USERNAME);
    noMoreInteractions();
  }

  /**
   * Test method for {{@link UserService#getUser(String)}.
   * Verify that a {@link NotFoundError} is thrown when the user repositories don't exist
   */
  @Test
  public void getUser_shouldThrowError_whenUserRepositoriesDoNotExist() {
    when(userRepository.get(USERNAME)).thenReturn(Mono.just(TestData.userResponse()));
    when(userRepositoryRepository.get(USERNAME)).thenReturn(Mono.error(new NotFoundError("example.com")));

    assertThrows(NotFoundError.class, () -> userService.getUser(USERNAME).block());

    verify(userRepository).get(USERNAME);
    verify(userRepositoryRepository).get(USERNAME);
    noMoreInteractions();
  }

  private void noMoreInteractions() {
    verifyNoMoreInteractions(userRepository, userRepositoryRepository);
  }

}
