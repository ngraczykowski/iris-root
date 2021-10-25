package com.silenteight.warehouse.indexer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.messaging.IntegrationConfiguration;
import com.silenteight.sep.base.common.messaging.MessagingConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.environment.EnvironmentModule;
import com.silenteight.warehouse.common.integration.AmqpCommonModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.indexer.alert.AlertModule;
import com.silenteight.warehouse.indexer.match.MatchModule;
import com.silenteight.warehouse.indexer.production.ProductionIndexerProperties;
import com.silenteight.warehouse.indexer.production.ProductionMessageHandlerModule;
import com.silenteight.warehouse.indexer.production.indextracking.IndexTrackingModule;
import com.silenteight.warehouse.indexer.query.QueryAlertModule;
import com.silenteight.warehouse.indexer.simulation.SimulationIndexerProperties;
import com.silenteight.warehouse.indexer.simulation.SimulationMessageHandlerModule;
import com.silenteight.warehouse.indexer.simulation.analysis.AnalysisModule;
import com.silenteight.warehouse.test.client.TestClientModule;
import com.silenteight.warehouse.test.client.gateway.IndexerClientIntegrationProperties;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;
import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    AlertModule.class,
    AmqpCommonModule.class,
    AnalysisModule.class,
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    IndexTrackingModule.class,
    MatchModule.class,
    OpendistroModule.class,
    ProductionMessageHandlerModule.class,
    SimulationMessageHandlerModule.class,
    QueryAlertModule.class,
    TestElasticSearchModule.class,
    TestClientModule.class
})
@ImportAutoConfiguration({
    IntegrationConfiguration.class,
    MessagingConfiguration.class,
    RabbitAutoConfiguration.class,
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@EnableIntegration
@EnableIntegrationManagement
@RequiredArgsConstructor
@Slf4j
public class IndexerTestConfiguration {

  private final ProductionIndexerProperties productionProperties;
  private final SimulationIndexerProperties simulationProperties;
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
  Binding simulationIndexExchangeToQueueBinding(
      Exchange simulationCommandExchange,
      Queue simulationIndexingQueue) {

    return bind(
        simulationCommandExchange,
        testProperties.getSimulationIndexingTestClientOutbound().getRoutingKey(),
        simulationIndexingQueue);
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
  Binding simulationIndexedExchangeToQueueBinding(
      Exchange whEventExchange,
      Queue simulationIndexedQueue) {

    return bind(
        whEventExchange,
        simulationProperties.getSimulationIndexedOutbound().getRoutingKey(),
        simulationIndexedQueue);
  }

  @Bean
  Queue productionIndexingQueue() {
    return QueueBuilder
        .durable(productionProperties.getProductionIndexingInbound().getQueueName())
        .build();
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
        .topicExchange(productionProperties.getProductionIndexedOutbound().getExchangeName())
        .build();
  }

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP));
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
