package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_TP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP;

@Service
class HistoricalRiskAccountNumberTruePositiveExtractor
    extends HistoricalRiskAccountNumberExtractor {

  HistoricalRiskAccountNumberTruePositiveExtractor(
      HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureUseCase) {
    super(historicalRiskAssessmentFeatureUseCase);
  }

  @Override
  protected String getFeature() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_TP;
  }

  @Override
  protected String getDiscriminator() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP;
  }
}
