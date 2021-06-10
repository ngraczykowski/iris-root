package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.agentresponse.integration.AgentResponseChannels;
import com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration.PendingRecommendationChannels;
import com.silenteight.adjudication.internal.v1.AddedAnalysisDatasets;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.validation.Valid;

import static com.silenteight.adjudication.engine.analysis.service.integration.IntegrationChannels.PENDING_RECOMMENDATIONS_PUB_SUB_CHANNEL;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(AnalysisInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class AnalysisInboundAmqpIntegrationConfiguration {

  @Valid
  private final AnalysisInboundAmqpIntegrationProperties properties;

  private final AmqpInboundFactory inboundFactory;

  @Bean
  IntegrationFlow analysisInboundIntegrationFlow() {
    return from(createInboundAdapter(properties.getEventInternalInboundQueueNames()))
        .<Object, Class<?>>route(Object::getClass, m -> m
            .channelMapping(
                AddedAnalysisDatasets.class,
                PendingRecommendationChannels.ADDED_ANALYSIS_DATASETS_INBOUND_CHANNEL)
            .channelMapping(
                PendingRecommendations.class,
                PENDING_RECOMMENDATIONS_PUB_SUB_CHANNEL)
        )
        .get();
  }

  @Bean
  IntegrationFlow agentResponseIntegrationFlow() {
    return from(createInboundAdapter(properties.getAgentResponse().getInboundQueueName()))
        .channel(AgentResponseChannels.AGENT_RESPONSE_INBOUND_CHANNEL)
        .get();
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }
}
