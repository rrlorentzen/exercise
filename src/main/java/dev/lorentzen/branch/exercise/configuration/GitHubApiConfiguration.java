package dev.lorentzen.branch.exercise.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Configuration class for GitHub API integration. This class binds the properties specified with the
 * prefix "github.api" from the application's configuration files to the fields of this record.
 * <br/>
 * It facilitates centralized configuration for GitHub API settings
 * such as the API URL, version, cache configuration, and access token.
 *
 * @param url   the url of the GitHub api
 * @param token an optional access token
 */
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "github.api")
public record GitHubApiConfiguration(String url, String version, CacheConfiguration cache, String token) {}
