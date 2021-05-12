package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.silenteight.adjudication.engine.analysis.service.integration.AmqpDefaults.*;

@Configuration
@RequiredArgsConstructor
@Profile("rabbitmq-declare")
public class RabbitBrokerConfiguration {

  private static final String DEAD_LETTER_EXCHANGE = "ae.dlx";
  private static final String DEAD_LETTER_QUEUE = "ae.dlq";

  @Bean
  Declarables rabbitBrokerDeclarables() {
    var dlx = ExchangeBuilder.topicExchange(DEAD_LETTER_EXCHANGE).durable(true).build();
    var dlq = QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    var dlqBinding = BindingBuilder.bind(dlq).to(dlx).with("*").noargs();

    var events = ExchangeBuilder.directExchange(EVENTS_EXCHANGE_NAME).durable(true).build();
    var internalEvents = ExchangeBuilder
        .directExchange(INTERNAL_EVENTS_EXCHANGE_NAME)
        .durable(true)
        .build();

    var pendingRecommendations = makeQueue(PENDING_RECOMMENDATIONS_QUEUE_NAME);
    var pendingRecommendationsBinding = bindQueue(pendingRecommendations, internalEvents,
        ADDED_ANALYSIS_DATASETS_ROUTING_KEY);

    var agentExchange = makeQueue(AGENT_EXCHANGE_QUEUE_NAME);
    var agentExchangeBinding = bindQueue(agentExchange, internalEvents,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);

    var category = makeQueue(CATEGORY_QUEUE_NAME);
    var categoryBinding = bindQueue(category, internalEvents,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);

    var commentInput = makeQueue(COMMENT_INPUT_QUEUE_NAME);
    var commentInputBinding = bindQueue(commentInput, internalEvents,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);

    /* TODO(ahaczewski): Configure queue receiving agent responses.
    var matchFeature = makeQueue(MATCH_FEATURE_QUEUE_NAME);
    var matchFeatureBinding = bindQueue(matchFeature, internalEvents,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);
     */

    return new Declarables(
        dlx, dlq, dlqBinding, events, internalEvents, pendingRecommendations,
        pendingRecommendationsBinding, agentExchange, agentExchangeBinding,
        category, categoryBinding, commentInput, commentInputBinding);
  }

  private static Queue makeQueue(String queueName) {
    return QueueBuilder
        .durable(queueName)
        .deadLetterExchange(DEAD_LETTER_EXCHANGE)
        .maxPriority(10)
        .build();
  }

  private static Binding bindQueue(Queue queue, Exchange exchange, String routingKey) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(routingKey)
        .noargs();
  }
}
