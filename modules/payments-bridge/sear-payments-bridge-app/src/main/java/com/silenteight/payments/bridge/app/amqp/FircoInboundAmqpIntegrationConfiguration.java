package com.silenteight.payments.bridge.app.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(FircoInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class FircoInboundAmqpIntegrationConfiguration {

  @Valid
  private final FircoInboundAmqpIntegrationProperties properties;

  private final AmqpInboundFactory inboundFactory;
  //
  //  @Bean
  //  IntegrationFlow analysisInboundIntegrationFlow() {
  //    return from(createInboundAdapter(properties.getEventInternalInboundQueueNames()))
  //        .<Object, Class<?>>route(Object::getClass, m -> m
  //            .channelMapping(
  //                AddedAnalysisDatasets.class,
  //                PendingRecommendationChannels.ADDED_ANALYSIS_DATASETS_INBOUND_CHANNEL)
  //            .channelMapping(
  //                PendingRecommendations.class,
  //                PENDING_RECOMMENDATIONS_PUB_SUB_CHANNEL)
  //        )
  //        .get();
  //  }
  //
  //  @Bean
  //  @ConditionalOnProperty(prefix = "ae.analysis.integration.outbound.agent", name = "enabled",
  //      havingValue = "true", matchIfMissing = true)
  //  IntegrationFlow agentResponseIntegrationFlow() {
  //    return from(createInboundAdapter(properties.getAgentResponseInboundQueueName()))
  //        .channel(AgentResponseChannels.AGENT_RESPONSE_INBOUND_CHANNEL)
  //        .get();
  //  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
