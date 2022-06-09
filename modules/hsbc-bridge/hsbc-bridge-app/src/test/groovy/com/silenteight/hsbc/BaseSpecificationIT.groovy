package com.silenteight.hsbc

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
@ActiveProfiles(["dev", "consuldisabled"])
class BaseSpecificationIT extends Specification {

  @MockBean
  RabbitTemplate rabbitTemplate

  static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:12")

  @Shared
  public PostgreSQLContainer POSTGRESQL_CONTAINER = postgresqlContainer.start()

  @DynamicPropertySource
  private static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
    registry.add("spring.datasource.username", postgresqlContainer::getUsername)
    registry.add("spring.datasource.password", postgresqlContainer::getPassword)
  }
}
