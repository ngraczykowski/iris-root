package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_CUSTOMER_NAME_FEATURE;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_CUSTOMER_NAME_LEARNING_DISC;

@Service
@RequiredArgsConstructor
class HistoricalRiskCustomerNameExtractor extends HistoricalRiskAssessmentExtractorBase {

  private final HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureUseCase;

  @Override
  HistoricalRiskAssessmentFeatureUseCase getHistoricalRiskExtractor() {
    return historicalRiskAssessmentFeatureUseCase;
  }

  @Override
  protected String getFeature() {
    return HISTORICAL_RISK_CUSTOMER_NAME_FEATURE;
  }

  @Override
  protected String getAlertedPartyId(AlertedPartyData alertedPartyData) {
    return alertedPartyData.getNames().stream()
        .map(String::trim)
        .findFirst()
        .orElse("");
  }

  @Override
  protected String getDiscriminator() {
    return HISTORICAL_RISK_CUSTOMER_NAME_LEARNING_DISC;
  }
}
