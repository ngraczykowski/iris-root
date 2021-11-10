package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.integration.AnalysisChannels;
import com.silenteight.adjudication.engine.analysis.categoryrequest.integration.CategoryRequestChannels;
import com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration.PendingRecommendationChannels;
import com.silenteight.adjudication.engine.analysis.recommendation.integration.RecommendationChannels;
import com.silenteight.sep.base.common.messaging.AmqpOutboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpBaseOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.handler.LoggingHandler.Level;

import javax.validation.Valid;

import static com.silenteight.adjudication.engine.analysis.agentresponse.integration.AgentResponseChannels.DELETE_AGENT_EXCHANGE_OUTBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.dataset.dataset.integration.DataRetentionChannels.ALERTS_EXPIRED_OUTBOUND_CHANNEL;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(AnalysisOutboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class AnalysisOutboundAmqpIntegrationConfiguration {

  @Valid
  private final AnalysisOutboundAmqpIntegrationProperties properties;

  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow analysisAlertsAddedOutboundIntegrationFlow() {
    return createOutboundFlow(
        AnalysisChannels.ANALYSIS_ALERTS_ADDED_OUTBOUND_CHANNEL,
        properties.getEventInternal().getOutboundExchangeName(),
        properties.getEventInternal().getAnalysisAlertsAddedRoutingKey());
  }

  @Bean
  IntegrationFlow deleteAgentExchangeIntegrationFlow() {
    return createOutboundFlow(
        DELETE_AGENT_EXCHANGE_OUTBOUND_CHANNEL,
        properties.getEventInternal().getOutboundExchangeName(),
        properties.getEventInternal().getDeleteAgentExchangeRoutingKey());
  }

  @Bean
  IntegrationFlow pendingRecommendationsOutboundIntegrationFlow() {
    return createOutboundFlow(
        PendingRecommendationChannels.PENDING_RECOMMENDATIONS_OUTBOUND_CHANNEL,
        properties.getEventInternal().getOutboundExchangeName(),
        properties.getEventInternal().getPendingRecommendationsRoutingKey());
  }

  //@Bean
  IntegrationFlow matchCategoriesUpdatedOutboundIntegrationFlow() {
    return createOutboundFlow(
        CategoryRequestChannels.MATCH_CATEGORIES_UPDATED_OUTBOUND_CHANNEL,
        properties.getEventInternal().getOutboundExchangeName(),
        properties.getEventInternal().getMatchCategoriesUpdatedRoutingKey());
  }

  @Bean
  IntegrationFlow recommendationsGeneratedOutboundIntegrationFlow() {
    return createOutboundFlow(
        RecommendationChannels.RECOMMENDATIONS_GENERATED_OUTBOUND_CHANNEL,
        properties.getEvent().getOutboundExchangeName(),
        properties.getEvent().getRecommendationsGeneratedRoutingKey());
  }

  @Bean
  IntegrationFlow datasetExpiredOutboundIntegrationFlow() {
    return createOutboundFlow(
        ALERTS_EXPIRED_OUTBOUND_CHANNEL,
        properties.getEvent().getOutboundExchangeName(),
        properties.getEvent().getDatasetExpiredRoutingKey());
  }

  private StandardIntegrationFlow createOutboundFlow(
      String outboundChannel, String outboundExchangeName, String outboundRoutingKey) {

    return from(outboundChannel)
        .log(Level.TRACE, getClass().getName() + "." + outboundChannel)
        .handle(createOutboundAdapter(outboundExchangeName, outboundRoutingKey))
        .get();
  }

  private AmqpBaseOutboundEndpointSpec<?, ?> createOutboundAdapter(
      String outboundExchangeName, String outboundRoutingKey) {

    return outboundFactory
        .outboundAdapter()
        .exchangeName(outboundExchangeName)
        .routingKey(outboundRoutingKey);
  }
}
