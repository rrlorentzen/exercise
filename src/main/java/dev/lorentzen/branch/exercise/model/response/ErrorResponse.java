package dev.lorentzen.branch.exercise.model.response;

import java.time.Instant;

/**
 * Represents an error response with details about an error event.
 *
 * @param timestamp:  The timestamp when the error occurred.
 * @param message:    A descriptive message detailing the cause or nature of the error.
 * @param statusCode: The HTTP status code associated with the error.
 */
public record ErrorResponse(Instant timestamp, String message, int statusCode) {}
