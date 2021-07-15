package com.silenteight.simulator.processing.alert.index.amqp.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.simulator.processing.alert.index.amqp.AlertIndexProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

import static com.silenteight.simulator.processing.alert.index.amqp.gateway.GatewayConfiguration.ACK_MESSAGES_OUTBOUND_CHANNEL;
import static com.silenteight.simulator.processing.alert.index.amqp.gateway.GatewayConfiguration.RECOMMENDATIONS_OUTBOUND_CHANNEL;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AlertIndexProperties.class)
class ListenerConfiguration {

  public static final String RECOMMENDATIONS_INBOUND_CHANNEL =
      "recommendationsInboundChannel";

  public static final String ACK_MESSAGES_INBOUND_CHANNEL =
      "ackMessagesInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow recommendationQueueToChannelIntegrationFlow(
      @Valid AlertIndexProperties properties) {

    return createInputFlow(
        RECOMMENDATIONS_INBOUND_CHANNEL,
        properties.getRecommendationsInbound().getQueueName());
  }

  @Bean
  IntegrationFlow ackMessagesQueueToChannelIntegrationFlow(
      @Valid AlertIndexProperties properties) {

    return createInputFlow(
        ACK_MESSAGES_INBOUND_CHANNEL,
        properties.getAckMessagesInbound().getQueueName());
  }

  @Bean
  RecommendationsGeneratedFlowAdapter recommendationsGeneratedFlowAdapter(
      RecommendationsGeneratedMessageHandler handler) {

    return new RecommendationsGeneratedFlowAdapter(
        RECOMMENDATIONS_INBOUND_CHANNEL,
        RECOMMENDATIONS_OUTBOUND_CHANNEL,
        handler);
  }

  @Bean
  AckMessageFlowAdapter ackMessagesCommandIntegrationFlowAdapter(
      AckMessageHandler handler) {

    return new AckMessageFlowAdapter(
        ACK_MESSAGES_INBOUND_CHANNEL,
        ACK_MESSAGES_OUTBOUND_CHANNEL,
        handler);
  }

  private IntegrationFlow createInputFlow(String channel, String queue) {
    return IntegrationFlows
        .from(inboundFactory
            .simpleAdapter()
            .configureContainer(c -> c.addQueueNames(queue)))
        .channel(channel)
        .get();
  }
}
