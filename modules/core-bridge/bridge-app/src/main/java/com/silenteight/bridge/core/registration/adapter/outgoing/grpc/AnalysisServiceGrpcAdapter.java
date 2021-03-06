package com.silenteight.bridge.core.registration.adapter.outgoing.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.library.v1.analysis.*;
import com.silenteight.adjudication.api.library.v1.analysis.AddAlertsToAnalysisIn.Alert;
import com.silenteight.adjudication.api.library.v1.analysis.AddAlertsToAnalysisOut.AddedAlert;
import com.silenteight.bridge.core.registration.domain.model.AlertsAddedToAnalysis;
import com.silenteight.bridge.core.registration.domain.model.AlertsAddedToAnalysis.Status;
import com.silenteight.bridge.core.registration.domain.model.Analysis;
import com.silenteight.bridge.core.registration.domain.model.DefaultModel;
import com.silenteight.bridge.core.registration.domain.model.DefaultModelFeature;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;

import com.google.protobuf.Timestamp;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class AnalysisServiceGrpcAdapter implements AnalysisService {

  private static final boolean ATTACH_RECOMMENDATION_TO_ANALYSIS = true;
  private static final boolean ATTACH_METADATA_TO_ANALYSIS = true;

  private final AnalysisServiceClient analysisServiceClient;

  @Override
  @Retryable(value = AdjudicationEngineLibraryRuntimeException.class,
      maxAttemptsExpression = "${grpc.client.retry.max-attempts}",
      backoff = @Backoff(
          multiplierExpression = "${grpc.client.retry.multiplier}",
          delayExpression = "${grpc.client.retry.delay-in-milliseconds}"))
  public Analysis create(DefaultModel defaultModel) {
    var createAnalysisIn = CreateAnalysisIn.builder()
        .categories(defaultModel.categories())
        .policy(defaultModel.policyName())
        .strategy(defaultModel.strategyName())
        .features(getFeatures(defaultModel.features()))
        .notificationFlags(getNotificationFlags())
        .build();
    log.info("Creating analysis [{}].", createAnalysisIn);
    var analysis = analysisServiceClient.createAnalysis(createAnalysisIn);
    log.info("Analysis [{}] created.", analysis);
    return new Analysis(analysis.getName());
  }

  @Override
  @Retryable(
      value = AdjudicationEngineLibraryRuntimeException.class,
      maxAttemptsExpression = "${grpc.client.retry.add-alerts-to-analysis.max-attempts}",
      backoff = @Backoff(
          multiplierExpression = "${grpc.client.retry.add-alerts-to-analysis.multiplier}",
          delayExpression = "${grpc.client.retry.add-alerts-to-analysis.delay-in-milliseconds}",
          maxDelayExpression = "${grpc.client.retry.add-alerts-to-analysis.max-delay-in-milliseconds}"))
  public AlertsAddedToAnalysis addAlertsToAnalysis(
      String analysisName, List<String> alertNames, Timestamp alertDeadlineTime) {
    log.info("Adding [{}] alerts to analysis [{}].", alertNames.size(), analysisName);
    var addAlertsToAnalysisIn = AddAlertsToAnalysisIn.builder()
        .analysisName(analysisName)
        .alerts(createAlerts(alertNames, alertDeadlineTime))
        .build();

    var response = analysisServiceClient.addAlertsToAnalysis(addAlertsToAnalysisIn);
    return new AlertsAddedToAnalysis(Status.SUCCESS, getAlertNames(response));
  }

  @Recover
  public AlertsAddedToAnalysis addAlertsToAnalysisRecover(
      AdjudicationEngineLibraryRuntimeException e, String analysisName, List<String> alertNames,
      Timestamp alertDeadlineTime) {
    log.warn("Adding alerts to analysis failed. Recover after retry max attempts exhausted.");
    return new AlertsAddedToAnalysis(Status.FAILURE, alertNames);
  }

  private List<FeatureIn> getFeatures(List<DefaultModelFeature> features) {
    return features.stream()
        .map(defaultModelFeature -> FeatureIn.builder()
            .name(defaultModelFeature.name())
            .agentConfig(defaultModelFeature.agentConfig())
            .build())
        .toList();
  }

  private List<Alert> createAlerts(List<String> alertNames, Timestamp alertDeadlineTime) {
    return alertNames.stream()
        .map(alertName -> Alert.builder()
            .name(alertName)
            .deadlineTime(alertDeadlineTime)
            .build())
        .toList();
  }

  private NotificationFlagsIn getNotificationFlags() {
    return NotificationFlagsIn.builder()
        .attachRecommendation(ATTACH_RECOMMENDATION_TO_ANALYSIS)
        .attachMetadata(ATTACH_METADATA_TO_ANALYSIS)
        .build();
  }

  private List<String> getAlertNames(AddAlertsToAnalysisOut response) {
    return response.getAddedAlerts().stream()
        .map(AddedAlert::getName)
        .toList();
  }
}
