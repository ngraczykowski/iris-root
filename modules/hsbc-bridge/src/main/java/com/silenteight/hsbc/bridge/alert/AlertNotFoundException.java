package com.silenteight.hsbc.bridge.alert;

public class AlertNotFoundException extends RuntimeException {

  public AlertNotFoundException(long bulkItemId) {
    super("Alert not found for bulkItemId=" + bulkItemId);
  }
}
