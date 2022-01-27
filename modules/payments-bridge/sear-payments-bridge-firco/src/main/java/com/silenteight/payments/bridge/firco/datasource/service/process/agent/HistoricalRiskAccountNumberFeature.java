package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.HISTORICAL_RISK_ACCOUNT_NUMBER_FEATURE;

@Component
class HistoricalRiskAccountNumberFeature extends HistoricalRiskAssessmentFeature {

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
