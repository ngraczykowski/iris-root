package com.silenteight.fab.dataprep

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class BaseSpecificationIT extends Specification {

  static RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:3.9.11-management")

  @Shared
  public RabbitMQContainer RABBITMQ_CONTAINER = rabbitMqContainer

  @DynamicPropertySource
  private static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.rabbitmq.host", rabbitMqContainer::getHost)
    registry.add("spring.rabbitmq.port", rabbitMqContainer::getAmqpPort)
    registry.add("spring.rabbitmq.username", rabbitMqContainer::getAdminUsername)
    registry.add("spring.rabbitmq.password", rabbitMqContainer::getAdminPassword)
  }
}
