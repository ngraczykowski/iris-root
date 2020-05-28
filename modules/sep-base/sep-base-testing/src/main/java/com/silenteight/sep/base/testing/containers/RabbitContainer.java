package com.silenteight.sep.base.testing.containers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import java.time.Duration;
import java.util.Map;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RabbitContainer {

  private static final GenericContainer<?> CONTAINER;
  private static final String USERNAME;
  private static final String PASSWORD;
  private static final int AMQP_PORT = 5672;

  static {
    USERNAME = "rabbit_" + randomAlphabetic(6);
    PASSWORD = randomAlphanumeric(12);

    CONTAINER = new GenericContainer<>("rabbitmq:3")
        .withEnv("RABBITMQ_DEFAULT_USER", USERNAME)
        .withEnv("RABBITMQ_DEFAULT_PASS", PASSWORD)
        .withEnv("RABBITMQ_VM_MEMORY_HIGH_WATERMARK", "256MiB")
        .withExposedPorts(AMQP_PORT)
        .withTmpFs(Map.of("/var/lib/rabbitmq", "rw"));

    WaitStrategy waitStrategy = new LogMessageWaitStrategy()
        .withRegEx(".*Server startup complete;.*")
        .withStartupTimeout(Duration.of(60, SECONDS));

    CONTAINER.setWaitStrategy(waitStrategy);

    CONTAINER.start();
  }

  public static String getHost() {
    return CONTAINER.getContainerIpAddress();
  }

  public static int getPort() {
    return CONTAINER.getMappedPort(AMQP_PORT);
  }

  public static String getUsername() {
    return USERNAME;
  }

  public static String getPassword() {
    return PASSWORD;
  }

  public static class RabbitTestInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertyValues propertyValues = TestPropertyValues.of(
          "spring.rabbitmq.host=" + getHost(),
          "spring.rabbitmq.port=" + getPort(),
          "spring.rabbitmq.username=" + getUsername(),
          "spring.rabbitmq.password=" + getPassword()
      );

      propertyValues.applyTo(context.getEnvironment());
    }
  }
}
