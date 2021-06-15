package com.silenteight.warehouse.indexer;

import lombok.RequiredArgsConstructor;

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
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.test.client.TestClientModule;
import com.silenteight.warehouse.test.client.gateway.IndexerClientIntegrationProperties;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;
import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    IndexerModule.class,
    AmqpCommonModule.class,
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
public class IndexerTestConfiguration {

  private final IndexerIntegrationProperties properties;
  private final IndexerClientIntegrationProperties testProperties;

  @Bean
  Binding productionIndexExchangeToQueueBinding(
      Exchange commonIndexExchange,
      Queue productionIndexingQueue) {
    return BindingBuilder
        .bind(productionIndexingQueue)
        .to(commonIndexExchange)
        .with(testProperties.getProductionIndexingTestClientOutbound().getRoutingKey())
        .noargs();
  }

  @Bean
  Binding simulationIndexExchangeToQueueBinding(
      Exchange commonIndexExchange,
      Queue simulationIndexingQueue) {
    return BindingBuilder
        .bind(simulationIndexingQueue)
        .to(commonIndexExchange)
        .with(testProperties.getSimulationIndexingTestClientOutbound().getRoutingKey())
        .noargs();
  }

  @Bean
  Binding productionIndexedExchangeToQueueBinding(
      Exchange alertIndexedExchange,
      Queue alertIndexedQueue) {
    return BindingBuilder
        .bind(alertIndexedQueue)
        .to(alertIndexedExchange)
        .with(properties.getProductionIndexedOutbound().getRoutingKey())
        .noargs();
  }

  @Bean
  Binding simulationIndexedExchangeToQueueBinding(
      Exchange alertIndexedExchange,
      Queue alertIndexedQueue) {
    return BindingBuilder
        .bind(alertIndexedQueue)
        .to(alertIndexedExchange)
        .with(properties.getSimulationIndexedOutbound().getRoutingKey())
        .noargs();
  }

  @Bean
  Queue productionIndexingQueue() {
    return QueueBuilder
        .durable(properties.getProductionIndexingInbound().getQueueName())
        .build();
  }

  @Bean
  Queue simulationIndexingQueue() {
    return QueueBuilder
        .durable(properties.getSimulationIndexingInbound().getQueueName())
        .build();
  }

  @Bean
  TopicExchange alertIndexedExchange() {
    return ExchangeBuilder
        .topicExchange(properties.getProductionIndexedOutbound().getExchangeName())
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
}
