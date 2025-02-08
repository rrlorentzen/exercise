package dev.lorentzen.branch.exercise;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;

/**
 * Base class for unit tests.
 */
@Tag("unit")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class UnitTest {}
