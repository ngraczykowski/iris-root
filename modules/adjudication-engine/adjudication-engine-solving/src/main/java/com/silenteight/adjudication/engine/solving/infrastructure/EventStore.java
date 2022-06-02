package com.silenteight.adjudication.engine.solving.infrastructure;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.DomainEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

@Slf4j
class EventStore {

  public static final String EVENT_STORE_DOMAIN_EVENT_INTERNAL_QUEUE = "event.store.domain.event";
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new ObjectMapper()
        .registerModule(new JavaTimeModule());
  }

  private final RabbitTemplate rabbitTemplate;
  private final EventStoreConfigurationProperties configurationProperties;
  private final IQueue<DomainEvent> queue;

  EventStore(
      final RabbitTemplate rabbitTemplate,
      final EventStoreConfigurationProperties eventStoreConfigurationProperties,
      final HazelcastInstance hazelcastInstance,
      final ScheduledExecutorService eventStoreConsumerExecutorService
  ) {
    this.rabbitTemplate = rabbitTemplate;
    this.configurationProperties = eventStoreConfigurationProperties;
    this.queue = hazelcastInstance.getQueue(EVENT_STORE_DOMAIN_EVENT_INTERNAL_QUEUE);

    eventStoreConsumerExecutorService.scheduleAtFixedRate(
        this::consume, 1000, 500, TimeUnit.MILLISECONDS);
  }

  public void publish(final List<DomainEvent> pendingEvents) {
    log.info("Publish to internal queue. Number of pending events: {}", pendingEvents.size());
    queue.addAll(pendingEvents);
  }

  private void consume() {
    for (int i = 0; i < 100; i++) {
      final DomainEvent domainEvent = this.queue.poll();
      if (domainEvent != null) {
        this.publishSingleEvent().accept(domainEvent);
      }
    }
  }

  @Nonnull
  private Consumer<DomainEvent> publishSingleEvent() {
    var journal = this.configurationProperties.getJournal();
    return domainEvent -> {
      log.debug("Publish event: {}", domainEvent);
      this.rabbitTemplate.convertAndSend(
          journal.getExchange(),
          journal.getRoutingKey(),
          this.serializeEvent(domainEvent)
      );
    };
  }

  @Nonnull
  private byte[] serializeEvent(DomainEvent domainEvent) {
    try {
      return OBJECT_MAPPER.writeValueAsBytes(domainEvent);
    } catch (JsonProcessingException exception) {
      log.warn("Error occurred during serializing: {}", domainEvent, exception);
      return new byte[0];
    }
  }
}
