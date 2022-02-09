package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_TP;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP;

@Component
class HistoricalRiskAccountNumberTruePositiveFeature extends HistoricalRiskAccountNumberFeature {

  @Override
  protected String getFeatureName() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_TP;
  }

  @Override
  protected String getDiscriminator() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_LEARNING_DISC_TP;
  }
}
