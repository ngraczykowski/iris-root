package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_TP;

@Component
class HistoricalRiskAccountNumberFeatureExtractor extends HistoricalRiskAssessmentFeatureExtractor {

  @Override
  protected String getFeature() {
    return HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE_TP;
  }

  @Override
  protected String getAlertedPartyId(AlertedPartyData alertedPartyData) {
    var accountNumber = alertedPartyData.getAccountNumber();
    return StringUtils.isBlank(accountNumber) ? "" : accountNumber.toUpperCase().trim();
  }
}
