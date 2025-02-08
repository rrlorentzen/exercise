package dev.lorentzen.branch.exercise.handler;

import dev.lorentzen.branch.exercise.error.NotFoundError;
import dev.lorentzen.branch.exercise.model.response.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

/**
 * A global exception handler for catching and processing application-wide exceptions.
 * This class uses Spring's @ControllerAdvice to intercept exceptions thrown by controllers
 * and provide appropriate responses to the client.
 */
@ControllerAdvice
public class ErrorHandler {

  private static final Logger log = LogManager.getLogger(ErrorHandler.class);

  /**
   * Handles exceptions of type {@link NotFoundError} and returns a 404 Not Found response.
   *
   * @param error the NotFoundError exception containing details of the not-found error
   * @return a {@link ResponseEntity} with HTTP 404 status and no response body
   */
  @ExceptionHandler(NotFoundError.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundError error) {
    log.warn(error.getMessage());
    return ResponseEntity.notFound().build();
  }

  /**
   * Handles generic exceptions that are not explicitly handled by other exception handler methods.
   * This method intercepts all {@link Exception} instances and returns an appropriate
   * internal server error response with relevant error details.
   *
   * @param exception the thrown exception to be handled
   * @return a {@link ResponseEntity} containing an {@link ErrorResponse} object with
   * details about the error and an HTTP status code of 500 (Internal Server Error)
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
    log.error("Encountered unhandled exception.", exception);
    return ResponseEntity.internalServerError()
      .body(new ErrorResponse(Instant.now(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }

}
