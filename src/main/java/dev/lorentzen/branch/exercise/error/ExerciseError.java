package dev.lorentzen.branch.exercise.error;

/**
 * Base class for exceptions related to exercise operations. This abstract class extends the {@link RuntimeException},
 * allowing for the creation of custom exceptions specific to exercise-related errors.
 * <br/>
 * Subclasses should provide specific context and details about the particular error encountered.
 */
public abstract class ExerciseError extends RuntimeException {

  public ExerciseError(final String message) {
    super(message);
  }

}
