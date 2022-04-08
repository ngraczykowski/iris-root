package com.silenteight.connector.ftcc.common.testing

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class BaseRabbitMQSpecificationIT extends Specification {

  private static final RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer("rabbitmq:3.9.11-management")

  @Shared
  private RabbitMQContainer lifecycleHelper = RABBIT_MQ_CONTAINER;

  @DynamicPropertySource
  private static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.rabbitmq.host", RABBIT_MQ_CONTAINER::getHost)
    registry.add("spring.rabbitmq.port", RABBIT_MQ_CONTAINER::getAmqpPort)
    registry.add("spring.rabbitmq.username", RABBIT_MQ_CONTAINER::getAdminUsername)
    registry.add("spring.rabbitmq.password", RABBIT_MQ_CONTAINER::getAdminPassword)
  }
}
