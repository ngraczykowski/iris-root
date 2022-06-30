package com.silenteight.agent.facade.exchange;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;

@Import(RabbitBrokerTestConfiguration.class)
public abstract class BaseRabbitIntegrationTest extends CommonRabbitIntegrationTest {

  @Value("${facade.amqp.inboundExchangeName}")
  private String exchangeName;

  @Value("${facade.amqp.inboundRoutingKey}")
  private String routingKey;

  @Value("${facade.amqp.deadLetterQueueName}")
  private String deadLetterQueueName;

  @Override
  protected String getExchangeName() {
    return exchangeName;
  }

  @Override
  protected String getRoutingKey() {
    return routingKey;
  }

  @Override
  protected String getDeadLetterQueueName() {
    return deadLetterQueueName;
  }
}
