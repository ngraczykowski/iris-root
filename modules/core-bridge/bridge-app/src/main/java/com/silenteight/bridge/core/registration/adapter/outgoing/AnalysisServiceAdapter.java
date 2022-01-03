package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.library.v1.analysis.AddAlertsToAnalysisIn;
import com.silenteight.adjudication.api.library.v1.analysis.AddAlertsToAnalysisIn.Alert;
import com.silenteight.adjudication.api.library.v1.analysis.AnalysisServiceClient;
import com.silenteight.adjudication.api.library.v1.analysis.CreateAnalysisIn;
import com.silenteight.adjudication.api.library.v1.analysis.FeatureIn;
import com.silenteight.bridge.core.registration.domain.AddAlertToAnalysisCommand;
import com.silenteight.bridge.core.registration.domain.port.outgoing.Analysis;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModel;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelFeature;

import com.google.protobuf.Timestamp;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class AnalysisServiceAdapter implements AnalysisService {

  private final AnalysisServiceClient analysisServiceClient;

  @Override
  @Retryable(AdjudicationEngineLibraryRuntimeException.class)
  public Analysis create(DefaultModel defaultModel) {
    var createAnalysisIn = CreateAnalysisIn.builder()
        .categories(defaultModel.categories())
        .policy(defaultModel.policyName())
        .strategy(defaultModel.strategyName())
        .features(getFeatures(defaultModel.features()))
        .build();
    log.info("Creating analysis: {}", createAnalysisIn);
    var analysis = analysisServiceClient.createAnalysis(createAnalysisIn);
    log.info("Analysis created: {}", analysis);
    return new Analysis(analysis.getName());
  }

  @Override
  @Retryable(AdjudicationEngineLibraryRuntimeException.class)
  public void addAlertsToAnalysis(
      String analysisName, List<AddAlertToAnalysisCommand> alerts, Timestamp alertDeadlineTime) {
    log.info("Adding {} alerts to analysis {}", alerts.size(), analysisName);
    var addAlertsToAnalysisIn = AddAlertsToAnalysisIn.builder()
        .analysisName(analysisName)
        .alerts(createAlerts(alerts, alertDeadlineTime))
        .build();
    analysisServiceClient.addAlertsToAnalysis(addAlertsToAnalysisIn);
  }

  private List<FeatureIn> getFeatures(List<DefaultModelFeature> features) {
    return features.stream()
        .map(defaultModelFeature -> FeatureIn.builder()
            .name(defaultModelFeature.name())
            .agentConfig(defaultModelFeature.agentConfig())
            .build())
        .toList();
  }

  private List<Alert> createAlerts(
      List<AddAlertToAnalysisCommand> alerts, Timestamp alertDeadlineTime) {
    return alerts.stream()
        .map(alert -> Alert.builder()
            .name(alert.alertId())
            .deadlineTime(alertDeadlineTime)
            .build())
        .toList();
  }
}
