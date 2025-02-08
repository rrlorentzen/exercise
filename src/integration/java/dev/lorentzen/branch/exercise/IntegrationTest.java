package dev.lorentzen.branch.exercise;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Base class for integration tests.
 */
@Tag("integration")
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  useMainMethod = SpringBootTest.UseMainMethod.ALWAYS
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTest {

  @LocalServerPort
  private int port;

  protected String url(final String path) {
    return baseUrl() + path;
  }

  private String baseUrl() {
    return "http://localhost:" + port;
  }

}
