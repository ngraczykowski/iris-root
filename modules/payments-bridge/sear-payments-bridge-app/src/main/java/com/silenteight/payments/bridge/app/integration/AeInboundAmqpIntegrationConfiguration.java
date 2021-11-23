package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.GetRecommendationRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.GetRegisteredAlertIdUseCase;
import com.silenteight.payments.bridge.ae.recommendation.port.RecommendationClientPort;
import com.silenteight.payments.bridge.common.event.RecommendationsGeneratedEvent;
import com.silenteight.payments.bridge.firco.recommendation.model.AdjudicationEngineSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;

import java.util.UUID;
import javax.validation.Valid;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(AeInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
@Slf4j
class AeInboundAmqpIntegrationConfiguration {

  @Valid
  private final AeInboundAmqpIntegrationProperties properties;
  private final AmqpInboundFactory inboundFactory;
  private final AnalysisDataAccessPort analysisDataAccessPort;
  private final CreateRecommendationUseCase createRecommendationUseCase;
  private final RecommendationClientPort recommendationClientPort;
  private final GetRegisteredAlertIdUseCase getRegisteredAlertIdUseCase;

  @Bean
  IntegrationFlow recommendationGeneratedInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .filter(RecommendationsGenerated.class,
            payload -> analysisDataAccessPort.existsAnalysis(payload.getAnalysis()))
        .split(RecommendationsGenerated.class, payload -> {
          if (log.isDebugEnabled()) {
            log.debug(
                "Received recommendation generated notification: count={}",
                payload.getRecommendationInfosCount());
          }
          return payload.getRecommendationInfosList();
        })
        .handle(RecommendationInfo.class, (recommendationInfo, headers) -> {
          handleRecommendationInfo(recommendationInfo);
          return null;
        })
        .get();
  }

  private void handleRecommendationInfo(RecommendationInfo recommendationInfo) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Retrieving recommendation: recommendation={}, alert={}",
          recommendationInfo.getRecommendation(), recommendationInfo.getAlert());
    }

    var recommendationWithMetadata =
        recommendationClientPort.receiveRecommendation(
            GetRecommendationRequest.newBuilder()
                .setRecommendation(recommendationInfo.getRecommendation()).build());

    var recommendation = recommendationWithMetadata.getRecommendation();
    var alertId = getRegisteredAlertIdUseCase.getAlertId(recommendation.getAlert());

    createRecommendationUseCase.create(new AdjudicationEngineSourcedRecommendation(
        UUID.fromString(alertId), recommendationWithMetadata));

  }

  @EventListener
  public void onRecommendationsGeneratedEventListener(
      RecommendationsGeneratedEvent recommendationsGeneratedEvent) {
    var recommendationsGenerated = recommendationsGeneratedEvent.getRecommendationsGenerated();
    recommendationsGenerated.getRecommendationInfosList().forEach(
        this::handleRecommendationInfo);
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }

}
