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
import static org.testcontainers.containers.BindMode.READ_ONLY;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OpendistroElasticContainer {

  static final String ELK_USERNAME = "technicaluser";
  static final String ELK_PASSWORD = "technicaluser";
  static final String ELASTIC_NETWORK_HOSTNAME = "elastic";

  static final ElasticsearchContainer ELASTIC;
  static final Network NETWORK = Network.newNetwork();

  private static final String CLUSTER_NAME;
  private static final String HOST_SECURITY_CONFIG_PATH = "../scripts/es/";
  private static final String CONTAINER_SECURITY_CONFIG_PATH =
      "/usr/share/elasticsearch/plugins/opendistro_security/securityconfig/";

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
        .withNetwork(NETWORK)
        .withFileSystemBind(HOST_SECURITY_CONFIG_PATH + "internal_users.yml",
            CONTAINER_SECURITY_CONFIG_PATH + "internal_users.yml", READ_ONLY)
        .withFileSystemBind(HOST_SECURITY_CONFIG_PATH + "roles.yml",
            CONTAINER_SECURITY_CONFIG_PATH + "roles.yml", READ_ONLY)
        .withFileSystemBind(HOST_SECURITY_CONFIG_PATH + "roles_mapping.yml",
            CONTAINER_SECURITY_CONFIG_PATH + "roles_mapping.yml", READ_ONLY);

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
