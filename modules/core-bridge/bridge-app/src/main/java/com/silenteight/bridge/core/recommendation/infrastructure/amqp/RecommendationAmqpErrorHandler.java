package com.silenteight.bridge.core.recommendation.infrastructure.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.OptionalLong;

@Slf4j
@RequiredArgsConstructor
@Component("recommendationAmqpErrorHandler")
class RecommendationAmqpErrorHandler implements RabbitListenerErrorHandler {

  private final AmqpRecommendationProperties amqpRecommendationProperties;

  @Override
  public Object handleError(
      Message amqpMessage, org.springframework.messaging.Message<?> message,
      ListenerExecutionFailedException exception) throws Exception {
    getMessageDeathCount(amqpMessage).ifPresentOrElse(
        value -> {
          var numberOfRetries = amqpRecommendationProperties.numberOfRetriesDeadMessages();
          if (value < numberOfRetries) {
            log.info("Retry {} of {}", value, numberOfRetries);
            throw exception;
          } else {
            var messageProperties = amqpMessage.getMessageProperties();
            log.info("Retries exhausted for message type: {} from queue: {}",
                messageProperties.getType(), messageProperties.getConsumerQueue());
          }
        }, () -> {
          throw exception;
        });
    return null;
  }

  private OptionalLong getMessageDeathCount(Message amqpMessage) {
    return Optional.ofNullable(amqpMessage.getMessageProperties())
        .map(MessageProperties::getXDeathHeader)
        .stream()
        .flatMap(Collection::stream)
        .mapToLong(properties -> (long) properties.get("count"))
        .distinct()
        .sorted()
        .findFirst();
  }
}
