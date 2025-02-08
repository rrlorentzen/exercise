package dev.lorentzen.branch.exercise.error;

/**
 * Exception thrown when a 404 status code is received for the provided url.
 */
public class NotFoundError extends ExerciseError {

  public NotFoundError(final String url) {
    super("404 Not Found: %s".formatted(url));
  }

}
