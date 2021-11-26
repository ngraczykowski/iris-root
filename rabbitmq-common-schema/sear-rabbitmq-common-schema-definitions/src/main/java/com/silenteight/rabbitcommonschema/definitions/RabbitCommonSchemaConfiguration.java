package com.silenteight.rabbitcommonschema.definitions;

import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.*;
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
      directExchange(AE_COMMAND_EXCHANGE).durable(true).build(),
      topicExchange(AE_EVENT_EXCHANGE).durable(true).build(),
      topicExchange(AGENT_REQUEST_EXCHANGE).durable(true).build(),
      topicExchange(AGENT_RESPONSE_EXCHANGE).durable(true).build(),
      topicExchange(AUDIT_EVENT_EXCHANGE).durable(true).build(),
      directExchange(BRIDGE_COMMAND_EXCHANGE).durable(true).build(),
      directExchange(BRIDGE_RETENTION_EXCHANGE).durable(true).build(),
      topicExchange(GOV_EVENT_EXCHANGE).durable(true).internal().build(),
      directExchange(SIM_COMMAND_EXCHANGE).durable(true).build(),
      topicExchange(WH_EVENT_EXCHANGE).durable(true).build(),
      topicExchange(GOV_QA_EXCHANGE).durable(true).build()
  };

  @Bean
  Declarables commonRabbitSchemaDeclarables() {
    return new Declarables(COMMON_SCHEMA);
  }
}
