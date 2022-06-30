package com.silenteight.agent.common.messaging.amqp;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.integration.amqp.dsl.AbstractMessageListenerContainerSpec;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;

import static com.google.common.base.Preconditions.checkState;

@RequiredArgsConstructor
public class AmqpInboundFactory {

  private final ContentTypeDelegatingMessageConverter messageConverter;

  @Setter
  private SimpleRabbitListenerContainerFactory simpleMessageListenerContainerFactory;

  public AmqpInboundChannelAdapterSMLCSpec simpleAdapter() {
    checkState(simpleMessageListenerContainerFactory != null);

    return Amqp
        .inboundAdapter(simpleMessageListenerContainerFactory.createListenerContainer())
        .messageConverter(messageConverter)
        .configureContainer(this::configureContainer);
  }

  private void configureContainer(
      AbstractMessageListenerContainerSpec<?, ? extends AbstractMessageListenerContainer> c) {
  }
}
