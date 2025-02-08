package dev.lorentzen.branch.exercise.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

/**
 * Get User API response class.
 *
 * @param username    the username of the user
 * @param displayName the full name of the user
 * @param avatar      the url of the users avatar
 * @param geoLocation user location
 * @param email       user email address
 * @param url         the url that points to this user in GitHub
 * @param createdAt   date when user was created
 * @param repos       a list of repositories for the user
 */
public record User(
  @JsonProperty("user_name")
  String username,
  @JsonProperty("display_name")
  String displayName,
  String avatar,
  @JsonProperty("geo_location")
  String geoLocation,
  String email,
  String url,
  @JsonProperty("created_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC", shape = JsonFormat.Shape.STRING)
  Instant createdAt,
  List<Repository> repos
) {}
