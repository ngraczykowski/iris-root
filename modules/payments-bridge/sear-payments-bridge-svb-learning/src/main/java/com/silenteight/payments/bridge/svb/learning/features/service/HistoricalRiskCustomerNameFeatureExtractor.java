package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.springframework.stereotype.Component;

@Component
class HistoricalRiskCustomerNameFeatureExtractor extends HistoricalRiskAssessmentFeatureExtractor {

  private static final String HISTORICAL_RISK_CUSTOMER_NAME_FEATURE = "historicalRiskCustomerName";

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
