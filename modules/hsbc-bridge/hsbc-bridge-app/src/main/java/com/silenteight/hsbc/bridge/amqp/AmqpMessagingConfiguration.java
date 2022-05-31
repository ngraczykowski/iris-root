package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.protocol.AnyProtoMessageConverter;
import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import static com.silenteight.hsbc.bridge.amqp.IngoingAmpqDefaults.*;
import static com.silenteight.hsbc.bridge.amqp.OutgoingAmqpDefaults.MODEL_PERSISTED_EXCHANGE;
import static com.silenteight.hsbc.bridge.amqp.OutgoingAmqpDefaults.WATCHLIST_PERSISTED_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.AE_EVENT_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.GOV_EVENT_EXCHANGE;
import static org.springframework.amqp.core.ExchangeBuilder.topicExchange;

@Configuration
@ConditionalOnClass(name = "com.rabbitmq.client.ConnectionFactory")
@EnableRabbit
@RequiredArgsConstructor
class AmqpMessagingConfiguration implements RabbitListenerConfigurer, BeanPostProcessor {

  private static final int QUEUE_MAX_PRIORITY = 10;

  private final BeanFactory beanFactory;
  private final MessageRegistry messageRegistry;

  @Override
  public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
    var conversionService = new DefaultConversionService();
    conversionService.addConverter(new AnyProtoMessageConverter(messageRegistry));

    var factory = new DefaultMessageHandlerMethodFactory();
    factory.setBeanFactory(beanFactory);
    factory.setConversionService(conversionService);
    factory.afterPropertiesSet();

    registrar.setMessageHandlerMethodFactory(factory);
  }

  @Bean
  @Primary
  MessageRegistry messageRegistryOverwrite() {
    return new MessageRegistryFactory(
        "com.silenteight",
        "com.google.protobuf",
        "com.google.rpc",
        "com.google.type"
    ).create();
  }


  @Bean
  Declarables ingoingRabbitBrokerDeclarables() {

    var deadLetterExchange =
        topicExchange(DEAD_LETTER_EXCHANGE).durable(true).build();

    var deadLetterQueue = deadLetterQueue();
    var deadLetterBinding = bind(
        deadLetterQueue, DEAD_LETTER_EXCHANGE, DEAD_LETTER_ROUTING_KEY);

    var modelPromotedQueue = queueMaxPriority(MODEL_PROMOTED_QUEUE).build();
    var modelPromotedBinding = bind(
        modelPromotedQueue, GOV_EVENT_EXCHANGE, MODEL_PROMOTED_ROUTING_KEY);

    var recommendationsQueue = queue(RECOMMENDATIONS_QUEUE).build();
    var recommendationsBinding = bind(
        recommendationsQueue, AE_EVENT_EXCHANGE, RECOMMENDATIONS_ROUTING_KEY);

    var worldcheckModelLoadedQueue = queueMaxPriority(WORLDCHECK_MODEL_LOADED_QUEUE).build();
    var worldCheckModelLoadedBinding = bind(
        worldcheckModelLoadedQueue, BRIDGE_MODEL_EXCHANGE, WORLDCHECK_MODEL_LOADED_ROUTING_KEY);

    var historicalDecisionsModelLoadedQueue =
        queueMaxPriority(HISTORICAL_DECISIONS_MODEL_LOADED_QUEUE).build();
    var historicalDecisionsModelLoadedBinding = bind(
        historicalDecisionsModelLoadedQueue,
        BRIDGE_MODEL_EXCHANGE,
        HISTORICAL_DECISIONS_MODEL_LOADED_ROUTING_KEY);

    var worldCheckModelPersistedQueue =
        queueMaxPriority(WORLDCHECK_MODEL_PERSISTED_QUEUE).build();
    var worldCheckModelPersistedBinding = bind(
        worldCheckModelPersistedQueue,
        BRIDGE_MODEL_EXCHANGE,
        WORLDCHECK_MODEL_PERSISTED_ROUTING_KEY);

    var historicalDecisionsModelPersistedQueue =
        queueMaxPriority(HISTORICAL_DECISIONS_MODEL_PERSISTED_QUEUE).build();
    var historicalDecisionsModelPersistedBinding = bind(
        historicalDecisionsModelPersistedQueue,
        BRIDGE_MODEL_EXCHANGE,
        HISTORICAL_DECISIONS_MODEL_PERSISTED_ROUTING_KEY);

    return new Declarables(
        deadLetterExchange, deadLetterQueue, deadLetterBinding,
        modelPromotedQueue, modelPromotedBinding,
        recommendationsQueue, recommendationsBinding,
        worldcheckModelLoadedQueue, worldCheckModelLoadedBinding,
        historicalDecisionsModelLoadedQueue, historicalDecisionsModelLoadedBinding,
        worldCheckModelPersistedQueue, worldCheckModelPersistedBinding,
        historicalDecisionsModelPersistedQueue, historicalDecisionsModelPersistedBinding);
  }

  @Bean
  Declarables outgoingRabbitBrokerDeclarables() {

    var watchlistPersistedExchangeName =
        topicExchange(WATCHLIST_PERSISTED_EXCHANGE).durable(true).build();
    var modelPersistedExchangeName =
        topicExchange(MODEL_PERSISTED_EXCHANGE).durable(true).build();

    return new Declarables(
        watchlistPersistedExchangeName,
        modelPersistedExchangeName
    );
  }
  private static QueueBuilder queue(String queueName) {
    return QueueBuilder.durable(queueName)
        .withArgument("x-queue-type", "classic")
        .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
  }

  private static QueueBuilder queueMaxPriority(String queueName) {
    return queue(queueName)
        .withArgument("x-max-priority", QUEUE_MAX_PRIORITY);
  }

  private static Queue deadLetterQueue() {
    return QueueBuilder.durable(DEAD_LETTER_QUEUE)
        .withArgument("x-queue-type", "classic")
        .withArgument("x-queue-mode", "lazy")
        .build();
  }

  private static Binding bind(Queue queue, String exchangeName, String routingKey) {
    return new Binding(queue.getName(), DestinationType.QUEUE, exchangeName, routingKey, null);
  }
}
