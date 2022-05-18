package com.silenteight.payments.bridge.governance.solvingmodel.model;

public final class GovernanceClientException extends RuntimeException {

  private static final long serialVersionUID = 7568497398228085792L;

  public GovernanceClientException(String message, Exception e) {
    super(message, e);
  }
}
