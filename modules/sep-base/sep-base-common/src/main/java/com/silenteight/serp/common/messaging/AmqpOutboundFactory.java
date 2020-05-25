package com.silenteight.serp.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.amqp.dsl.AmqpOutboundEndpointSpec;

@RequiredArgsConstructor
public class AmqpOutboundFactory {

  private final RabbitTemplate rabbitTemplate;

  public AmqpOutboundEndpointSpec outboundAdapter() {
    return Amqp.outboundAdapter(rabbitTemplate);
  }
}
