package com.silenteight.simulator.processing.alert.index.amqp.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
import com.silenteight.simulator.processing.alert.index.amqp.RecommendationsGeneratedProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import javax.validation.Valid;

import static com.silenteight.simulator.processing.alert.index.amqp.gateway.RecommendationsGeneratedGatewayConfiguration.RECOMMENDATIONS_OUTBOUND_CHANNEL;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RecommendationsGeneratedProperties.class)
class RecommendationsGeneratedListenerConfiguration {

  public static final String RECOMMENDATIONS_INBOUND_CHANNEL =
      "recommendationsInboundChannel";

  @NonNull
  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow recommendationQueueToChannelIntegrationFlow(
      @Valid RecommendationsGeneratedProperties properties) {

    return createInputFlow(
        RECOMMENDATIONS_INBOUND_CHANNEL,
        properties.getRecommendationsInbound().getQueueName());
  }

  @Bean
  RecommendationsGeneratedFlowAdapter recommendationsGeneratedFlowAdapter(
      RecommendationsGeneratedMessageHandler handler) {

    return new RecommendationsGeneratedFlowAdapter(
        RECOMMENDATIONS_INBOUND_CHANNEL,
        RECOMMENDATIONS_OUTBOUND_CHANNEL,
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
