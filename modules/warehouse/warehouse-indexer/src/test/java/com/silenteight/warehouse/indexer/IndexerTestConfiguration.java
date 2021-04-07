package com.silenteight.warehouse.indexer;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.integration.AmqpCommonModule;
import com.silenteight.warehouse.indexer.indextestclient.gateway.IndexerClientIntegrationProperties;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

@Configuration
@ComponentScan(basePackageClasses = {
    IndexerModule.class,
    AmqpCommonModule.class
})
@ImportAutoConfiguration({
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
}
