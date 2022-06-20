/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco

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
