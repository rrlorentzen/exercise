package dev.lorentzen.branch.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Entry point for the Exercise application.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class ExerciseApplication {

  public static void main(final String[] args) {
    SpringApplication.run(ExerciseApplication.class, args);
  }

}
