package com.silenteight.qco

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@SpringBootConfiguration
@TestConfiguration
@ComponentScan("com.silenteight.qco")
class QcoIntegrationTestConfiguration {

  @Bean
  RabbitTemplate rabbitTemplate(
      @Value('${spring.rabbitmq.host}') String host,
      @Value('${spring.rabbitmq.port}') int port,
      @Value('${spring.rabbitmq.username}') String username,
      @Value('${spring.rabbitmq.password}') String password) {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    return new RabbitTemplate(connectionFactory);
  }
}
