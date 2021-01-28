package com.silenteight.serp.governance.common.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(FeatureVectorSolvedAmqpProperties.class)
public class FeatureVectorSolvedAmqpIntegrationConfiguration {

  public static final String FEATURE_VECTOR_SOLVED_OUTBOUND_CHANNEL =
      "featureVectorSolvedOutboundChannel";
  public static final String FEATURE_VECTOR_SOLVED_INBOUND_CHANNEL =
      "featureVectorSolvedInboundChannel";

  private final AmqpOutboundFactory outboundFactory;
  private final AmqpInboundFactory inboundFactory;
  private final FeatureVectorSolvedAmqpProperties properties;

  @Bean
  TopicExchange featureVectorExchange() {
    return ExchangeBuilder
        .topicExchange(properties.getRequest().getExchangeName())
        .build();
  }

  @Bean
  Queue governanceAnalyticsQueue() {
    return QueueBuilder
        .durable(properties.getReceive().getQueueName())
        .build();
  }

  @Bean
  Binding featureVectorSolvedExchangeToQueueBinding(
      Exchange featureVectorSolvedExchange,
      Queue governanceAnalyticsQueue) {
    return BindingBuilder
        .bind(governanceAnalyticsQueue)
        .to(featureVectorSolvedExchange)
        .with(properties.getRequest().getRoutingKey())
        .noargs();
  }

  @Bean
  IntegrationFlow featureVectorSolvedChannelToExchangeIntegrationFlow() {
    return createOutputFlow(
        FEATURE_VECTOR_SOLVED_OUTBOUND_CHANNEL,
        properties.getRequest().getExchangeName(),
        properties.getRequest().getRoutingKey());
  }

  @Bean
  IntegrationFlow featureVectorSolvedQueueToChannelIntegrationFlow() {
    return createInputFlow(
        FEATURE_VECTOR_SOLVED_INBOUND_CHANNEL,
        properties.getReceive().getQueueName());
  }

  private IntegrationFlow createInputFlow(String channel, String queue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queue)))
        .channel(channel)
        .get();
  }

  private IntegrationFlow createOutputFlow(String channel, String exchange, String routingKey) {
    return flow -> flow
        .channel(channel)
        .handle(outboundFactory
            .outboundAdapter()
            .exchangeName(exchange)
            .routingKey(routingKey));
  }
}
