package com.silenteight.sep.base.testing.containers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.net.InetSocketAddress;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElasticContainer {

  private static final String CLUSTER_NAME;

  private static final ElasticsearchContainer CONTAINER;

  static {
    CLUSTER_NAME = "test-cluster-" + randomAlphabetic(6);
    CONTAINER = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:6.2.4")
        .withTmpFs(Map.of("/usr/share/elasticsearch/data", "rw"))
        .withEnv("cluster.name", CLUSTER_NAME);

    CONTAINER.start();
  }

  public static String getClusterName() {
    return CLUSTER_NAME;
  }

  public static String getTransportAddress() {
    InetSocketAddress host = CONTAINER.getTcpHost();
    return host.getHostName() + ":" + host.getPort();
  }

  public static String getRestApiAddress() {
    return CONTAINER.getHttpHostAddress();
  }

  public static class ElasticContainerInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertyValues propertyValues = TestPropertyValues.of(
          "spring.data.elasticsearch.cluster-name=" + getClusterName(),
          "spring.data.elasticsearch.cluster-nodes=" + getTransportAddress(),
          "spring.elasticsearch.rest.uris=" + getRestApiAddress()
      );

      propertyValues.applyTo(context.getEnvironment());
    }
  }
}
