package com.silenteight.bridge.core.registration.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.analysis.*;
import com.silenteight.adjudication.api.library.v1.analysis.AddAlertsToAnalysisOut.AddedAlert;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationIncomingRecommendationsGeneratedConfigurationProperties;
import com.silenteight.bridge.core.registration.infrastructure.application.RegistrationAnalysisProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
class AnalysisServiceClientMock implements AnalysisServiceClient {

  private final RabbitTemplate rabbitTemplate;
  private final RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties;
  private final RegistrationAnalysisProperties registrationAnalysisProperties;
  private final RecommendationCreatorMock recommendationCreator;

  @Override
  public AnalysisDatasetOut addDataset(AddDatasetIn request) {
    log.info("MOCK: Add dataset.");
    return null;
  }

  @Override
  public CreateAnalysisOut createAnalysis(CreateAnalysisIn request) {
    log.info("MOCK: Create analysis.");
    return CreateAnalysisOut.builder()
        .name("analysis_name" + UUID.randomUUID())
        .policy("analysis_policy")
        .strategy("analysis_strategy")
        .build();
  }

  @Override
  public GetAnalysisOut getAnalysis(String analysis) {
    log.info("MOCK: Get analysis.");
    return null;
  }

  @Override
  public AddAlertsToAnalysisOut addAlertsToAnalysis(AddAlertsToAnalysisIn request) {
    log.info(
        "MOCK: Add [{}] alerts to analysis [{}].",
        request.getAlerts().size(),
        request.getAnalysisName());
    if (registrationAnalysisProperties.mockRecommendationsGeneration()) {
      publishRecommendationsGeneratedEvent(request);
    }
    return AddAlertsToAnalysisOut.builder()
        .addedAlerts(request.getAlerts().stream()
            .map(this::createAddedAlerts)
            .toList())
        .build();
  }

  private void publishRecommendationsGeneratedEvent(AddAlertsToAnalysisIn request) {
    var recommendations =
        recommendationCreator.getRecommendations(request.getAnalysisName());
    var event = RecommendationsGenerated.newBuilder()
        .setAnalysis(request.getAnalysisName())
        .addAllRecommendationInfos(recommendations)
        .build();
    rabbitTemplate.convertAndSend(
        properties.exchangeName(), properties.exchangeRoutingKey(), event);
  }

  private AddedAlert createAddedAlerts(AddAlertsToAnalysisIn.Alert alert) {
    return AddedAlert.builder()
        .name(alert.getName())
        .createdAt(alert.getDeadlineTime())
        .build();
  }
}
