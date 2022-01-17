package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class HistoricalRiskCustomerNameExtractor extends HistoricalRiskAssessmentExtractorBase {

  private static final String HISTORICAL_RISK_CUSTOMER_NAME_FEATURE = "historicalRiskCustomerName";
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
}
