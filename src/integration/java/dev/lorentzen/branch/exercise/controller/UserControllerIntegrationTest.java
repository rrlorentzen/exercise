package dev.lorentzen.branch.exercise.controller;

import dev.lorentzen.branch.exercise.IntegrationTest;
import dev.lorentzen.branch.exercise.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Integration tests for {@link UserController}.
 */
public class UserControllerIntegrationTest extends IntegrationTest {

  private static final String USERNAME = "octocat";

  @Autowired
  private TestRestTemplate restTemplate;

  /**
   * Test method for {@link UserController#getUser(String)}.
   * <br/>
   * getUser should return a 200 ok response when the provided username exists
   */
  @Test
  public void getUser_shouldReturn200_whenUsernameExists() {
    final Instant date = LocalDateTime.parse("2011-01-25 18:44:36", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      .atOffset(ZoneOffset.UTC)
      .toInstant();

    final ResponseEntity<User> response = restTemplate.getForEntity(userUrl(USERNAME), User.class);
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertNotNull(response.getBody());

    final User user = response.getBody();
    assertEquals(USERNAME, user.username());
    assertEquals("The Octocat", user.displayName());
    assertEquals("https://avatars.githubusercontent.com/u/583231?v=4", user.avatar());
    assertEquals("San Francisco", user.geoLocation());
    assertNull(user.email());
    assertEquals("https://api.github.com/users/octocat", user.url());
    assertEquals(date, user.createdAt());
    assertNotNull(user.repos());
    assertFalse(user.repos().isEmpty());
  }

  /**
   * Test method for {@link UserController#getUser(String)}.
   * <br/>
   * getUser should return a 404 not found response when the provided username does not exist
   */
  @Test
  public void getUser_shouldReturn404_whenUsernameDoesNotExist() {
    final ResponseEntity<User> response = restTemplate.getForEntity(
      userUrl("this-username-probably-does-not-exist"), User.class
    );
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
  }

  private String userUrl(final String username) {
    return url(String.format("%s/%s", UserController.PATH, username));
  }

}
