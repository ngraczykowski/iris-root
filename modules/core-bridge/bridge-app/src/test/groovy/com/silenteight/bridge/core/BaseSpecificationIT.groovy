package com.silenteight.bridge.core

import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
@Import(ExternalExchangesConfigurationIT.class)
@ActiveProfiles("consuldisabled")
class BaseSpecificationIT extends Specification {

  static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:12")

  static RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:3.8.16-management")

  @Shared
  @SuppressWarnings('unused')
  public PostgreSQLContainer POSTGRES_SQL_CONTAINER = postgresqlContainer

  @Shared
  @SuppressWarnings('unused')
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

    registry.add("silenteight.bridge.grpc.port", () -> "-1")
  }
}
