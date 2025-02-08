package dev.lorentzen.branch.exercise.configuration;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationTestConfiguration {

  @Bean
  public TestRestTemplate testRestTemplate() {
    return new TestRestTemplate();
  }

}
