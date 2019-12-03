package com.silenteight.sens.webapp.common.testing.containers;

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
    CONTAINER = new PostgreSQLContainer<>("postgres:10")
        .withDatabaseName("webapp_test_" + randomAlphabetic(6))
        .withUsername("user_" + randomAlphabetic(6))
        .withPassword(randomAlphanumeric(12))
        .withTmpFs(Map.of("/var/lib/postgresql/data", "rw"));

    CONTAINER.start();
  }

  public static class PostgresTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertyValues propertyValues = TestPropertyValues.of(
          "spring.datasource.url=" + CONTAINER.getJdbcUrl(),
          "spring.datasource.username=" + CONTAINER.getUsername(),
          "spring.datasource.password=" + CONTAINER.getPassword(),
          "spring.datasource.driver-class-name=" + CONTAINER.getDriverClassName());

      propertyValues.applyTo(context.getEnvironment());
    }
  }
}
