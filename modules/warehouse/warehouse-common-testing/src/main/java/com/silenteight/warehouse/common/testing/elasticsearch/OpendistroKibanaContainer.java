package com.silenteight.warehouse.common.testing.elasticsearch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.ELASTIC_NETWORK_HOSTNAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.ELK_PASSWORD;
import static com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.ELK_USERNAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.NETWORK;
import static org.springframework.boot.test.util.TestPropertyValues.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OpendistroKibanaContainer {

  @SuppressWarnings("rawtypes")
  private static final GenericContainer KIBANA;

  static {
    DockerImageName kibanaImageName =
        DockerImageName.parse("amazon/opendistro-for-elasticsearch-kibana")
            .withTag("1.13.1");
    KIBANA = new GenericContainer<>(kibanaImageName)
        .withEnv("ELASTICSEARCH_URL", "http://" + ELASTIC_NETWORK_HOSTNAME + ":9200")
        .withEnv("ELASTICSEARCH_HOSTS", "http://" + ELASTIC_NETWORK_HOSTNAME + ":9200")
        .withExposedPorts(5601)
        .withNetwork(NETWORK);

    KIBANA.start();
  }

  public static class OpendistroKibanaContainerInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertyValues propertyValues = of(
          "warehouse.kibana.url=http://" + getTransportAddress(),
          "warehouse.kibana.username=" + ELK_USERNAME,
          "warehouse.kibana.password=" + ELK_PASSWORD
      );

      propertyValues.applyTo(context.getEnvironment());
    }

    private static String getTransportAddress() {
      return KIBANA.getHost() + ":" + KIBANA.getMappedPort(5601);
    }
  }
}
