package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_FP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.getFullFeatureName;

@Service
class HistoricalRiskAccountNumberFalsePositiveExtractor
    extends HistoricalRiskAccountNumberExtractor {

  HistoricalRiskAccountNumberFalsePositiveExtractor(
      HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureUseCase) {
    super(historicalRiskAssessmentFeatureUseCase);
  }

  @Override
  protected String getFeature() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_FP;
  }

  protected String getDiscriminator() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP;
  }

  @Override
  public String name() {
    return getFullFeatureName(HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_FP);
  }
}
