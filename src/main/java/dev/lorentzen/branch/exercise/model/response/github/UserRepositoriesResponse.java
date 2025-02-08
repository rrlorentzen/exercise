package dev.lorentzen.branch.exercise.model.response.github;

import java.util.List;

/**
 * Represents a response containing a list of user repositories.
 * This record encapsulates multiple {@link UserRepositoryResponse} objects,
 * each of which provides details about a specific user repository, such as its name and URL.
 */
public record UserRepositoriesResponse(
  List<UserRepositoryResponse> repositories
) {}
