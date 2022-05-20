package com.silenteight.sep.base.testing.containers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ulimit;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;
import java.util.List;
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
  private static final long NPROC_LIMIT = 262144;
  private static final long NOFILE_LIMIT = 65536;
  private static final List<Ulimit> ULIMITS = List.of(
      new Ulimit("nproc", NPROC_LIMIT, NPROC_LIMIT),
      new Ulimit("nofile", NOFILE_LIMIT, NOFILE_LIMIT));

  static {
    USERNAME = "rabbit_" + randomAlphabetic(6);
    PASSWORD = randomAlphanumeric(12);

    var rabbitmqConfig = MountableFile.forClasspathResource(
        "com/silenteight/sep/base/testing/containers/rabbitmq.conf");

    CONTAINER = new GenericContainer<>("rabbitmq:3.8")
        .withCreateContainerCmdModifier(RabbitContainer::modifyContainerCmd)
        .withCopyFileToContainer(rabbitmqConfig, "/etc/rabbitmq/rabbitmq.conf")
        .withEnv("RABBITMQ_DEFAULT_USER", USERNAME)
        .withEnv("RABBITMQ_DEFAULT_PASS", PASSWORD)
        .withEnv(
            "RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS",
            "+sbwt none +sbwtdcpu none +sbwtdio none"
                + " +P " + NPROC_LIMIT + " +S 2:2 +Q " + NOFILE_LIMIT)
        .withExposedPorts(AMQP_PORT)
        .withTmpFs(Map.of("/var/lib/rabbitmq", "rw"));

    WaitStrategy waitStrategy = new LogMessageWaitStrategy()
        .withRegEx(".*Server startup complete;.*")
        .withStartupTimeout(Duration.of(60, SECONDS));

    CONTAINER.setWaitStrategy(waitStrategy);

    CONTAINER.start();
  }

  private static void modifyContainerCmd(CreateContainerCmd cmd) {
    var hostConfig = cmd.getHostConfig();
    if (hostConfig == null)
      hostConfig = HostConfig.newHostConfig();

    cmd.withHostConfig(hostConfig.withUlimits(ULIMITS));
  }

  public static String getHost() {
    return CONTAINER.getHost();
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
