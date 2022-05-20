package com.silenteight.warehouse.common.testing.storage.minio;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MinioContainer {

  private static final String ADMIN_ACCESS_KEY = "admin";
  private static final String ADMIN_SECRET_KEY = "admin123";
  private static final String MINIO_DOCKER_IMAGE = "minio/minio:RELEASE.2021-03-10T05-11-33Z";
  private static final String REGION = "s3_test_region";
  private static final int MINIO_PORT = 9000;

  private static final GenericContainer<?> MINIO_CONTAINER;

  static {
    MINIO_CONTAINER = new GenericContainer<>(MINIO_DOCKER_IMAGE)
        .withEnv("MINIO_ACCESS_KEY", ADMIN_ACCESS_KEY)
        .withEnv("MINIO_SECRET_KEY", ADMIN_SECRET_KEY)
        .withCommand("server /data")
        .withExposedPorts(MINIO_PORT)
        .waitingFor(new HttpWaitStrategy()
            .forPath("/minio/health/ready")
            .forPort(MINIO_PORT)
            .withStartupTimeout(Duration.ofSeconds(10)));

    MINIO_CONTAINER.start();
  }

  public static class MinioContainerInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertyValues propertyValues = TestPropertyValues.of(
          "sep.filestorage.minio.url=http://" + getTransportAddress(),
          "sep.filestorage.minio.access-key=" + ADMIN_ACCESS_KEY,
          "sep.filestorage.minio.private-key=" + ADMIN_SECRET_KEY,
          "sep.filestorage.minio.region=" + REGION
      );

      propertyValues.applyTo(context.getEnvironment());
    }

    private static String getTransportAddress() {
      return MINIO_CONTAINER.getHost() + ":" + MINIO_CONTAINER.getMappedPort(MINIO_PORT);
    }
  }
}
