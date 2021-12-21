package com.silenteight.warehouse.production;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.integration.AmqpCommonModule;
import com.silenteight.warehouse.production.handler.ProductionIndexerProperties;
import com.silenteight.warehouse.production.handler.ProductionMessageHandlerModule;
import com.silenteight.warehouse.production.persistence.ProductionPersistenceModule;
import com.silenteight.warehouse.test.client.TestClientModule;
import com.silenteight.warehouse.test.client.gateway.IndexerClientIntegrationProperties;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.silenteight.sep.base.testing.time.MockTimeSource.ARBITRARY_INSTANCE;
import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    ProductionMessageHandlerModule.class,
    ProductionPersistenceModule.class,
    AmqpCommonModule.class,
    TestClientModule.class
})
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
class ProductionMessageTestConfiguration {

  private final ProductionIndexerProperties productionProperties;
  private final IndexerClientIntegrationProperties testProperties;

  @Bean
  Binding productionIndexExchangeToQueueBinding(
      Exchange productionCommandExchange,
      Queue productionIndexingQueue) {

    return bind(
        productionCommandExchange,
        testProperties.getProductionIndexingTestClientOutbound().getRoutingKey(),
        productionIndexingQueue);
  }

  @Bean
  Binding productionIndexedExchangeToQueueBinding(
      Exchange whEventExchange,
      Queue productionIndexedQueue) {

    return bind(
        whEventExchange,
        productionProperties.getProductionIndexedOutbound().getRoutingKey(),
        productionIndexedQueue);
  }

  @Bean
  Queue productionIndexingQueue() {
    return QueueBuilder
        .durable(productionProperties.getProductionIndexingInbound().getQueueName())
        .build();
  }

  @Bean
  TopicExchange whEventExchange() {
    return ExchangeBuilder
        .topicExchange(productionProperties.getProductionIndexedOutbound().getExchangeName())
        .build();
  }

  @Bean
  TimeSource timeSource() {
    return ARBITRARY_INSTANCE;
  }

  @Bean
  UserAwareTokenProvider userAwareTokenProvider() {
    return mock(UserAwareTokenProvider.class);
  }

  private Binding bind(Exchange exchange, String routingKey, Queue queue) {
    log.info("exchange={}, routingKey={}, queue={}",
        exchange.getName(), routingKey, queue.getName());

    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(routingKey)
        .noargs();
  }
}
