package com.silenteight.testing.containers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

public final class PostgresContainer {

  private static final PostgreSQLContainer<?> CONTAINER;

  static {
    CONTAINER = new PostgreSQLContainer<>("postgres:10").withDatabaseName(
        "serp_test_" + randomAlphabetic(6))
        .withUsername("user_" + randomAlphabetic(6))
        .withPassword(randomAlphanumeric(12))
        .withTmpFs(Map.of("/var/lib/postgresql/data", "rw"));

    CONTAINER.start();
  }

  public static String getJdbcUrl() {
    return CONTAINER.getJdbcUrl();
  }

  public static String getUsername() {
    return CONTAINER.getUsername();
  }

  public static String getPassword() {
    return CONTAINER.getPassword();
  }

  private PostgresContainer() {
  }

  public static class PostgresTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertyValues propertyValues = TestPropertyValues.of(
          "spring.datasource.url=" + CONTAINER.getJdbcUrl(),
          "spring.datasource.username=" + CONTAINER.getUsername(),
          "spring.datasource.password=" + CONTAINER.getPassword());

      propertyValues.applyTo(context.getEnvironment());
    }
  }
}
