package com.silenteight.hsbc.bridge.alert;

public class AlertNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -517553714868791346L;

  public AlertNotFoundException(long bulkItemId) {
    super("Alert not found for bulkItemId=" + bulkItemId);
  }
}
