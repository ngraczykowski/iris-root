package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
class HistoricalRiskAccountNumberFeature extends HistoricalRiskAssessmentFeature {

  private static final String HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE =
      "historicalRiskAccountNumber";

  @Override
  protected String getFeatureName() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE;
  }

  @Override
  protected String getAlertedPartyId(AlertedPartyData alertedPartyData) {
    var accountNumber = alertedPartyData.getAccountNumber();
    if (StringUtils.isBlank(accountNumber)) {
      return "";
    }
    return accountNumber.toUpperCase().trim();
  }
}
