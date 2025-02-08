package dev.lorentzen.branch.exercise.configuration;

/**
 * Configuration class for cache settings. This class encapsulates the
 * parameters needed to configure a cache, including expiration time
 * and maximum allowed size.
 *
 * @param expirationSeconds the duration in seconds after which cache entries should expire
 * @param maximumSize       the maximum number of entries allowed in the cache
 */
public record CacheConfiguration(int expirationSeconds, int maximumSize) {}
