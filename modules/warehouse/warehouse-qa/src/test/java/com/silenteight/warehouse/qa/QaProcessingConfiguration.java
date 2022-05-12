package com.silenteight.warehouse.qa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.messaging.IntegrationConfiguration;
import com.silenteight.sep.base.common.messaging.MessagingConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.common.integration.AmqpCommonModule;
import com.silenteight.warehouse.production.handler.ProductionMessageHandlerModule;
import com.silenteight.warehouse.production.handler.ProductionMessageHandlerProperties;
import com.silenteight.warehouse.production.persistence.ProductionPersistenceModule;
import com.silenteight.warehouse.qa.handler.QaMessageHandlerModule;
import com.silenteight.warehouse.qa.handler.QaMessageHandlerProperties;
import com.silenteight.warehouse.qa.processing.QaProcessingModule;
import com.silenteight.warehouse.test.client.TestClientModule;
import com.silenteight.warehouse.test.client.gateway.IndexerClientIntegrationProperties;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

import static com.silenteight.warehouse.qa.QaProcessingTestFixtures.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;

@ComponentScan(basePackageClasses = {
    AmqpCommonModule.class,
    ProductionPersistenceModule.class,
    ProductionMessageHandlerModule.class,
    QaMessageHandlerModule.class,
    QaProcessingModule.class,
    TestClientModule.class
})
@ImportAutoConfiguration({
    IntegrationConfiguration.class,
    MessagingConfiguration.class,
    RabbitAutoConfiguration.class,
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@EnableAutoConfiguration
@EnableIntegration
@EnableIntegrationManagement
@RequiredArgsConstructor
@Slf4j
public class QaProcessingConfiguration {

  private final QaMessageHandlerProperties qaProperties;
  private final ProductionMessageHandlerProperties productionProperties;
  private final IndexerClientIntegrationProperties testProperties;

  @Bean
  Binding qaIndexExchangeToQueueBinding(
      Exchange qaEventExchange,
      Queue qaIndexingQueue) {

    return bind(
        qaEventExchange,
        testProperties.getQaIndexingTestClientOutbound().getRoutingKey(),
        qaIndexingQueue);
  }

  @Bean
  Queue qaIndexingQueue() {
    return QueueBuilder
        .durable(qaProperties.getQaIndexingInbound().getQueueName())
        .build();
  }

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
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP));
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
