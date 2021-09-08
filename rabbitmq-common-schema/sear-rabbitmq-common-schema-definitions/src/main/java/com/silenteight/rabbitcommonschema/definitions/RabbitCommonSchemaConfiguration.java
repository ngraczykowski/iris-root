package com.silenteight.rabbitcommonschema.definitions;

import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.ExchangeBuilder.topicExchange;

@Configuration
public class RabbitCommonSchemaConfiguration {

  /**
   * The array of common RabbitMQ schema elements.
   * <p/>
   * DO NOT REMOVE ELEMENTS FROM THIS LIST.
   */
  private static final Declarable[] COMMON_SCHEMA = {
      directExchange("ae.command").durable(true).build(),
      topicExchange("ae.event").durable(true).build(),
      topicExchange("agent.request").durable(true).build(),
      topicExchange("agent.response").durable(true).build(),
      topicExchange("gov.event").durable(true).build(),
      directExchange("sim.command").durable(true).build(),
      topicExchange("wh.event").durable(true).build(),
      };

  @Bean
  Declarables commonRabbitSchemaDeclarables() {
    return new Declarables(COMMON_SCHEMA);
  }
}
