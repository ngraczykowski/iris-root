package com.silenteight.warehouse.common.testing.elasticsearch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.Network;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import static java.util.Map.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OpendistroElasticContainer {

  private static final String CLUSTER_NAME;
  static final String ELK_USERNAME = "admin";
  static final String ELK_PASSWORD = "admin";
  static final String ELASTIC_NETWORK_HOSTNAME = "elastic";

  static final ElasticsearchContainer ELASTIC;
  static final Network NETWORK = Network.newNetwork();

  static {
    CLUSTER_NAME = "test-cluster-" + randomAlphabetic(6);
    DockerImageName dockerImageName =
        DockerImageName.parse("amazon/opendistro-for-elasticsearch")
            .withTag("1.13.1")
            .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch");
    ELASTIC = new ElasticsearchContainer(dockerImageName)
        .withTmpFs(of("/usr/share/elasticsearch/data", "rw"))
        .withEnv("cluster.name", CLUSTER_NAME)
        .withEnv("opendistro_security.ssl.http.enabled", "false")
        .withNetworkAliases(ELASTIC_NETWORK_HOSTNAME)
        .withNetwork(NETWORK);

    ELASTIC.start();
  }

  public static class OpendistroElasticContainerInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      String httpHostAddress = ELASTIC.getHttpHostAddress();

      TestPropertyValues propertyValues = TestPropertyValues.of(
          "spring.data.elasticsearch.cluster-name=" + CLUSTER_NAME,
          "spring.data.elasticsearch.cluster-nodes=" + httpHostAddress,
          "spring.elasticsearch.rest.uris=" + httpHostAddress,
          "spring.elasticsearch.rest.username=" + ELK_USERNAME,
          "spring.elasticsearch.rest.password=" + ELK_PASSWORD
      );

      propertyValues.applyTo(context.getEnvironment());
    }
  }
}
