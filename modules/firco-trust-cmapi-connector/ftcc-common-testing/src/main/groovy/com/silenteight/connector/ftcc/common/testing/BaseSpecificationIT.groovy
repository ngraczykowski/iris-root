package com.silenteight.connector.ftcc.common.testing

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import spock.lang.Shared
import spock.lang.Specification

class BaseSpecificationIT extends Specification {

  @Shared
  static RabbitMQContainer RABBITMQ_CONTAINER = new RabbitMQContainer(
      "rabbitmq:3.9.11-management")

  @Shared
  static PostgreSQLContainer POSTGRESQL_CONTAINER = new PostgreSQLContainer("postgres:12")

  def startPostgresql() {
    POSTGRESQL_CONTAINER.start()
  }

  def startRabbitmq() {
    RABBITMQ_CONTAINER.start()
  }

  @DynamicPropertySource
  private static void overrideProperties(DynamicPropertyRegistry registry) {
    if (RABBITMQ_CONTAINER.isRunning()) {
      registry.add("spring.rabbitmq.host", RABBITMQ_CONTAINER::getHost)
      registry.add("spring.rabbitmq.port", RABBITMQ_CONTAINER::getAmqpPort)
      registry.add("spring.rabbitmq.username", RABBITMQ_CONTAINER::getAdminUsername)
      registry.add("spring.rabbitmq.password", RABBITMQ_CONTAINER::getAdminPassword)
    }

    if (POSTGRESQL_CONTAINER.isRunning()) {
      registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl)
      registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername)
      registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword)
    }
  }
}
