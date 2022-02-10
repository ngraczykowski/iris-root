package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

abstract class HistoricalRiskAssessmentFeatureExtractor {

  protected abstract String getFeature();

  protected abstract String getAlertedPartyId(AlertedPartyData alertedPartyData);

  public abstract String getDiscriminator();
}
