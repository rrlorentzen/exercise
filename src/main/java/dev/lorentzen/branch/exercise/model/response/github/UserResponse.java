package dev.lorentzen.branch.exercise.model.response.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Represents a response containing user details fetched from an external source
 * (e.g., a GitHub API). The record encapsulates the essential information about
 * a user such as their name, avatar URL, location, email, public profile URL,
 * and the timestamp indicating when their account was created.
 */
public record UserResponse(
  String name,
  @JsonProperty("avatar_url") String avatarUrl,
  String location,
  String email,
  String url,
  @JsonProperty("created_at") Instant createdAt
) {}
