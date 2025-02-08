package dev.lorentzen.branch.exercise.repository;

import dev.lorentzen.branch.exercise.model.response.github.UserResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Repository for making HTTP requests to the user endpoints of the GitHub api.
 */
@Component
public class UserRepository extends WebClientCachingRepository {

  protected static final String USERS_FORMAT = "/users/%s";

  private static final Logger log = LogManager.getLogger(UserRepository.class);

  public UserRepository(@Qualifier("githubWebClient") final WebClient webClient,
                        @Qualifier("githubCacheManager") final CacheManager cacheManager) {
    super(webClient, cacheManager);
  }

  /**
   * Retrieves the GitHub user details for the specified username.
   *
   * @param username the username of the GitHub user whose details are to be retrieved
   * @return a {@link Mono} emitting a {@link UserResponse} object containing the user's details
   */
  public Mono<UserResponse> get(final String username) {
    log.debug("Getting user details for {}.", username);
    return get(USERS_FORMAT.formatted(username), UserResponse.class);
  }

}
