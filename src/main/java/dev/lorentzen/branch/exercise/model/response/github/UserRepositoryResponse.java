package dev.lorentzen.branch.exercise.model.response.github;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a response containing details for a single user repository.
 * This record is typically used to encapsulate the repository's name and
 * its URL as retrieved from an external source such as a GitHub API.
 * <br/>
 * The repository name provides an identifier for the project, while the
 * URL specifies the link to the repository's web interface.
 */
public record UserRepositoryResponse(
  String name,
  @JsonProperty("html_url")
  String url
) {}
