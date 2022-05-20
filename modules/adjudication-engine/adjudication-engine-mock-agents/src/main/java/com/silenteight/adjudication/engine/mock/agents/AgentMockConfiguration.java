package com.silenteight.adjudication.engine.mock.agents;

import lombok.extern.slf4j.Slf4j;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mock-agents")
@Slf4j
class AgentMockConfiguration {

  @Bean
  CachingConnectionFactory connectionFactory() {
    log.info("Mock Agents RabbitMQ connection factory injected");
    return new CachingConnectionFactory(new MockConnectionFactory());
  }

}
