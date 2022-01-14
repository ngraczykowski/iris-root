package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.library.v1.analysis.AddAlertsToAnalysisIn;
import com.silenteight.adjudication.api.library.v1.analysis.AddAlertsToAnalysisIn.Alert;
import com.silenteight.adjudication.api.library.v1.analysis.AnalysisServiceClient;
import com.silenteight.adjudication.api.library.v1.analysis.CreateAnalysisIn;
import com.silenteight.adjudication.api.library.v1.analysis.FeatureIn;
import com.silenteight.bridge.core.registration.domain.model.Analysis;
import com.silenteight.bridge.core.registration.domain.model.DefaultModel;
import com.silenteight.bridge.core.registration.domain.model.DefaultModelFeature;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;

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
      String analysisName, List<String> alertNames, Timestamp alertDeadlineTime) {
    log.info("Adding {} alerts to analysis {}", alertNames.size(), analysisName);
    var addAlertsToAnalysisIn = AddAlertsToAnalysisIn.builder()
        .analysisName(analysisName)
        .alerts(createAlerts(alertNames, alertDeadlineTime))
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

  private List<Alert> createAlerts(List<String> alertNames, Timestamp alertDeadlineTime) {
    return alertNames.stream()
        .map(alertName -> Alert.builder()
            .name(alertName)
            .deadlineTime(alertDeadlineTime)
            .build())
        .toList();
  }
}
