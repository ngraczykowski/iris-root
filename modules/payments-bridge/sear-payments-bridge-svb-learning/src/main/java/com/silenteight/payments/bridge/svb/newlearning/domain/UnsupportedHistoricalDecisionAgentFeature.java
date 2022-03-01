package com.silenteight.payments.bridge.svb.newlearning.domain;

class UnsupportedHistoricalDecisionAgentFeature extends RuntimeException {

  private static final long serialVersionUID = 7438255714694047836L;

  UnsupportedHistoricalDecisionAgentFeature(String message) {
    super(message);
  }
}
