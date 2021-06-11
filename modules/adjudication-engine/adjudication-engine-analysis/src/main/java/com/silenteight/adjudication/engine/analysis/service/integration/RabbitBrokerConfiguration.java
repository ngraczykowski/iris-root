package com.silenteight.adjudication.engine.analysis.service.integration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.silenteight.adjudication.engine.analysis.service.integration.AmqpDefaults.*;
import static org.springframework.amqp.core.ExchangeBuilder.topicExchange;

@Configuration
@Profile("rabbitdeclare")
class RabbitBrokerConfiguration {

  private static final String ERROR_QUEUE = "ae.error-queue";

  @Bean
  Declarables rabbitBrokerDeclarables() {
    var errorQueue = queue(ERROR_QUEUE).build();

    var eventExchange = topicExchange(EVENT_EXCHANGE_NAME).durable(true).build();
    var eventInternalExchange = topicExchange(EVENT_INTERNAL_EXCHANGE_NAME)
        .durable(true)
        .build();
    var agentRequestExchange = topicExchange(AGENT_REQUEST_EXCHANGE_NAME)
        .durable(true)
        .build();
    var agentResponseExchange = topicExchange(AGENT_RESPONSE_EXCHANGE_NAME)
        .durable(true)
        .build();

    var pendingRecommendation = queue(PENDING_RECOMMENDATION_QUEUE_NAME).build();
    var pendingRecommendationsBinding = bind(pendingRecommendation, eventInternalExchange,
        ADDED_ANALYSIS_DATASETS_ROUTING_KEY);

    var agentExchange = queue(AGENT_EXCHANGE_QUEUE_NAME).build();
    var agentExchangeBinding = bind(agentExchange, eventInternalExchange,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);

    var category = queue(CATEGORY_QUEUE_NAME).build();
    var categoryBinding = bind(category, eventInternalExchange,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);

    var commentInput = queue(COMMENT_INPUT_QUEUE_NAME).build();
    var commentInputBinding = bind(commentInput, eventInternalExchange,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);

    /* TODO(ahaczewski): Configure queue receiving agent responses.
    var matchFeature = makeQueue(MATCH_FEATURE_QUEUE_NAME);
    var matchFeatureBinding = bindQueue(matchFeature, internalEvents,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);
     */

    var agentResponse = queue(AGENT_RESPONSE_QUEUE_NAME).maxPriority(10).build();
    var agentResponseBinding = bind(agentResponse, agentResponseExchange, "#");

    var tmpAgentRequest = queue(TMP_AGENT_REQUEST_QUEUE_NAME).maxPriority(10).build();
    var tmpAgentRequestBinding = bind(tmpAgentRequest, agentRequestExchange, "#");

    return new Declarables(
        errorQueue,
        eventExchange, eventInternalExchange, agentRequestExchange, agentResponseExchange,
        pendingRecommendation, pendingRecommendationsBinding,
        agentExchange, agentExchangeBinding,
        category, categoryBinding,
        commentInput, commentInputBinding,
        agentResponse, agentResponseBinding,
        tmpAgentRequest, tmpAgentRequestBinding);
  }

  private static QueueBuilder queue(String queueName) {
    return QueueBuilder
        .durable(queueName)
        .withArgument("x-queue-type", "classic");
  }

  private static Binding bind(Queue queue, Exchange exchange, String routingKey) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(routingKey)
        .noargs();
  }
}
