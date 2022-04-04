package com.silenteight.fab.dataprep

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class BaseSpecificationIT extends Specification {

  static RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:3.9.11-management")

  static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:12")

  @Shared
  public RabbitMQContainer RABBITMQ_CONTAINER = rabbitMqContainer

  @Shared
  public PostgreSQLContainer POSTGRESQL_CONTAINER = postgresqlContainer

  @DynamicPropertySource
  private static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.rabbitmq.host", rabbitMqContainer::getHost)
    registry.add("spring.rabbitmq.port", rabbitMqContainer::getAmqpPort)
    registry.add("spring.rabbitmq.username", rabbitMqContainer::getAdminUsername)
    registry.add("spring.rabbitmq.password", rabbitMqContainer::getAdminPassword)

    registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
    registry.add("spring.datasource.username", postgresqlContainer::getUsername)
    registry.add("spring.datasource.password", postgresqlContainer::getPassword)
  }
}