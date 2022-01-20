package com.silenteight.bridge.core.registration.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.analysis.*;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationIncomingRecommendationsGeneratedConfigurationProperties;
import com.silenteight.bridge.core.registration.infrastructure.RegistrationAnalysisProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
class AnalysisServiceClientMock implements AnalysisServiceClient {

  private final RabbitTemplate rabbitTemplate;
  private final RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties;
  private final RegistrationAnalysisProperties registrationAnalysisProperties;

  @Override
  public AnalysisDatasetOut addDataset(AddDatasetIn request) {
    log.info("MOCK: Add dataset");
    return null;
  }

  @Override
  public CreateAnalysisOut createAnalysis(CreateAnalysisIn request) {
    log.info("MOCK: Create analysis");
    return CreateAnalysisOut.builder()
        .name("analysis_name" + UUID.randomUUID())
        .policy("analysis_policy")
        .strategy("analysis_strategy")
        .build();
  }

  @Override
  public GetAnalysisOut getAnalysis(String analysis) {
    log.info("MOCK: Get analysis");
    return null;
  }

  @Override
  public AddAlertsToAnalysisOut addAlertsToAnalysis(AddAlertsToAnalysisIn request) {
    log.info("MOCK: Add alerts to analysis: " + request);
    if (registrationAnalysisProperties.mockRecommendationsGeneration()) {
      publishRecommendationsGeneratedEvent(request);
    }
    return AddAlertsToAnalysisOut.builder()
        .addedAlerts(List.of())
        .build();
  }

  private void publishRecommendationsGeneratedEvent(AddAlertsToAnalysisIn request) {
    var recommendations = request.getAlerts()
        .stream().map(alert -> RecommendationInfo.newBuilder()
            .setAlert(alert.getName())
            .setRecommendation("recommendation")
            .build())
        .toList();
    var event = RecommendationsGenerated.newBuilder()
        .setAnalysis(request.getAnalysisName())
        .addAllRecommendationInfos(recommendations)
        .build();
    rabbitTemplate.convertAndSend(
        properties.exchangeName(), properties.exchangeRoutingKey(), event);
  }
}
