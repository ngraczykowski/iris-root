package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_CUSTOMER_NAME_FEATURE;

@Component
class HistoricalRiskCustomerNameFeature extends HistoricalRiskAssessmentFeature {

  @Override
  protected String getFeatureName() {
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
