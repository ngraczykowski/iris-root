package com.silenteight.sep.base.testing.containers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PostgresContainer {

  private static final PostgreSQLContainer<?> CONTAINER;

  static {
    CONTAINER = new PostgreSQLContainer<>("postgres:"
        + System.getProperty("testcontainers.postgres.version", "12"))
        .withDatabaseName("test_db_" + randomAlphabetic(6))
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

  public static String getDriverClassName() {
    return CONTAINER.getDriverClassName();
  }

  public static class PostgresTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertyValues propertyValues = TestPropertyValues.of(
          "spring.datasource.url=" + getJdbcUrl(),
          "spring.datasource.username=" + getUsername(),
          "spring.datasource.password=" + getPassword(),
          "spring.datasource.driver-class-name=" + getDriverClassName(),
          "spring.test.database.replace=none"
      );
      propertyValues.applyTo(context.getEnvironment());
    }
  }
}
