package com.silenteight.bridge.core

import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.MountableFile
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Paths

@Testcontainers
@Import(ExternalExchangesConfigurationIT.class)
@ActiveProfiles("consuldisabled")
class BaseSpecificationIT extends Specification {

  static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:12")

  static RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:3.9.11-management")
      .withCopyFileToContainer(
          MountableFile.forHostPath(
              Paths.get('../docker/rabbitmq/rabbitmq_delayed_message_exchange-3.9.0.ez')),
          '/opt/rabbitmq/plugins/')
      .withPluginsEnabled('rabbitmq_delayed_message_exchange')

  static String getGrpcPortForTest() {return "21234"}

  @Shared
  public PostgreSQLContainer POSTGRES_SQL_CONTAINER = postgresqlContainer

  @Shared
  public RabbitMQContainer RABBITMQ_CONTAINER = rabbitMqContainer

  @DynamicPropertySource
  private static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
    registry.add("spring.datasource.username", postgresqlContainer::getUsername)
    registry.add("spring.datasource.password", postgresqlContainer::getPassword)

    registry.add("spring.rabbitmq.host", rabbitMqContainer::getHost)
    registry.add("spring.rabbitmq.port", rabbitMqContainer::getAmqpPort)
    registry.add("spring.rabbitmq.username", rabbitMqContainer::getAdminUsername)
    registry.add("spring.rabbitmq.password", rabbitMqContainer::getAdminPassword)

    registry.add("silenteight.bridge.grpc.port", BaseSpecificationIT::getGrpcPortForTest)
  }
}
