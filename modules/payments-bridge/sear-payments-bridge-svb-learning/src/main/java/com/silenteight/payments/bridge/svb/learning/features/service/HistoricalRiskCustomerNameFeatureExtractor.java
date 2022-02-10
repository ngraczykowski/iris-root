package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_CUSTOMER_NAME_FEATURE;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_CUSTOMER_NAME_LEARNING_DISC;

@Component
class HistoricalRiskCustomerNameFeatureExtractor extends HistoricalRiskAssessmentFeatureExtractor {

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
  public String getDiscriminator() {
    return HISTORICAL_RISK_CUSTOMER_NAME_LEARNING_DISC;
  }
}
