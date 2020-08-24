package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.google.common.base.Preconditions;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.integration.amqp.dsl.AbstractMessageListenerContainerSpec;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterDMLCSpec;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.util.ErrorHandler;

import static com.silenteight.sep.base.common.messaging.MessagingErrorConfiguration.ERROR_CHANNEL_NAME;

@RequiredArgsConstructor
// NOTE(ahaczewski): Experimental API to wrap the creation of AMQP endpoints in Spring Integration
//  with our message converting and error handling logic.
public class AmqpInboundFactory {

  private final ContentTypeDelegatingMessageConverter messageConverter;

  @Setter
  private SimpleRabbitListenerContainerFactory simpleMessageListenerContainerFactory;
  @Setter
  private DirectRabbitListenerContainerFactory directMessageListenerContainerFactory;
  @Setter
  private ErrorHandler errorHandler;
  @Setter
  private boolean errorQueueEnabled;

  public AmqpInboundChannelAdapterSMLCSpec simpleAdapter() {
    Preconditions.checkState(simpleMessageListenerContainerFactory != null);

    var adapter = Amqp
        .inboundAdapter(simpleMessageListenerContainerFactory.createListenerContainer())
        .messageConverter(messageConverter)
        .configureContainer(this::configureContainer);

    if (errorQueueEnabled)
      adapter.errorChannel(ERROR_CHANNEL_NAME);

    return adapter;
  }

  public AmqpInboundChannelAdapterDMLCSpec directAdapter() {
    Preconditions.checkState(directMessageListenerContainerFactory != null);

    var adapter = Amqp
        .inboundAdapter(directMessageListenerContainerFactory.createListenerContainer())
        .messageConverter(messageConverter)
        .configureContainer(this::configureContainer);

    if (errorQueueEnabled)
      adapter.errorChannel(ERROR_CHANNEL_NAME);

    return adapter;
  }

  private void configureContainer(
      AbstractMessageListenerContainerSpec<?, ? extends AbstractMessageListenerContainer> c) {
  }
}
