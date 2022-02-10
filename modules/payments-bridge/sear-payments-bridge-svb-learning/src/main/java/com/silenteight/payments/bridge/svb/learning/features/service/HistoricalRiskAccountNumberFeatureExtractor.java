package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
abstract class HistoricalRiskAccountNumberFeatureExtractor
    extends HistoricalRiskAssessmentFeatureExtractor {

  @Override
  protected String getAlertedPartyId(AlertedPartyData alertedPartyData) {
    var accountNumber = alertedPartyData.getAccountNumber();
    return StringUtils.isBlank(accountNumber) ? "" : accountNumber.toUpperCase().trim();
  }

  public abstract String getDiscriminator();

  public abstract String getFeature();
}
