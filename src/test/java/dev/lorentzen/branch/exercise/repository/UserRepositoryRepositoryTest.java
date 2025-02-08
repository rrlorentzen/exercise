package dev.lorentzen.branch.exercise.repository;

import dev.lorentzen.branch.exercise.TestData;
import dev.lorentzen.branch.exercise.UnitTest;
import dev.lorentzen.branch.exercise.model.response.github.UserRepositoriesResponse;
import dev.lorentzen.branch.exercise.model.response.github.UserRepositoryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryRepositoryTest extends UnitTest {

  private static final String CACHE_NAME = UserRepositoryRepository.class.getName();
  private static final String USERNAME = "test-user";
  private static final String PATH = String.format(UserRepositoryRepository.REPOS_FORMAT, USERNAME);

  private final WebClient webClient = mock(WebClient.class);
  private final CacheManager cacheManager = mock(CacheManager.class);

  private final UserRepositoryRepository userRepository = new UserRepositoryRepository(webClient, cacheManager);

  @Mock
  private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

  @Mock
  private WebClient.RequestHeadersSpec requestHeadersSpec;

  @Mock
  private WebClient.ResponseSpec responseSpec;

  @AfterEach
  public void afterEach() {
    reset(webClient, cacheManager, requestHeadersUriSpec, requestHeadersSpec, responseSpec);
  }

  /**
   * Test method for {@link UserRepositoryRepository#get(String)}.
   * Verify that when the cache does not contain a response for the path then a call to the api is made.
   */
  @Test
  public void get_shouldCallApi_whenPathIsNotInCache() {
    final UserRepositoryResponse[] responses = TestData.userRepositoryResponses();
    final UserRepositoriesResponse userResponse = new UserRepositoriesResponse(Arrays.asList(responses));

    final Cache cache = mock(Cache.class);
    when(cacheManager.getCache(CACHE_NAME)).thenReturn(cache);

    when(webClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(PATH)).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(UserRepositoryResponse[].class)).thenReturn(Mono.just(responses));

    final UserRepositoriesResponse result = userRepository.get(USERNAME).block();
    assertNotNull(result);
    assertEquals(userResponse, result);

    verify(cacheManager, times(2)).getCache(CACHE_NAME);
    verify(cache).get(PATH, UserRepositoryResponse[].class);
    verify(webClient).get();
    verify(requestHeadersUriSpec).uri(PATH);
    verify(requestHeadersSpec).retrieve();
    verify(responseSpec).onStatus(any(), any());
    verify(responseSpec).bodyToMono(UserRepositoryResponse[].class);
    verify(cache).put(PATH, responses);
    noMoreInteractions(cache);
  }

  /**
   * Test method for {@link UserRepositoryRepository#get(String)}.
   * Verify that when the cache contains a response it is returned and no calls are made to the api.
   */
  @Test
  public void get_shouldReturnUserResponseFromCache_whenItExistsForPath() {
    final UserRepositoryResponse[] responses = TestData.userRepositoryResponses();
    final UserRepositoriesResponse userResponse = new UserRepositoriesResponse(Arrays.asList(responses));

    final Cache cache = mock(Cache.class);
    when(cacheManager.getCache(CACHE_NAME)).thenReturn(cache);
    when(cache.get(PATH, UserRepositoryResponse[].class)).thenReturn(responses);

    final UserRepositoriesResponse result = userRepository.get(USERNAME).block();
    assertNotNull(result);
    assertEquals(userResponse, result);

    verify(cacheManager).getCache(CACHE_NAME);
    verify(cache).get(PATH, UserRepositoryResponse[].class);
    noMoreInteractions(cache);
  }

  private void noMoreInteractions(final Object... mocks) {
    verifyNoMoreInteractions(mocks);
    verifyNoMoreInteractions(webClient, cacheManager, requestHeadersUriSpec, requestHeadersSpec, responseSpec);
  }

}
