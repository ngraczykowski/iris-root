package com.silenteight.payments.bridge.common.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.DomainEvent;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Collections;
import java.util.List;

@Slf4j
public class TypedPublishSubscribeChannel extends PublishSubscribeChannel {

  public TypedPublishSubscribeChannel(Class<? extends DomainEvent> domainEvent) {
    this(domainEvent, Collections.emptyList());
  }

  public TypedPublishSubscribeChannel(Class<? extends DomainEvent> domainEvent,
      List<ChannelInterceptor> interceptors) {
    addInterceptor(new DomainEventTypeInterceptor(domainEvent));
    addInterceptor(new LoggingChannelInterceptor());
    interceptors.forEach(this::addInterceptor);
  }

  private static final class DomainEventTypeInterceptor implements ChannelInterceptor {

    private final Class<? extends DomainEvent> domainEvent;

    private DomainEventTypeInterceptor(Class<? extends DomainEvent> domainEvent) {
      this.domainEvent = domainEvent;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
      if (domainEvent.equals(message.getPayload().getClass())) {
        log.warn("The received payload [{}] doesn't match the configured type: [{}]. "
            + "The message will be ignored.", message.getPayload().getClass().getName(),
            domainEvent.getName());
        return null;
      }
      return message;
    }
  }


}
