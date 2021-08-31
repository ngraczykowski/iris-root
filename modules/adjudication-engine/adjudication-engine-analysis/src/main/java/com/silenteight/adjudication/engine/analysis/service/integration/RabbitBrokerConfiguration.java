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
  private static final String DEAD_LETTER_QUEUE = "ae.dlq";
  private static final String DEAD_LETTER_EXCHANGE = "ae.dlx";

  @Bean
  Declarables rabbitBrokerDefinitions() {
    var errorQueue = queue(ERROR_QUEUE).build();
    var deadLetterQueue = queue(DEAD_LETTER_QUEUE).lazy().build();

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
    var deadLetterExchange = topicExchange(DEAD_LETTER_EXCHANGE)
        .durable(true)
        .internal()
        .build();
    var deadLetterBinding = bind(deadLetterQueue, deadLetterExchange, "#");

    var pendingRecommendation = queueDeadLetter(PENDING_RECOMMENDATION_QUEUE_NAME).build();
    var pendingRecommendationsBinding = bind(pendingRecommendation, eventInternalExchange,
        ANALYSIS_ALERTS_ADDED_ROUTING_KEY);

    var agentExchange = queueDeadLetter(AGENT_EXCHANGE_QUEUE_NAME).build();
    var agentExchangeBinding = bind(agentExchange, eventInternalExchange,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);

    var category = queueDeadLetter(CATEGORY_QUEUE_NAME).build();
    var categoryBinding = bind(category, eventInternalExchange,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);

    var commentInput = queueDeadLetter(COMMENT_INPUT_QUEUE_NAME).build();
    var commentInputBinding = bind(commentInput, eventInternalExchange,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);

    /* TODO(ahaczewski): Configure queue receiving agent responses.
    var matchFeature = makeQueue(MATCH_FEATURE_QUEUE_NAME);
    var matchFeatureBinding = bindQueue(matchFeature, internalEvents,
        PENDING_RECOMMENDATIONS_ROUTING_KEY);
     */

    var agentResponse = queueDeadLetter(AGENT_RESPONSE_QUEUE_NAME).maxPriority(10).build();
    var agentResponseBinding = bind(agentResponse, agentResponseExchange, "#");

    var tmpAgentRequest = queue(TMP_AGENT_REQUEST_QUEUE_NAME).maxPriority(10).build();
    var tmpAgentRequestBinding = bind(tmpAgentRequest, agentRequestExchange, "#");

    return new Declarables(
        errorQueue,
        deadLetterExchange,
        deadLetterQueue, deadLetterBinding,
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

  private static QueueBuilder queueDeadLetter(String queueName) {
    return queue(queueName).deadLetterExchange(DEAD_LETTER_EXCHANGE);
  }

  private static Binding bind(Queue queue, Exchange exchange, String routingKey) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(routingKey)
        .noargs();
  }
}
