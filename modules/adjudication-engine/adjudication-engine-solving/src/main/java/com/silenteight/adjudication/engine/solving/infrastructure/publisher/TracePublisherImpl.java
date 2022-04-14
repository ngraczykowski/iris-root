package com.silenteight.adjudication.engine.solving.infrastructure.publisher;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.TracePublisher;
import com.silenteight.adjudication.engine.solving.domain.TraceEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Slf4j
@Service
@Profile("trace-solving")
class TracePublisherImpl implements TracePublisher {

  private static final String TRACKING_EXCHANGE_NAME = "ae.event.internal.tracing";
  private static final String TRACKING_ROUTING_KEY = "";
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new ObjectMapper()
        .registerModule(new JavaTimeModule());
  }

  private final RabbitTemplate rabbitTemplate;

  TracePublisherImpl(final RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void publish(TraceEvent event) {
    log.debug("Publish event {}", event);

    rabbitTemplate.convertAndSend(
        TRACKING_EXCHANGE_NAME, TRACKING_ROUTING_KEY, serializeEvent(event));
  }

  @Nonnull
  private byte[] serializeEvent(TraceEvent domainEvent) {
    try {
      return OBJECT_MAPPER.writeValueAsBytes(domainEvent);
    } catch (JsonProcessingException exception) {
      log.warn("Error occurred during serializing: {}", domainEvent, exception);
      return new byte[0];
    }
  }
}
