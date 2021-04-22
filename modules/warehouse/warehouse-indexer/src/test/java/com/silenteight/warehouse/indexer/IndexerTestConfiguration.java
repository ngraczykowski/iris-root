package com.silenteight.warehouse.indexer;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.IntegrationConfiguration;
import com.silenteight.sep.base.common.messaging.MessagingConfiguration;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.common.integration.AmqpCommonModule;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.indexer.indextestclient.gateway.IndexerClientIntegrationProperties;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;

@Configuration
@ComponentScan(basePackageClasses = {
    IndexerModule.class,
    AmqpCommonModule.class,
    TestElasticSearchModule.class
})
@ImportAutoConfiguration({
    IntegrationConfiguration.class,
    MessagingConfiguration.class,
    RabbitAutoConfiguration.class,
    ElasticsearchRestClientAutoConfiguration.class
})
@EnableIntegration
@EnableIntegrationManagement
@RequiredArgsConstructor
public class IndexerTestConfiguration {

  private final IndexerIntegrationProperties properties;
  private final IndexerClientIntegrationProperties testProperties;

  @Bean
  Binding alertIndexExchangeToQueueBinding(
      Exchange alertIndexExchange,
      Queue alertIndexingQueue) {
    return BindingBuilder
        .bind(alertIndexingQueue)
        .to(alertIndexExchange)
        .with(testProperties.getAlertIndexingTestClientOutbound().getRoutingKey())
        .noargs();
  }

  @Bean
  Binding alertIndexedExchangeToQueueBinding(
      Exchange alertIndexedExchange,
      Queue alertIndexedQueue) {
    return BindingBuilder
        .bind(alertIndexedQueue)
        .to(alertIndexedExchange)
        .with(properties.getAlertIndexedOutbound().getRoutingKey())
        .noargs();
  }

  @Bean
  Queue alertIndexingQueue() {
    return QueueBuilder
        .durable(properties.getAlertIndexingInbound().getQueueName())
        .build();
  }

  @Bean
  TopicExchange alertIndexedExchange() {
    return ExchangeBuilder
        .topicExchange(properties.getAlertIndexedOutbound().getExchangeName())
        .build();
  }

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP));
  }
}
