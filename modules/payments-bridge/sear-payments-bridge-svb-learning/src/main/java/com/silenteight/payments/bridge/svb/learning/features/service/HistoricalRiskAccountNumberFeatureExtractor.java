package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
class HistoricalRiskAccountNumberFeatureExtractor extends HistoricalRiskAssessmentFeatureExtractor {

  private static final String HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE =
      "historicalRiskAccountNumber";

  @Override
  protected String getFeature() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE;
  }

  @Override
  protected String getAlertedPartyId(AlertedPartyData alertedPartyData) {
    var accountNumber = alertedPartyData.getAccountNumber();
    return StringUtils.isBlank(accountNumber) ? "" : accountNumber.toUpperCase().trim();
  }
}
