package dev.lorentzen.branch.exercise.model;

/**
 * Represents a repository belonging to a {@link User}.
 *
 * @param name the name of the repository
 * @param url  the URL of the repository
 */
public record Repository(
  String name,
  String url
) {}
