package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
abstract class HistoricalRiskAccountNumberExtractor
    extends HistoricalRiskAssessmentExtractorBase {

  private final HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureUseCase;

  @Override
  HistoricalRiskAssessmentFeatureUseCase getHistoricalRiskExtractor() {
    return historicalRiskAssessmentFeatureUseCase;
  }


  @Override
  protected String getAlertedPartyId(AlertedPartyData alertedPartyData) {
    var accountNumber = alertedPartyData.getAccountNumber();
    return StringUtils.isBlank(accountNumber) ? "" : accountNumber.toUpperCase().trim();
  }
}
