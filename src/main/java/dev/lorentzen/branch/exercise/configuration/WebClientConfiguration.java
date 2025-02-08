package dev.lorentzen.branch.exercise.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Optional;

/**
 * Configuration class for creating instances of {@link WebClient} and {@link CacheManager}.
 */
@Configuration
@EnableCaching
public class WebClientConfiguration {

  private static final String HEADER_VERSION = "X-GitHub-Api-Version";

  private static final Logger log = LogManager.getLogger(WebClientConfiguration.class);

  /**
   * Creates a {@link WebClient} bean configured for integration with the GitHub API.
   * The client is built with a base URL, default headers including the GitHub API version,
   * and optional Bearer authentication if a token is provided.
   *
   * @param githubApiConfiguration the configuration object containing the GitHub API settings
   * @return the configured {@link WebClient} instance for communication with the GitHub API
   */
  @Bean("githubWebClient")
  public WebClient githubRestClient(final GitHubApiConfiguration githubApiConfiguration) {
    return WebClient.builder()
      .baseUrl(githubApiConfiguration.url())
      .defaultHeaders(httpHeaders -> {
        httpHeaders.add(HttpHeaders.ACCEPT, "application/vnd.github+json");
        httpHeaders.add(HEADER_VERSION, githubApiConfiguration.version());
        Optional.ofNullable(githubApiConfiguration.token())
          .filter(StringUtils::isNotBlank)
          .map(String::trim)
          .ifPresent(httpHeaders::setBearerAuth);
      })
      .build();
  }

  /**
   * Creates and configures a {@link CacheManager} bean for caching GitHub API data.
   * The cache manager uses Caffeine as the underlying cache implementation and is
   * configured with expiration time and maximum size specified in the GitHub API
   * configuration.
   *
   * @param configuration the github api configuration object containing the cache settings
   * @return the configured {@link CacheManager} instance
   */
  @Bean("githubCacheManager")
  public CacheManager cacheManager(final GitHubApiConfiguration configuration) {
    final CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(
      Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofSeconds(configuration.cache().expirationSeconds()))
        .maximumSize(configuration.cache().maximumSize())
        .evictionListener((key, value, cause) ->
          log.debug("Value {} with key {} evicted from the cache because of {}.", value, key, cause)
        )
        .removalListener((key, value, cause) ->
          log.debug("Value {} with key {} removed from the cache because of {}.", value, key, cause)
        )
        .scheduler(Scheduler.systemScheduler())
    );
    return cacheManager;
  }

}
