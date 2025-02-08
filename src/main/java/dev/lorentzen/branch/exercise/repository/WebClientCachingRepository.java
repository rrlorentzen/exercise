package dev.lorentzen.branch.exercise.repository;

import dev.lorentzen.branch.exercise.error.NotFoundError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Abstract repository class for managing HTTP requests and caching responses.
 * Provides common functionality to retrieve resources via WebClient and cache
 * them for reuse to reduce the need for repetitive API calls.
 */
public abstract class WebClientCachingRepository {

  private static final Logger log = LogManager.getLogger(WebClientCachingRepository.class);

  private final WebClient webClient;
  private final CacheManager cacheManager;
  private final String cacheKey;

  public WebClientCachingRepository(final WebClient webClient, final CacheManager cacheManager) {
    this.webClient = webClient;
    this.cacheManager = cacheManager;
    this.cacheKey = getClass().getName();
  }

  /**
   * Retrieves a resource from the specified URI as an object of the provided class type.
   * First attempts to retrieve the resource from the cache. If not found in the cache, it fetches the resource
   * from the remote URI using the WebClient, stores it in the cache, and returns it.
   *
   * @param path  the path of the resource to be retrieved
   * @param clazz the {@link Class} type of the resource to be retrieved
   * @param <T>   the type parameter of the resource
   * @return a {@link Mono} emitting the retrieved resource of type {@link T}
   */
  protected <T> Mono<T> get(final String path, final Class<T> clazz) {
    return getFromCache(path, clazz)
      .orElseGet(() -> getAndPutInCache(path, clazz));
  }

  /**
   * Retrieves a resource from a specified URI as an object of the provided class type.
   * The resource is retrieved via a remote call using WebClient and cached for future use.
   *
   * @param path  the path of the resource to be retrieved
   * @param clazz the {@link Class} type of the resource to be retrieved
   * @param <T>   the type parameter of the resource
   * @return a {@link Mono} emitting the fetched resource of type {@link T}
   */
  private <T> Mono<T> getAndPutInCache(final String path, final Class<T> clazz) {
    log.debug("Instance of {} not found in cache {} with key {}, putting now.", clazz.getSimpleName(), cacheKey, path);
    return webClient.get()
      .uri(path)
      .retrieve()
      .onStatus(
        statusCode -> statusCode.value() == HttpStatus.NOT_FOUND.value(),
        request -> Mono.error(new NotFoundError(request.request().getURI().toString()))
      )
      .bodyToMono(clazz)
      .doOnSuccess(t -> cacheManager.getCache(cacheKey).put(path, t));
  }

  /**
   * Attempts to retrieve a cached value for the given key and type.
   *
   * @param key   the key used to identify the cached value
   * @param clazz the {@link Class} type of the cached value
   * @param <T>   the type parameter representing the type of the cached value
   * @return an {@link Optional} containing a {@link Mono} with the cached value if found, otherwise an empty {@link Optional}
   */
  private <T> Optional<Mono<T>> getFromCache(final String key, final Class<T> clazz) {
    return Optional.ofNullable(cacheManager.getCache(cacheKey).get(key, clazz)).map(cached -> {
      log.debug("Found instance of {} in cache {} with key {}.", clazz.getSimpleName(), cacheKey, key);
      return Mono.just(cached);
    });
  }

}
