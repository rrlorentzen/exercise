package dev.lorentzen.branch.exercise.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI documentation setup.
 * This class defines the settings for generating OpenAPI specifications and customizes the API documentation.
 * It uses {@link BuildProperties} to dynamically include build-related metadata such as version information.
 * <br/>
 * This configuration:
 * - Sets up a default server URL for the API
 * - Creates grouped configurations for the public `Exercise API` and `Actuator API`
 * - Adds custom metadata (title, version, description, contact information) to the OpenAPI documentation
 */
@Configuration
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
public class OpenApiConfiguration {

  private final BuildProperties buildProperties;

  public OpenApiConfiguration(final BuildProperties buildProperties) {
    this.buildProperties = buildProperties;
  }

  /**
   * Creates a {@link GroupedOpenApi} bean for the public API group.
   *
   * @return a {@link GroupedOpenApi} instance for the "exercise-api" group
   */
  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
      .group("exercise-api")
      .displayName("Exercise API")
      .pathsToMatch("/api/**")
      .addOpenApiCustomizer(api -> api.info(info()))
      .build();
  }

  /**
   * Creates a {@link GroupedOpenApi} bean for the actuator API group.
   *
   * @return a {@link GroupedOpenApi} instance for the "actuator-api" group
   */
  @Bean
  public GroupedOpenApi actuatorApi() {
    return GroupedOpenApi.builder()
      .group("actuator")
      .displayName("Actuator")
      .pathsToMatch("/actuator/**")
      .addOpenApiCustomizer(api -> api.info(info()))
      .build();
  }

  private Info info() {
    return new Info()
      .title("Exercise API")
      .version(buildProperties.getVersion())
      .description("API for getting GitHub user information.")
      .contact(contact());
  }

  private Contact contact() {
    return new Contact()
      .name("Ryan Lorentzen")
      .email("ryan@lorentzen.dev")
      .url("https://github.com/rrlorentzen");
  }

}
