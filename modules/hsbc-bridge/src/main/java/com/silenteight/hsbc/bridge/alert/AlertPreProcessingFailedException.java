package com.silenteight.hsbc.bridge.alert;

public class AlertPreProcessingFailedException extends RuntimeException {

  public AlertPreProcessingFailedException(String keyLabel) {
    super("Alert preprocessing failed, keyLabel = " + keyLabel);
  }
}
