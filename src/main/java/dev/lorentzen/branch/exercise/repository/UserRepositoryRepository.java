package dev.lorentzen.branch.exercise.repository;

import dev.lorentzen.branch.exercise.model.response.github.UserRepositoriesResponse;
import dev.lorentzen.branch.exercise.model.response.github.UserRepositoryResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * Repository for making HTTP requests to the user repository endpoints of the GitHub api.
 */
@Component
public class UserRepositoryRepository extends WebClientCachingRepository {

  protected static final String REPOS_FORMAT = "/users/%s/repos";

  private static final Logger log = LogManager.getLogger(UserRepositoryRepository.class);

  public UserRepositoryRepository(@Qualifier("githubWebClient") final WebClient webClient,
                                  @Qualifier("githubCacheManager") final CacheManager cacheManager) {
    super(webClient, cacheManager);
  }

  /**
   * Retrieves a list of repositories for the specified GitHub username.
   *
   * @param username the username of the GitHub user whose repositories are to be retrieved
   * @return a {@link Mono} emitting a {@link UserRepositoriesResponse} object containing the user's repositories
   */
  public Mono<UserRepositoriesResponse> get(final String username) {
    log.debug("Getting user repository details for {}.", username);
    return get(REPOS_FORMAT.formatted(username), UserRepositoryResponse[].class)
      .map(array -> Optional.ofNullable(array).map(Arrays::asList).orElseGet(Collections::emptyList))
      .map(UserRepositoriesResponse::new);
  }

}
