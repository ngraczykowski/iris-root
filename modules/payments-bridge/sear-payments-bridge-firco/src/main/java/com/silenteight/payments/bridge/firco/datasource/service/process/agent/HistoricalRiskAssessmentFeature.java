package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

abstract class HistoricalRiskAssessmentFeature {

  protected abstract String getFeatureName();

  protected abstract String getAlertedPartyId(AlertedPartyData alertedPartyData);
}
