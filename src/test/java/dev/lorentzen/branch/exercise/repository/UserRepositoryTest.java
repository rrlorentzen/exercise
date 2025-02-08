package dev.lorentzen.branch.exercise.repository;

import dev.lorentzen.branch.exercise.TestData;
import dev.lorentzen.branch.exercise.UnitTest;
import dev.lorentzen.branch.exercise.model.response.github.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
public class UserRepositoryTest extends UnitTest {

  private static final String CACHE_NAME = UserRepository.class.getName();
  private static final String USERNAME = "test-user";
  private static final String PATH = String.format(UserRepository.USERS_FORMAT, USERNAME);

  private final WebClient webClient = mock(WebClient.class);
  private final CacheManager cacheManager = mock(CacheManager.class);

  private final UserRepository userRepository = new UserRepository(webClient, cacheManager);

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
   * Test method for {@link UserRepository#get(String)}.
   * Verify that when the cache does not contain a response for the path then a call to the api is made.
   */
  @Test
  public void get_shouldCallApi_whenPathIsNotInCache() {
    final UserResponse userResponse = TestData.userResponse();

    final Cache cache = mock(Cache.class);
    when(cacheManager.getCache(CACHE_NAME)).thenReturn(cache);

    when(webClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(PATH)).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(UserResponse.class)).thenReturn(Mono.just(userResponse));

    final UserResponse result = userRepository.get(USERNAME).block();
    assertNotNull(result);
    assertEquals(userResponse, result);

    verify(cacheManager, times(2)).getCache(CACHE_NAME);
    verify(cache).get(PATH, UserResponse.class);
    verify(webClient).get();
    verify(requestHeadersUriSpec).uri(PATH);
    verify(requestHeadersSpec).retrieve();
    verify(responseSpec).onStatus(any(), any());
    verify(responseSpec).bodyToMono(UserResponse.class);
    verify(cache).put(PATH, userResponse);
    noMoreInteractions(cache);
  }

  /**
   * Test method for {@link UserRepository#get(String)}.
   * Verify that when the cache contains a response it is returned and no calls are made to the api.
   */
  @Test
  public void get_shouldReturnUserResponseFromCache_whenItExistsForPath() {
    final UserResponse userResponse = TestData.userResponse();
    final Cache cache = mock(Cache.class);
    when(cacheManager.getCache(CACHE_NAME)).thenReturn(cache);
    when(cache.get(PATH, UserResponse.class)).thenReturn(userResponse);

    final UserResponse result = userRepository.get(USERNAME).block();
    assertNotNull(result);
    assertEquals(userResponse, result);

    verify(cacheManager).getCache(CACHE_NAME);
    verify(cache).get(PATH, UserResponse.class);
    noMoreInteractions(cache);
  }

  private void noMoreInteractions(final Object... mocks) {
    verifyNoMoreInteractions(mocks);
    verifyNoMoreInteractions(webClient, cacheManager, requestHeadersUriSpec, requestHeadersSpec, responseSpec);
  }

}
