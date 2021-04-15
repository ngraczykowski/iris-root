package com.silenteight.warehouse.common.testing.elasticsearch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.InetSocketAddress;

import static java.util.Map.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OpenDistroContainer {

  private static final String CLUSTER_NAME;
  private static final String USERNAME = "admin";
  private static final String PASSWORD = "admin";

  private static final ElasticsearchContainer CONTAINER;

  static {
    CLUSTER_NAME = "test-cluster-" + randomAlphabetic(6);
    DockerImageName dockerImageName =
        DockerImageName.parse("amazon/opendistro-for-elasticsearch")
            .withTag("1.13.1")
            .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch");
    CONTAINER = new ElasticsearchContainer(dockerImageName)
        .withTmpFs(of("/usr/share/elasticsearch/data", "rw"))
        .withEnv("cluster.name", CLUSTER_NAME)
        .withEnv("opendistro_security.ssl.http.enabled", "false");

    CONTAINER.start();
  }

  private static String getClusterName() {
    return CLUSTER_NAME;
  }

  private static String getTransportAddress() {
    InetSocketAddress host = CONTAINER.getTcpHost();
    return host.getHostName() + ":" + host.getPort();
  }

  private static String getRestApiAddress() {
    return CONTAINER.getHttpHostAddress();
  }

  public static class OpenDistroContainerInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertyValues propertyValues = TestPropertyValues.of(
          "spring.data.elasticsearch.cluster-name=" + getClusterName(),
          "spring.data.elasticsearch.cluster-nodes=" + getTransportAddress(),
          "spring.elasticsearch.rest.uris=" + getRestApiAddress(),
          "spring.elasticsearch.rest.username=" + USERNAME,
          "spring.elasticsearch.rest.password=" + PASSWORD
      );

      propertyValues.applyTo(context.getEnvironment());
    }
  }
}
