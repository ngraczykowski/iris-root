package com.silenteight.warehouse.retention.simulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.messaging.IntegrationConfiguration;
import com.silenteight.sep.base.common.messaging.MessagingConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.common.integration.AmqpCommonModule;
import com.silenteight.warehouse.simulation.handler.SimulationHandlerProperties;
import com.silenteight.warehouse.simulation.handler.SimulationMessageHandlerModule;
import com.silenteight.warehouse.simulation.processing.SimulationProcessingModule;
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

import static java.time.Instant.parse;
import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    AmqpCommonModule.class,
    RetentionSimulationModule.class,
    SimulationMessageHandlerModule.class,
    SimulationProcessingModule.class,
    TestClientModule.class
})
@ImportAutoConfiguration({
    IntegrationConfiguration.class,
    MessagingConfiguration.class,
    RabbitAutoConfiguration.class,
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class
})
@EnableAutoConfiguration
@EnableIntegration
@EnableIntegrationManagement
@RequiredArgsConstructor
@Slf4j
class RetentionSimulationTestConfiguration {

  static final String PROCESSING_TIMESTAMP = "2021-04-15T12:17:37.098Z";

  private final SimulationHandlerProperties simulationProperties;
  private final IndexerClientIntegrationProperties testProperties;

  @Bean
  Binding analysisExpiredIndexingTestBinding(
      Exchange simulationCommandExchange,
      Queue analysisExpiredIndexingQueue) {

    return bind(
        simulationCommandExchange,
        testProperties.getAnalysisExpiredIndexingTestClientOutbound().getRoutingKey(),
        analysisExpiredIndexingQueue);
  }

  @Bean
  Binding simulationIndexExchangeToQueueBinding(
      Exchange simulationCommandExchange,
      Queue simulationIndexingQueue) {

    return bind(
        simulationCommandExchange,
        testProperties.getSimulationIndexingTestClientOutbound().getRoutingKey(),
        simulationIndexingQueue);
  }

  @Bean
  Binding simulationIndexedExchangeToQueueBinding(
      Exchange whEventExchange,
      Queue simulationIndexedQueue) {

    return bind(
        whEventExchange,
        simulationProperties.getSimulationIndexedOutbound().getRoutingKey(),
        simulationIndexedQueue);
  }

  @Bean
  Queue simulationIndexingQueue() {
    return QueueBuilder
        .durable(simulationProperties.getSimulationIndexingInbound().getQueueName())
        .build();
  }

  @Bean
  TopicExchange whEventExchange() {
    return ExchangeBuilder
        .topicExchange(simulationProperties.getSimulationIndexedOutbound().getExchangeName())
        .build();
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

  @Bean
  UserAwareTokenProvider userAwareTokenProvider() {
    return mock(UserAwareTokenProvider.class);
  }

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP));
  }
}
