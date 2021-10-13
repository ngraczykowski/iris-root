package com.silenteight.payments.bridge.common.integration;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.DomainEvent;

import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
public class TypedPublishSubscribeChannel extends PublishSubscribeChannel {

  public TypedPublishSubscribeChannel(Class<? extends DomainEvent> domainEvent) {
    this(domainEvent, Collections.emptyList());
  }

  public TypedPublishSubscribeChannel(Class<? extends DomainEvent> domainEvent,
      List<ChannelInterceptor> interceptors) {
    this(Set.of(domainEvent), interceptors);
  }

  public TypedPublishSubscribeChannel(Set<Class<? extends DomainEvent>> domainEvents,
      List<ChannelInterceptor> interceptors) {
    addInterceptor(new DomainEventTypeInterceptor(domainEvents));
    addInterceptor(new LoggingChannelInterceptor());
    interceptors.forEach(this::addInterceptor);
  }

  private static final class DomainEventTypeInterceptor implements ChannelInterceptor {

    private final Set<Class<? extends DomainEvent>> domainEvents;

    private DomainEventTypeInterceptor(Set<Class<? extends DomainEvent>> domainEvents) {
      this.domainEvents = domainEvents;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
      if (!domainEvents.contains(message.getPayload().getClass())) {
        log.warn("The received payload [{}] doesn't match the configured types: [{}]. "
            + "The message will be ignored.", message.getPayload().getClass().getName(),
            Arrays.toString(domainEvents.toArray()));
        return null;
      }
      return message;
    }
  }


}
