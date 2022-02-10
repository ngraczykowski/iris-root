package com.silenteight.payments.bridge.svb.learning.features.service;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_FP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP;

@Component
class HistoricalRiskAccountNumberFeatureFpExtractor
    extends HistoricalRiskAccountNumberFeatureExtractor {

  @Override
  public String getDiscriminator() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_FP;
  }

  @Override
  public String getFeature() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_FP;
  }
}
