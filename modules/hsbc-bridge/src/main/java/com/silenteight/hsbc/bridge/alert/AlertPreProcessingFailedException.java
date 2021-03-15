package com.silenteight.hsbc.bridge.alert;

public class AlertPreProcessingFailedException extends RuntimeException {

  public AlertPreProcessingFailedException(int caseId) {
    super("Alert preprocessing failed, caseId = " + caseId);
  }
}
