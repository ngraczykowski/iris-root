package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;

abstract class HistoricalRiskAccountNumberFeature extends HistoricalRiskAssessmentFeature {

  @Override
  protected String getAlertedPartyId(AlertedPartyData alertedPartyData) {
    var accountNumber = alertedPartyData.getAccountNumber();
    if (StringUtils.isBlank(accountNumber)) {
      return "";
    }
    return accountNumber.toUpperCase().trim();
  }

}
