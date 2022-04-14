package com.silenteight.qco

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class ContainersSpecificationIT extends Specification {

  static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:12")
  static RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:3.8.16-management")

  @Shared
  @SuppressWarnings('unused')
  public PostgreSQLContainer POSTGRESQL_CONTAINER = postgresqlContainer

  @Shared
  @SuppressWarnings('unused')
  public RabbitMQContainer RABBITMQ_CONTAINER = rabbitMqContainer
      .withExchange("bridge.command", "direct")

  @DynamicPropertySource
  private static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
    registry.add("spring.datasource.username", postgresqlContainer::getUsername)
    registry.add("spring.datasource.password", postgresqlContainer::getPassword)
    registry.add("spring.datasource.driverClassName", postgresqlContainer::getDriverClassName)

    registry.add("spring.rabbitmq.host", rabbitMqContainer::getHost)
    registry.add("spring.rabbitmq.port", rabbitMqContainer::getAmqpPort)
    registry.add("spring.rabbitmq.username", rabbitMqContainer::getAdminUsername)
    registry.add("spring.rabbitmq.password", rabbitMqContainer::getAdminPassword)
  }
}
