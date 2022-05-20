package com.silenteight.warehouse.production;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.authorization.RoleAccessor;
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

import static com.silenteight.warehouse.production.ProductionProcessingTestFixtures.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;
import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    AmqpCommonModule.class,
    ProductionMessageHandlerModule.class,
    ProductionPersistenceModule.class,
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
public class ProductionProcessingConfiguration {

  private final ProductionMessageHandlerProperties productionProperties;
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
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP));
  }

  @Bean
  RoleAccessor roleAccessor() {
    return mock(RoleAccessor.class);
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
