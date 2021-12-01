package com.silenteight.warehouse.retention.production;

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
import com.silenteight.warehouse.retention.production.RetentionProductionModule;
import com.silenteight.warehouse.test.client.TestClientModule;
import com.silenteight.warehouse.test.client.gateway.IndexerClientIntegrationProperties;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

import static java.time.Instant.parse;
import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    AlertModule.class,
    AmqpCommonModule.class,
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    MatchModule.class,
    OpendistroModule.class,
    ProductionMessageHandlerModule.class,
    RetentionProductionModule.class,
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
public class RetentionProductionTestConfiguration {

  public static final String PROCESSING_TIMESTAMP = "2021-04-15T12:17:37.098Z";

  private final ProductionIndexerProperties productionProperties;
  private final IndexerClientIntegrationProperties testProperties;

  @Bean
  Binding retentionPersonalInformationIndexingBinding(
      Exchange retentionCommandExchange,
      Queue personalInformationExpiredIndexingQueue) {

    return bind(
        retentionCommandExchange,
        testProperties.getPersonalInformationExpiredIndexingTestClientOutbound().getRoutingKey(),
        personalInformationExpiredIndexingQueue);
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
  Binding retentionAlertsIndexingBinding(
      Exchange retentionCommandExchange,
      Queue alertsExpiredIndexingQueue) {

    return bind(
        retentionCommandExchange,
        testProperties.getAlertsExpiredIndexingTestClientOutbound().getRoutingKey(),
        alertsExpiredIndexingQueue);
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