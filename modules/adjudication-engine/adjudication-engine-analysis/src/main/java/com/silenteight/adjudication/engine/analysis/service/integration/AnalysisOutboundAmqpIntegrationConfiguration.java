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

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(AnalysisOutboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class AnalysisOutboundAmqpIntegrationConfiguration {

  @Valid
  private final AnalysisOutboundAmqpIntegrationProperties properties;

  private final AmqpOutboundFactory outboundFactory;

  @Bean
  IntegrationFlow addedAnalysisDatasetsOutboundIntegrationFlow() {
    return createOutboundFlow(
        AnalysisChannels.ADDED_ANALYSIS_ALERTS_OUTBOUND_CHANNEL,
        properties.getEventInternal().getOutboundExchangeName(),
        properties.getEventInternal().getAddedAnalysisDatasetsRoutingKey());
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

  private StandardIntegrationFlow createOutboundFlow(
      String outboundChannel, String outboundExchangeName, String outboundRoutingKey) {

    return from(outboundChannel)
        .log(Level.TRACE, getClass().getName() + "." + outboundExchangeName)
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
